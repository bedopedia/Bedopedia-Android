package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.CustomeLayoutManager;
import trianglz.components.TopItemDecoration;
import trianglz.ui.adapters.SingleMultiSelectAnswerAdapter;
import trianglz.utils.Util;

public class SolveQuizActivity extends AppCompatActivity {

    private TextView subjectNameTextView;
    private TextView counterTextView;
    private ImageButton backButton;
    private RecyclerView recyclerView;
    private SingleMultiSelectAnswerAdapter singleMultiSelectAnswerAdapter;
    private TextView questionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_quiz);
        bindViews();
    }

    private void bindViews(){
        subjectNameTextView = findViewById(R.id.tv_subject_name);
        counterTextView = findViewById(R.id.tv_counter);
        questionTextView = findViewById(R.id.tv_question);
        recyclerView = findViewById(R.id.rv_answers);
        singleMultiSelectAnswerAdapter = new SingleMultiSelectAnswerAdapter(this, SingleMultiSelectAnswerAdapter.TYPE.MULTI_SELECTION);
        recyclerView.setAdapter(singleMultiSelectAnswerAdapter);
        CustomeLayoutManager customeLayoutManager = new CustomeLayoutManager(this);
        customeLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(customeLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.addItemDecoration(new TopItemDecoration((int)Util.convertDpToPixel(8,this),false));
        singleMultiSelectAnswerAdapter.addData(getFakeData());

    }

    private ArrayList<String> getFakeData(){
        ArrayList<String> stringArrayList = new ArrayList<>();
        for(int i = 0; i< 15; i++){
            stringArrayList.add("Test "+i);
        }
        return stringArrayList;
    }
}
