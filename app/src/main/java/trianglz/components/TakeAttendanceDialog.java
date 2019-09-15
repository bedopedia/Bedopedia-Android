package trianglz.components;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

/**
 * Created by Farah A. Moniem on 15/09/2019.
 */
public class TakeAttendanceDialog extends BottomSheetDialog implements View.OnClickListener, DialogInterface.OnShowListener {

    Context context;
    Button cancelButton;
    TextView actionTextView;
    LinearLayout assignPresentLayout, assignAbsentLayout, assignLateLayout, removeAllStatusLayout;

    public TakeAttendanceDialog(@NonNull Context context) {
        super(context, R.style.AttendanceDialog);
        View view = getLayoutInflater().inflate(R.layout.layout_take_attendance, null);
        setContentView(view);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setOnShowListener(this);
        bindViews();
        setListeners();
        getWindow()
                .findViewById(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);
    }

    private void bindViews() {
        assignPresentLayout = findViewById(R.id.assign_present_layout);
        assignAbsentLayout = findViewById(R.id.assign_absent_layout);
        assignLateLayout = findViewById(R.id.assign_late_layout);
        removeAllStatusLayout = findViewById(R.id.remove_all_status);
        cancelButton = findViewById(R.id.cancel_attendance_dialog_btn);
        actionTextView = findViewById(R.id.dialog_action_tv);
    }

    private void setListeners() {
        assignPresentLayout.setOnClickListener(this);
        assignAbsentLayout.setOnClickListener(this);
        assignLateLayout.setOnClickListener(this);
        removeAllStatusLayout.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }


    @Override
    public void onShow(DialogInterface dialog) {
        BottomSheetDialog d = (BottomSheetDialog) dialog;
        FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
        CoordinatorLayout lyout = (CoordinatorLayout) bottomSheet.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(bottomSheet.getHeight());
        lyout.getParent().requestLayout();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.assign_present_layout:
                break;
            case R.id.assign_absent_layout:
                break;
            case R.id.assign_late_layout:
                break;
            case R.id.cancel_attendance_dialog_btn:
                dismiss();
                break;

        }
    }
}
