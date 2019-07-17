package trianglz.ui.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import trianglz.components.CustomRtlViewPager;
import trianglz.ui.adapters.StudentMainPagerAdapter;
import trianglz.ui.fragments.AnnouncementsFragment;
import trianglz.ui.fragments.MenuFragment;
import trianglz.ui.fragments.MessagesFragment;
import trianglz.ui.fragments.NotificationsFragment;

public class StudentMainActivity extends SuperActivity implements View.OnClickListener {

    private LinearLayout announcementLayout, messagesLayout, notificationsLayout, menuLayout;
    private ImageView announcementImageView, messagesImageView, notificationsImageView, menuImageView;
    private TextView announcementTextView, messagesTextView, notificationsTextView, menuTextView;

    private CustomRtlViewPager pager;
    private StudentMainPagerAdapter pagerAdapter;

    // fragments
    private AnnouncementsFragment announcementsFragment;
    private MessagesFragment messagesFragment;
    private NotificationsFragment notificationsFragment;
    private MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        bindViews();
        setListeners();
    }

    private void setListeners() {
        announcementLayout.setOnClickListener(this);
        messagesLayout.setOnClickListener(this);
        notificationsLayout.setOnClickListener(this);
        menuLayout.setOnClickListener(this);
    }

    private void bindViews() {
        // tabs
        announcementLayout = findViewById(R.id.ll_announcment_tab);
        messagesLayout = findViewById(R.id.ll_messages_tab);
        notificationsLayout = findViewById(R.id.ll_notifications_tab);
        menuLayout = findViewById(R.id.ll_menu_tab);

        // image views
        announcementImageView = findViewById(R.id.iv_announcements);
        messagesImageView = findViewById(R.id.iv_messages);
        notificationsImageView = findViewById (R.id.iv_notifcations);
        menuImageView = findViewById(R.id.iv_menu);

        // text views
        announcementTextView = findViewById(R.id.tv_announcements);
        messagesTextView = findViewById(R.id.tv_messages);
        notificationsTextView = findViewById(R.id.tv_notifications);
        menuTextView = findViewById (R.id.tv_menu);

        // init fragments
        announcementsFragment = new AnnouncementsFragment();
        messagesFragment = new MessagesFragment();
        notificationsFragment = new NotificationsFragment();
        menuFragment = new MenuFragment();

        // pager
        pager = findViewById(R.id.pager);
        pager.setPagingEnabled(false);
        pagerAdapter = new StudentMainPagerAdapter(getSupportFragmentManager(), getFragmentList());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(4);
        pager.setCurrentItem(4);
    }

    private ArrayList<Fragment> getFragmentList() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(announcementsFragment);
        fragmentArrayList.add(messagesFragment);
        fragmentArrayList.add(notificationsFragment);
        fragmentArrayList.add(menuFragment);
        return fragmentArrayList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_announcment_tab:
                pager.setCurrentItem(0,true);
                handleTabsClicking(1);
                break;
            case R.id.ll_messages_tab:
                pager.setCurrentItem(1,true);
                handleTabsClicking(2);
                break;
            case R.id.ll_notifications_tab:
                pager.setCurrentItem(2,true);
                handleTabsClicking(3);
                break;
            case R.id.ll_menu_tab:
                pager.setCurrentItem(3,true);
                handleTabsClicking(4);
                break;
        }
    }

    private void handleTabsClicking(int tabNumber) {
        switch (tabNumber) {
            case 1:
                announcementImageView.setImageResource(R.drawable.ic_announcment_selected);
                messagesImageView.setImageResource(R.drawable.ic_messages_tab);
                notificationsImageView.setImageResource(R.drawable.ic_notifications_tab);
                menuImageView.setImageResource(R.drawable.ic_menu_tab);

                announcementTextView.setVisibility(View.VISIBLE);
                messagesTextView.setVisibility(View.GONE);
                notificationsTextView.setVisibility(View.GONE);
                menuTextView.setVisibility(View.GONE);
                break;
            case 2:
                announcementImageView.setImageResource(R.drawable.ic_announcments);
                messagesImageView.setImageResource(R.drawable.ic_messages_selected);
                notificationsImageView.setImageResource(R.drawable.ic_notifications_tab);
                menuImageView.setImageResource(R.drawable.ic_menu_tab);

                announcementTextView.setVisibility(View.GONE);
                messagesTextView.setVisibility(View.VISIBLE);
                notificationsTextView.setVisibility(View.GONE);
                menuTextView.setVisibility(View.GONE);
                break;
            case 3:
                announcementImageView.setImageResource(R.drawable.ic_announcments);
                messagesImageView.setImageResource(R.drawable.ic_messages_tab);
                notificationsImageView.setImageResource(R.drawable.ic_notification_selected);
                menuImageView.setImageResource(R.drawable.ic_menu_tab);

                announcementTextView.setVisibility(View.GONE);
                messagesTextView.setVisibility(View.GONE);
                notificationsTextView.setVisibility(View.VISIBLE);
                menuTextView.setVisibility(View.GONE);
                break;
            case 4:
                announcementImageView.setImageResource(R.drawable.ic_announcments);
                messagesImageView.setImageResource(R.drawable.ic_messages_tab);
                notificationsImageView.setImageResource(R.drawable.ic_notifications_tab);
                menuImageView.setImageResource(R.drawable.menu_tab_selected);

                announcementTextView.setVisibility(View.GONE);
                messagesTextView.setVisibility(View.GONE);
                notificationsTextView.setVisibility(View.GONE);
                menuTextView.setVisibility(View.VISIBLE);
                break;
        }
    }
}
