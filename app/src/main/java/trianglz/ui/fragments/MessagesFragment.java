package trianglz.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.ContactTeacherPresenter;
import trianglz.core.views.ContactTeacherView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Actor;
import trianglz.models.Message;
import trianglz.models.MessageThread;
import trianglz.models.Student;
import trianglz.ui.activities.ChatActivity;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.ContactTeacherAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment implements View.OnClickListener,
        ContactTeacherPresenter, ContactTeacherAdapter.ContactTeacherAdapterInterface, StudentMainActivity.OnBackPressedInterface {

    // root view 
    private View rootView;

    // parent activity 
    private StudentMainActivity activity;
    private Student student;
    private ContactTeacherView contactTeacherView;
    private RecyclerView recyclerView;
    private ContactTeacherAdapter contactTeacherAdapter;
    private ArrayList<MessageThread> messageThreadArrayList;
    private Actor actor;
    private boolean isOpeningThread = false;
    private SwipeRefreshLayout pullRefreshLayout;
    private LinearLayout placeholderLinearLayout;
    private LinearLayout skeletonLayout;
    private ShimmerFrameLayout shimmer;
    private LayoutInflater inflater;

    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        activity = (StudentMainActivity) getActivity();
        bindViews();
        setListeners();
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            student = activity.getStudent();
        } else {
            actor = activity.getActor();
        }
        getMessages();
        return rootView;
    }

    private void bindViews() {
        contactTeacherView = new ContactTeacherView(getActivity(), this);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        contactTeacherAdapter = new ContactTeacherAdapter(getActivity(), this);
        recyclerView.setAdapter(contactTeacherAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, getActivity()), false));
        messageThreadArrayList = new ArrayList<>();
        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
        placeholderLinearLayout = rootView.findViewById(R.id.placeholder_layout);
        skeletonLayout = rootView.findViewById(R.id.skeletonLayout);
        shimmer = rootView.findViewById(R.id.shimmer_view_container);
        this.inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void setListeners() {
        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessages();
                showSkeleton(true);
                pullRefreshLayout.setRefreshing(false);
                placeholderLinearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
//        getMessages();
    }

    private void getMessages() {
        if (Util.isNetworkAvailable(getActivity())) {
            showSkeleton(true);
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getThreads();
                contactTeacherView.getMessages(url, SessionManager.getInstance().getId());

        } else {
            Util.showNoInternetConnectionDialog(getActivity());
        }
        isOpeningThread = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                activity.onBackPressed();
                break;
        }
    }

    @Override
    public void onGetMessagesSuccess(ArrayList<MessageThread> messageThreadArrayList) {
        showSkeleton(false);
        if (messageThreadArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            placeholderLinearLayout.setVisibility(View.GONE);
            this.messageThreadArrayList = messageThreadArrayList;
            contactTeacherAdapter.addData(messageThreadArrayList);
        }

    }

    @Override
    public void onGetMessagesFailure(String message, int errorCode) {
        showSkeleton(false);
        activity.showErrorDialog(activity, errorCode, "");
        recyclerView.setVisibility(View.GONE);
        placeholderLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetSingleThreadSuccess(ArrayList<Message> messages,int position) {
        //todo call get messages
        MessageThread messageThread = contactTeacherAdapter.mDataList.get(position);
        messageThread.messageArrayList.clear();
        messageThread.messageArrayList.addAll(messages);
        openChatActivity(messageThread);
        isOpeningThread = false;
    }

    @Override
    public void onGetSingleThreadFailure(String message, int errorCode) {
        activity.showErrorDialog(activity, errorCode, "");
        isOpeningThread = false;
    }

    @Override
    public void onThreadClicked(int position) {
        if (!isOpeningThread) {
            if (Util.isNetworkAvailable(getActivity())) {
                isOpeningThread = true;
                MessageThread messageThread = contactTeacherAdapter.mDataList.get(position);
                String url = SessionManager.getInstance().getBaseUrl() +ApiEndPoints.getSingleThread(messageThread.id);
                contactTeacherView.getSingleThread(url,position);
            } else {
                Util.showNoInternetConnectionDialog(getActivity());
            }

        }


    }

    private void openChatActivity(MessageThread messageThread) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_MESSAGES, messageThread);
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivity(intent);
    }

    public void openNewMessageActivity() {
        NewMessageFragment newMessageFragment = new NewMessageFragment();
        Bundle bundle = new Bundle();
        if (student != null)
            bundle.putString(Constants.STUDENT, student.toString());
        newMessageFragment.setArguments(bundle);
        getChildFragmentManager().
                beginTransaction().add(R.id.messages_root, newMessageFragment, "MessagesFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        isOpeningThread = false;
    }

    @Override
    public void onBackPressed() {
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            if (activity.pager.getCurrentItem() == 1) {
                getChildFragmentManager().popBackStack();
                if (getChildFragmentManager().getFragments().size() == 1) {
                    activity.toolbarView.setVisibility(View.VISIBLE);
                    activity.headerLayout.setVisibility(View.VISIBLE);
                }
            }
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            if (activity.pager.getCurrentItem() == 1) {
                if (getChildFragmentManager().getFragments().size() == 0) {
                    getActivity().finish();
                    return;
                }
                getChildFragmentManager().popBackStack();
                if (getChildFragmentManager().getFragments().size() == 1) {
                    activity.toolbarView.setVisibility(View.VISIBLE);
                    activity.headerLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    public void showSkeleton(boolean show) {
        if (show) {
            skeletonLayout.removeAllViews();

            int skeletonRows = Util.getSkeletonRowCount(activity);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup) inflater
                        .inflate(R.layout.skeleton_row_layout, (ViewGroup) rootView, false);
                skeletonLayout.addView(rowLayout);
            }
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.GONE);
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmer();
            shimmer.showShimmer(true);
            skeletonLayout.setVisibility(View.VISIBLE);
            skeletonLayout.bringToFront();
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            placeholderLinearLayout.setVisibility(View.VISIBLE);
            shimmer.stopShimmer();
            shimmer.hideShimmer();
            shimmer.setVisibility(View.GONE);
        }
    }
}
