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
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.Collections;

import trianglz.components.CustomeLayoutManager;
import trianglz.components.TopItemDecoration;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.SingleMultiSelectAnswerAdapter;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class SolveQuizFragment extends Fragment {
    private StudentMainActivity activity;
    private View rootView;

    private TextView subjectNameTextView;
    private TextView counterTextView;
    private ImageButton backButton;
    private ItemTouchHelper itemTouchHelper;
    private RecyclerView recyclerView;
    private SingleMultiSelectAnswerAdapter singleMultiSelectAnswerAdapter;
    private TextView questionTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
//        activity.toolbarView.setVisibility(View.GONE);
//        activity.headerLayout.setVisibility(View.GONE);
        rootView = inflater.inflate(R.layout.activity_solve_quiz, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindViews();
   //     onBackPress();
    }

    private void bindViews() {
        subjectNameTextView = rootView.findViewById(R.id.tv_subject_name);
        counterTextView = rootView.findViewById(R.id.tv_counter);
        questionTextView = rootView.findViewById(R.id.tv_question);
        recyclerView = rootView.findViewById(R.id.rv_answers);
        singleMultiSelectAnswerAdapter = new SingleMultiSelectAnswerAdapter(activity, SingleMultiSelectAnswerAdapter.TYPE.MULTI_SELECTION);
        recyclerView.setAdapter(singleMultiSelectAnswerAdapter);
        CustomeLayoutManager customeLayoutManager = new CustomeLayoutManager(activity);
        customeLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(customeLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));
        singleMultiSelectAnswerAdapter.addData(getFakeData());

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
}
