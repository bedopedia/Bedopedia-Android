package trianglz.ui.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.Student;
import trianglz.utils.Constants;

public class CreatePersonalEventActivity extends AppCompatActivity implements View.OnClickListener {
    private Student student;
    private IImageLoader imageLoader;
    private AvatarView studentImageView;
    String firstDate, secondDate;
    private boolean isTimeSet = false, isDateSet = false;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private ImageButton backBtn;
    private EditText subjectEditText, notesEditText;
    private Button startDateBtn, endDateBtn, createEventBtn, cancelEventBtn;
    private View whenView, toView, subjectView, notesView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_personal_event);
        getValueFromIntent();
        bindViews();
        setListeners();
        String studentName = student.firstName + " " + student.lastName;
        setStudentImage(student.getAvatar(), studentName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_start_date_btn:
                showDatePicker(startDateBtn);
                firstDate = startDateBtn.getText().toString();
                break;
            case R.id.event_end_date_btn:
                showDatePicker(endDateBtn);
                secondDate = endDateBtn.getText().toString();
                break;
            case R.id.event_create_btn:
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
    }

    private void setListeners() {
        startDateBtn.setOnClickListener(this);
        endDateBtn.setOnClickListener(this);
        subjectEditText.setOnClickListener(this);
        notesEditText.setOnClickListener(this);
        createEventBtn.setOnClickListener(this);
        cancelEventBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    private void showDatePicker(final Button button) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        final String buttonText = button.getText().toString();
        datePickerDialog = new DatePickerDialog(CreatePersonalEventActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        button.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        showTimePicker(button, buttonText);
                    }
                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
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
                    status = "PM";
                    twelveHourFormat = selectedHours - 12;
                    if (twelveHourFormat == 0)
                        twelveHourFormat = 12;
                } else {
                    status = "AM";
                    twelveHourFormat = selectedHours;
                }
                isTimeSet = true;
                button.append(" " + String.format("%02d:%02d", twelveHourFormat, selectedMinute) + " " + status);
            }
        }, hour, minute, false);
        timePickerDialog.show();
        timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!isTimeSet)
                    button.setText(buttonText);
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

    private boolean timeValidator(int h1, int m1, int h2, int m2) {
        if (h1 > h1)
            return false;
        if (h1 >= h1 && h1 > h2)
            return false;
        return true;
    }

    private boolean checkDateAndTime(String first, String second) {
        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

        String firstArray[] = second.split(" ");
        String secondArray[] = second.split(" ");
        String d1 = firstArray[0], d2 = secondArray[0];
        Log.i("", "checkDateAndTime: "+firstArray);
        boolean valid = false;
        try {
//            Date start = sdf.parse(firstArray[1]);
//            Date end = sdf.parse(secondArray[1]);
            if (dfDate.parse(d1).before(dfDate.parse(d2))) {
                valid = true;
            } else if (dfDate.parse(d1).equals(dfDate.parse(d2))) {
               // if (end.before(start)) {
               //     valid = false;
                // else
                    valid = true;
            } else {
                valid = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
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
}

