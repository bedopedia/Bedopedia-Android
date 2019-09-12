package trianglz.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.ContactTeacherPresenter;
import trianglz.core.views.ContactTeacherView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Actor;
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
        ContactTeacherPresenter, ContactTeacherAdapter.ContactTeacherAdapterInterface,StudentMainActivity.OnBackPressedInterface {

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
        if (SessionManager.getInstance().getUserType()) {
            student = activity.getStudent();
        } else {
            actor = activity.getActor();
        }
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

    }

    private void setListeners() {
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Util.isNetworkAvailable(getActivity())) {
            if (!activity.isCalling)
                activity.showLoadingDialog();
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
        this.messageThreadArrayList = messageThreadArrayList;
        contactTeacherAdapter.addData(messageThreadArrayList);
        if (!activity.isCalling)
            activity.progress.dismiss();
    }

    @Override
    public void onGetMessagesFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            if (!activity.isCalling)
                activity.progress.dismiss();
        }
        activity.showErrorDialog(activity, errorCode,"");

    }

    @Override
    public void onThreadClicked(int position) {
        if (!isOpeningThread) {
            if (Util.isNetworkAvailable(getActivity())) {
                isOpeningThread = true;
                MessageThread messageThread = contactTeacherAdapter.mDataList.get(position);
                openChatActivity(messageThread);
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
        NewMessageFragment newMessageFragment= new NewMessageFragment();
        Bundle bundle = new Bundle();
        if (student != null)
            bundle.putString(Constants.STUDENT, student.toString());
        newMessageFragment.setArguments(bundle);
        getChildFragmentManager().
                beginTransaction().add(R.id.messages_root, newMessageFragment, "MessagesFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
//        Intent intent = new Intent(getActivity(), NewMessageActivity.class);
//        if (student != null) intent.putExtra(Constants.STUDENT, student.toString());
//        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        isOpeningThread = false;
    }

    @Override
    public void onBackPressed() {
        Boolean isStudent = SessionManager.getInstance().getStudentAccount();
        Boolean isParent = SessionManager.getInstance().getUserType() && !isStudent;
        if (isStudent) {
            if (activity.pager.getCurrentItem() == 1) {
                getChildFragmentManager().popBackStack();
                if (getChildFragmentManager().getFragments().size() == 1) {
                    activity.toolbarView.setVisibility(View.VISIBLE);
                    activity.headerLayout.setVisibility(View.VISIBLE);
                }
            }
        } else if (isParent) {
            if (activity.pager.getCurrentItem() == 1) {
                if(getChildFragmentManager().getFragments().size()==0){
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
}
