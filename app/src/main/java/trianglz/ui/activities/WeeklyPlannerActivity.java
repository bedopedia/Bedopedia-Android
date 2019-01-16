package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.skolera.skolera_android.R;

public class WeeklyPlannerActivity extends AppCompatActivity {
    private SmartTabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_planner);
    }
    private void bindViews () {
        tabLayout = findViewById(R.id.tab_layout);
    }
}
