package trianglz.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.components.HideKeyboardOnTouch;
import trianglz.core.presenters.CreatePersonalEventPresenter;
import trianglz.core.presenters.FragmentCommunicationInterface;
import trianglz.core.views.CreatePersonalEventView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 05/09/2019.
 */
public class CreatePersonalEventFragment extends Fragment implements View.OnClickListener, CreatePersonalEventPresenter {

    private StudentMainActivity activity;
    private View rootView;
    private Student student;
    private View parentView;
    private CreatePersonalEventView createPersonalEventView;
    private int daySelected, monthSelected, yearSelected;
    private IImageLoader imageLoader;
    private AvatarView studentImageView;
    private Date firstDate = new Date(), secondDate = new Date();
    private boolean isTimeSet = false, isDateDialogShowing = false;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private ImageButton backBtn;
    private String studentName = "";
    private EditText subjectEditText, notesEditText;
    private Button startDateBtn, endDateBtn, createEventBtn, cancelEventBtn;
    private View whenView, toView, subjectView, notesView;
    private FragmentCommunicationInterface fragmentCommunicationInterface;

    public static CreatePersonalEventFragment newInstance(FragmentCommunicationInterface fragmentCommunicationInterface) {
        CreatePersonalEventFragment createPersonalEventFragment = new CreatePersonalEventFragment();
        createPersonalEventFragment.fragmentCommunicationInterface = fragmentCommunicationInterface;
        return createPersonalEventFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
//        activity.toolbarView.setVisibility(View.GONE);
//        activity.headerLayout.setVisibility(View.GONE);
        rootView = inflater.inflate(R.layout.activity_create_personal_event, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        // checkUserType();
        setListeners();
        studentName = student.firstName + " " + student.lastName;
        setStudentImage(student.getAvatar(), studentName);
        onBackPress();
    }

    private void onBackPress() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            student = (Student) bundle.getSerializable(Constants.STUDENT);
        }
    }

    private void bindViews() {
        parentView = rootView.findViewById(R.id.root_view);
        backBtn = rootView.findViewById(R.id.btn_back);
        subjectEditText = rootView.findViewById(R.id.event_subject_edit_text);
        studentImageView = rootView.findViewById(R.id.img_student);
        notesEditText = rootView.findViewById(R.id.event_notes_edit_text);
        startDateBtn = rootView.findViewById(R.id.event_start_date_btn);
        endDateBtn = rootView.findViewById(R.id.event_end_date_btn);
        createEventBtn = rootView.findViewById(R.id.event_create_btn);
        cancelEventBtn = rootView.findViewById(R.id.event_cancel_btn);
        whenView = rootView.findViewById(R.id.when_view);
        toView = rootView.findViewById(R.id.to_view);
        subjectView = rootView.findViewById(R.id.subject_view);
        notesView = rootView.findViewById(R.id.notes_view);
        createPersonalEventView = new CreatePersonalEventView(activity, this);
        startDateBtn.setHint(getCurrentDate());
        endDateBtn.setHint(getCurrentDate());
        if (Util.getLocale(activity).equals("ar")) {
            startDateBtn.setHint(String.format(new Locale("ar"), getCurrentDate()));
            endDateBtn.setHint(String.format(new Locale("ar"), getCurrentDate())
            );
        } else {
            startDateBtn.setHint(getCurrentDate());
            endDateBtn.setHint(getCurrentDate());
        }
    }

    private void setListeners() {

        EditText.OnEditorActionListener notesEditTextListener = new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Boolean valid = validate(startDateBtn.getText().toString(),
                            endDateBtn.getText().toString(),
                            subjectEditText.getText().toString(),
                            notesEditText.getText().toString());
                    if (valid) {
                        if (!(checkDateAndTime(firstDate, secondDate))) {
                            whenView.setBackgroundResource(R.color.tomato);
                            toView.setBackgroundResource(R.color.tomato);
                        } else {
                            changeViewsToValid();
                            createEvent();
                        }
                    }
                }
                return true;
            }
        };
        notesEditText.setOnEditorActionListener(notesEditTextListener);

        startDateBtn.setOnClickListener(this);
        endDateBtn.setOnClickListener(this);
        subjectEditText.setOnClickListener(this);
        parentView.setOnTouchListener(new HideKeyboardOnTouch(activity));
        notesEditText.setOnClickListener(this);
        createEventBtn.setOnClickListener(this);
        cancelEventBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    private void showDatePicker(final Button button) {
        final Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final String buttonText = button.getText().toString();
        datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yearofYear, int monthOfYear, int dayOfMonth) {
                        daySelected = dayOfMonth;
                        monthSelected = monthOfYear;
                        yearSelected = yearofYear;
                        if (Util.getLocale(activity).equals("ar")) {
                            Log.i("tag", "onDateSet: year:" + year + "month" + monthOfYear + "day:" + dayOfMonth);
                            button.setText(String.format(new Locale("ar"), "%d\\%d\\%d", dayOfMonth, (monthOfYear + 1), year));
                        } else
                            button.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + yearofYear);
                        isDateDialogShowing = datePickerDialog.isShowing();
                        showTimePicker(button, buttonText);
                    }
                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
        isDateDialogShowing = datePickerDialog.isShowing();
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isDateDialogShowing = datePickerDialog.isShowing();
            }
        });
    }

    private void showTimePicker(final Button button, final String buttonText) {
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHours, int selectedMinute) {
                String status;
                int twelveHourFormat;
                calendar.set(Calendar.HOUR_OF_DAY, selectedHours);
                calendar.set(Calendar.MINUTE, selectedMinute);
                calendar.set(Calendar.SECOND, 0);
                if (selectedHours >= 12) {
                    if (Util.getLocale(activity).equals("ar"))
                        status = "م";
                    else
                        status = "PM";
                    twelveHourFormat = selectedHours - 12;
                    if (twelveHourFormat == 0)
                        twelveHourFormat = 12;
                } else {
                    if (Util.getLocale(activity).equals("ar"))
                        status = "ص";
                    else
                        status = "AM";
                    twelveHourFormat = selectedHours;
                }
                isTimeSet = true;
                final Calendar c = Calendar.getInstance();
                c.set(yearSelected, monthSelected, daySelected, selectedHours, selectedMinute);

                if (Util.getLocale(activity).equals("ar"))
                    button.append(" " + String.format(new Locale("ar"), "%02d:%02d", twelveHourFormat, selectedMinute) + " " + status);
                else
                    button.append(" " + String.format("%02d:%02d", twelveHourFormat, selectedMinute) + " " + status);
                if (button.getId() == R.id.event_start_date_btn) {
                    firstDate = c.getTime();
                    Log.d("dates", "onTimeSet: " + firstDate);
                } else
                    secondDate = c.getTime();
            }
        }, hour, minute, false);
        timePickerDialog.show();
        timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!isTimeSet) {
                    button.setText(buttonText);
                }
            }
        });
        isTimeSet = false;
    }

    private boolean validate(String startDate, String endDate, String subject, String notes) {
        boolean valid = true;
        if (startDate.isEmpty()) {
            whenView.setBackgroundResource(R.color.tomato);
            valid = false;
        }
        if (endDate.isEmpty()) {
            toView.setBackgroundResource(R.color.tomato);
            valid = false;
        }
        if (subject.isEmpty()) {
            subjectView.setBackgroundResource(R.color.tomato);
            valid = false;
        }
        if (notes.isEmpty()) {
            notesView.setBackgroundResource(R.color.tomato);
            valid = false;
        }
        return valid;
    }

    private void changeViewsToValid() {
        whenView.setBackgroundResource(R.color.silver_75);
        toView.setBackgroundResource(R.color.silver_75);
        subjectView.setBackgroundResource(R.color.silver_75);
        notesView.setBackgroundResource(R.color.silver_75);
    }

    private boolean checkDateAndTime(Date first, Date second) {
        boolean valid = false;
        if (first.before(second)) {
            valid = true;
        } else if (first.after(second)) {
            valid = false;
        }
        return valid;
    }

    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(activity)
                    .load(imageUrl)
                    .fit()
                    .noPlaceholder()
                    .transform(new CircleTransform())
                    .into(studentImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageLoader = new PicassoLoader();
                            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }

    private void createEvent() {
        if (Util.isNetworkAvailable(activity)) {
            activity.showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.createEvent();
            createPersonalEventView.createEvent(url, firstDate, secondDate, "personal", "false", subjectEditText.getText().toString(), "User", student.userId, notesEditText.getText().toString(), "false");
        }
    }

    public static String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        df.setTimeZone(TimeZone.getDefault());
        Date dateObject = Calendar.getInstance().getTime();
        String date = df.format(dateObject);
        return date;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_start_date_btn:
                if (!isDateDialogShowing) {
                    showDatePicker(startDateBtn);
                }
                break;
            case R.id.event_end_date_btn:
                if (!isDateDialogShowing) {
                    showDatePicker(endDateBtn);
                }
                break;
            case R.id.event_create_btn:
                Boolean valid = validate(startDateBtn.getText().toString(),
                        endDateBtn.getText().toString(),
                        subjectEditText.getText().toString(),
                        notesEditText.getText().toString());
                if (valid) {
                    Log.i("TAG", "onClick: " + firstDate + secondDate);
                    if (!(checkDateAndTime(firstDate, secondDate))) {
                        whenView.setBackgroundResource(R.color.tomato);
                        toView.setBackgroundResource(R.color.tomato);
                    } else {
                        changeViewsToValid();
                        createEvent();
                    }
                }
                break;
            case R.id.event_cancel_btn:
                activity.getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_back:
                activity.getSupportFragmentManager().popBackStack();
                break;
        }

    }

    @Override
    public void onCreateEventSuccess() {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        if (fragmentCommunicationInterface != null) {
            fragmentCommunicationInterface.reloadEvents();
        }
        activity.getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCreateEventFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        if (errorCode == 401 || errorCode == 500) {
            activity.logoutUser(activity);
        } else {
            activity.showErrorDialog(activity);
        }
    }
}
