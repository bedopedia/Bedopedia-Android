package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.skolera.skolera_android.R;

public class TeacherAttendanceActivity extends SuperActivity implements View.OnClickListener {
    private Button fullDayButton, perSlotButton;
    private View fullDayView, perSlotView;
    private ImageButton backButton;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendance);
        bindViews();
        setListeners();
    }

    private void bindViews() {
        fullDayButton = findViewById(R.id.full_day_btn);
        perSlotButton = findViewById(R.id.per_slot_btn);
        fullDayView = findViewById(R.id.full_day_view);
        perSlotView = findViewById(R.id.per_slot_view);
        recyclerView = findViewById(R.id.recycler_view);
        backButton = findViewById(R.id.btn_back);
    }

    private void setListeners() {
        fullDayButton.setOnClickListener(this);
        perSlotButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    private void showFullDayAttendance() {
        fullDayView.setVisibility(View.VISIBLE);
        perSlotView.setVisibility(View.INVISIBLE);
        fullDayButton.setTextColor(getResources().getColor(R.color.cerulean_blue,null));
        perSlotButton.setTextColor(getResources().getColor(R.color.greyish,null));

    }

    private void showPerSlotAttendance() {
        perSlotView.setVisibility(View.VISIBLE);
        fullDayView.setVisibility(View.INVISIBLE);
        perSlotButton.setTextColor(getResources().getColor(R.color.cerulean_blue,null));
        fullDayButton.setTextColor(getResources().getColor(R.color.greyish,null));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.full_day_btn:
                showFullDayAttendance();
                break;
            case R.id.per_slot_btn:
                showPerSlotAttendance();
                break;
        }
    }
}
