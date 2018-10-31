package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.ui.activities.adapters.NotificationsAdapter;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        bindViews();
    }
    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new NotificationsAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter.addData(getFakeData());

    }
    private ArrayList<String> getFakeData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("");

        }
        return list;
    }
}
