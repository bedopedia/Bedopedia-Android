package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.skolera.skolera_android.R;

public class PostsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        bindViews();
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);

    }
}
