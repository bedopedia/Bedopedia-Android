package trianglz.components;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.skolera.skolera_android.R;

import trianglz.ui.activities.StudentMainActivity;

/**
 * Created by Farah A. Moniem on 30/09/2019.
 */
public class QuizSubmittedDialog extends Dialog implements View.OnClickListener {
    private Button submitButton;
    private StudentMainActivity context;
    private Fragment fragment;


    public QuizSubmittedDialog(@NonNull StudentMainActivity context, Fragment fragment) {
        super(context, R.style.QuizDialog);
        View view = getLayoutInflater().inflate(R.layout.layout_quiz_submitted_dialog, null);
        setContentView(view);
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();
        setListeners();
    }

    private void setListeners() {
        submitButton.setOnClickListener(this);
    }

    private void bindViews() {
        submitButton = findViewById(R.id.submit_btn);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn) {
            for (int i = 0; i < fragment.getParentFragment().getChildFragmentManager().getFragments().size(); i++)
                fragment.getParentFragment().getChildFragmentManager().popBackStack();
        }
        context.toolbarView.setVisibility(View.VISIBLE);
        context.headerLayout.setVisibility(View.VISIBLE);
        dismiss();
    }
}
