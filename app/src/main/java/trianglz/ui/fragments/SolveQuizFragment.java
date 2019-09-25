package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import trianglz.components.CustomeLayoutManager;
import trianglz.components.TopItemDecoration;
import trianglz.models.Quizzes;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.SingleMultiSelectAnswerAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class SolveQuizFragment extends Fragment implements View.OnClickListener {
    private StudentMainActivity activity;
    private View rootView;

    private TextView subjectNameTextView;
    private TextView counterTextView;
    private ImageButton backButton;
    private Button previousButton, nextButton;
    private ItemTouchHelper itemTouchHelper;
    private RecyclerView recyclerView;
    private SingleMultiSelectAnswerAdapter singleMultiSelectAnswerAdapter;
    private TextView questionTextView;
    private Quizzes quizzes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_solve_quiz, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    private void bindViews() {
        subjectNameTextView = rootView.findViewById(R.id.tv_subject_name);
        counterTextView = rootView.findViewById(R.id.tv_counter);
        questionTextView = rootView.findViewById(R.id.tv_question);
        recyclerView = rootView.findViewById(R.id.rv_answers);
        backButton = rootView.findViewById(R.id.back_btn);
        previousButton = rootView.findViewById(R.id.btn_previous);
        nextButton = rootView.findViewById(R.id.btn_next);
        counterTextView.setText(secondsConverter(quizzes.getDuration()));
        subjectNameTextView.setText(quizzes.getName());
        singleMultiSelectAnswerAdapter = new SingleMultiSelectAnswerAdapter(activity, SingleMultiSelectAnswerAdapter.TYPE.MULTI_SELECTION);
        recyclerView.setAdapter(singleMultiSelectAnswerAdapter);
        CustomeLayoutManager customeLayoutManager = new CustomeLayoutManager(activity);
        customeLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(customeLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));
        singleMultiSelectAnswerAdapter.addData(getFakeData());

    }

    private void setListeners() {
        backButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            quizzes = Quizzes.create(bundle.getString(Constants.KEY_QUIZZES));
        }
    }

    private ArrayList<String> getFakeData() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            stringArrayList.add("Test " + i);
        }
        return stringArrayList;
    }

    private void setItemTouchHelper() {

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder dragged, RecyclerView.ViewHolder target) {

                int draggedPosition = dragged.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                Collections.swap(singleMultiSelectAnswerAdapter.mDataList, draggedPosition, targetPosition);
                singleMultiSelectAnswerAdapter.notifyItemMoved(draggedPosition, targetPosition);
                for (int i = 0; i < singleMultiSelectAnswerAdapter.mDataList.size(); i++) {
                    //   Log.d("list", "onMove: " +singleMultiSelectAnswerAdapter.mDataList.get(i));
                }
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private String secondsConverter(int time) {
        int seconds = time % 60;
        int hours = time / 60;
        int minutes = hours % 60;
        hours = hours / 60;
        if (hours == 0) {
            return String.format(Locale.ENGLISH, "%02d", minutes) + ":" + String.format(Locale.ENGLISH, "%02d", seconds);
        } else {
            return String.format(Locale.ENGLISH, "%02d", hours) + ":" + String.format(Locale.ENGLISH, "%02d", minutes) + ":" + String.format(Locale.ENGLISH, "%02d", seconds);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.btn_previous:
                break;
            case R.id.btn_next:
                break;
        }
    }
}
