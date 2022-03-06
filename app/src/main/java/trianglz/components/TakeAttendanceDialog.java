package trianglz.components;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.skolera.skolera_android.R;

import trianglz.utils.Constants;

/**
 * Created by Farah A. Moniem on 15/09/2019.
 */
public class TakeAttendanceDialog extends BottomSheetDialog implements View.OnClickListener, DialogInterface.OnShowListener {

    private Context context;
    private Button cancelButton;
    private TextView actionTextView;
    private Boolean isMultipleSelected;
    private TakeAttendanceDialogInterface takeAttendanceDialogInterface;
    private LinearLayout assignPresentLayout, assignAbsentLayout, assignLateLayout, removeAllStatusLayout;

    public TakeAttendanceDialog(@NonNull Context context, Boolean isMultipleSelected, TakeAttendanceDialogInterface takeAttendanceDialogInterface) {
        super(context, R.style.BottomSheetDialog);
        this.context = context;
        this.isMultipleSelected = isMultipleSelected;
        this.takeAttendanceDialogInterface = takeAttendanceDialogInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_take_attendance);
        bindViews();
        setListeners();
        getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setDimAmount((float) 0.4);

    //    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
      //  getWindow().setDimAmount((float) 0.5);
    }

    private void bindViews() {
        assignPresentLayout = findViewById(R.id.assign_present_layout);
        assignAbsentLayout = findViewById(R.id.assign_absent_layout);
        assignLateLayout = findViewById(R.id.assign_late_layout);
        removeAllStatusLayout = findViewById(R.id.remove_all_status);
        cancelButton = findViewById(R.id.cancel_attendance_dialog_btn);
        actionTextView = findViewById(R.id.dialog_action_tv);
        if (isMultipleSelected) {
            actionTextView.setText(context.getResources().getString(R.string.assign_action_for_selected));
        } else {
            actionTextView.setText(context.getResources().getString(R.string.assign_action_for_all));
        }
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
        CoordinatorLayout layout = (CoordinatorLayout) bottomSheet.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(bottomSheet.getHeight());
        layout.getParent().requestLayout();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.assign_present_layout:
                takeAttendanceDialogInterface.onStatusSelected(Constants.TYPE_PRESENT);
                dismiss();
                break;
            case R.id.assign_absent_layout:
                takeAttendanceDialogInterface.onStatusSelected(Constants.TYPE_ABSENT);
                dismiss();
                break;
            case R.id.assign_late_layout:
                takeAttendanceDialogInterface.onStatusSelected(Constants.TYPE_LATE);
                dismiss();
                break;
            case R.id.remove_all_status:
                takeAttendanceDialogInterface.onStatusSelected(Constants.DELETE_ALL);
                dismiss();
            case R.id.cancel_attendance_dialog_btn:
                dismiss();
                break;

        }
    }

    public interface TakeAttendanceDialogInterface {
        void onStatusSelected(String status);
    }
}
