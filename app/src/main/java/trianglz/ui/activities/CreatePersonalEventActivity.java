package trianglz.ui.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
import trianglz.core.views.CreatePersonalEventView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Student;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class CreatePersonalEventActivity extends SuperActivity implements View.OnClickListener, CreatePersonalEventPresenter {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_personal_event);
        getValueFromIntent();
        bindViews();
        // checkUserType();
        setListeners();
        studentName = student.firstName + " " + student.lastName;
        setStudentImage(student.getAvatar(), studentName);
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
                onBackPressed();
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    private void bindViews() {
        parentView = findViewById(R.id.root_view);
        backBtn = findViewById(R.id.btn_back);
        subjectEditText = findViewById(R.id.event_subject_edit_text);
        studentImageView = findViewById(R.id.img_student);
        notesEditText = findViewById(R.id.event_notes_edit_text);
        startDateBtn = findViewById(R.id.event_start_date_btn);
        endDateBtn = findViewById(R.id.event_end_date_btn);
        createEventBtn = findViewById(R.id.event_create_btn);
        cancelEventBtn = findViewById(R.id.event_cancel_btn);
        whenView = findViewById(R.id.when_view);
        toView = findViewById(R.id.to_view);
        subjectView = findViewById(R.id.subject_view);
        notesView = findViewById(R.id.notes_view);
        createPersonalEventView = new CreatePersonalEventView(CreatePersonalEventActivity.this, this);
        startDateBtn.setHint(getCurrentDate());
        endDateBtn.setHint(getCurrentDate());
        if (Util.getLocale(CreatePersonalEventActivity.this).equals("ar")) {
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
        parentView.setOnTouchListener(new HideKeyboardOnTouch(this));
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
        datePickerDialog = new DatePickerDialog(CreatePersonalEventActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yearofYear, int monthOfYear, int dayOfMonth) {
                        daySelected = dayOfMonth;
                        monthSelected = monthOfYear;
                        yearSelected = yearofYear;
                        if (Util.getLocale(CreatePersonalEventActivity.this).equals("ar")) {
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
        timePickerDialog = new TimePickerDialog(CreatePersonalEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHours, int selectedMinute) {
                String status;
                int twelveHourFormat;
                calendar.set(Calendar.HOUR_OF_DAY, selectedHours);
                calendar.set(Calendar.MINUTE, selectedMinute);
                calendar.set(Calendar.SECOND, 0);
                if (selectedHours >= 12) {
                    if (Util.getLocale(CreatePersonalEventActivity.this).equals("ar"))
                        status = "م";
                    else
                        status = "PM";
                    twelveHourFormat = selectedHours - 12;
                    if (twelveHourFormat == 0)
                        twelveHourFormat = 12;
                } else {
                    if (Util.getLocale(CreatePersonalEventActivity.this).equals("ar"))
                        status = "ص";
                    else
                        status = "AM";
                    twelveHourFormat = selectedHours;
                }
                isTimeSet = true;
                final Calendar c = Calendar.getInstance();
                c.set(yearSelected, monthSelected, daySelected, selectedHours, selectedMinute);

                if (Util.getLocale(CreatePersonalEventActivity.this).equals("ar"))
                    button.append(" " + String.format(new Locale("ar"), "%02d:%02d", twelveHourFormat, selectedMinute) + " " + status);
                else
                    button.append(" " + String.format("%02d:%02d", twelveHourFormat, selectedMinute) + " " + status);
                if (button.getId() == R.id.event_start_date_btn) {
                    firstDate = c.getTime();
                    Log.d("dates", "onTimeSet: "+firstDate);
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

    private void getValueFromIntent() {
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
    }

    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(this)
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
        if (Util.isNetworkAvailable(this)) {
            showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.createEvent();
            createPersonalEventView.createEvent(url, firstDate, secondDate, "personal", "false", subjectEditText.getText().toString(), "User", student.userId, notesEditText.getText().toString(), "false");
        }
    }

    @Override
    public void onCreateEventSuccess() {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onCreateEventFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        showErrorDialog(this, errorCode,"");
    }

    public static String getCurrentDate() {

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        df.setTimeZone(TimeZone.getDefault());
        Date dateObject = Calendar.getInstance().getTime();
        String date = df.format(dateObject);
        return date;
    }

    private void checkUserType() {
        if (SessionManager.getInstance().getStudentAccount()) {

            createEventBtn.setBackground(ContextCompat.getDrawable(CreatePersonalEventActivity.this, R.drawable.curved_salmon_background_radius_8));
        } else if (SessionManager.getInstance().getUserType()) {

            createEventBtn.setBackground(ContextCompat.getDrawable(CreatePersonalEventActivity.this, R.drawable.curved_turquoise_blue_two_background_radius_8));
        }
    }


}

