package trianglz.ui.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.ErrorDialog;
import trianglz.core.presenters.AdapterPaginationInterface;
import trianglz.core.presenters.SchoolFeePresenter;
import trianglz.core.views.SchoolFeesView;
import trianglz.managers.SessionManager;
import trianglz.models.Meta;
import trianglz.models.SchoolFee;
import trianglz.ui.adapters.SchoolFeeAdapter;
import trianglz.utils.Util;

public class SchoolFeeActivity extends SuperActivity implements SchoolFeePresenter, AdapterPaginationInterface,
        View.OnClickListener, ErrorDialog.DialogConfirmationInterface {
    private RecyclerView recyclerView;
    private SchoolFeeAdapter adapter;
    private SchoolFeesView schoolFeesView;
    private LinearLayout skeletonLayout;
    private ShimmerFrameLayout shimmer;
    private SwipeRefreshLayout pullRefreshLayout;
    private ImageButton backBtn;

    private int nextPage = -1;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_school_fee);
        initViews();
        setListeners();
        if(Util.isNetworkAvailable(this)){
            getSchoolFees();
        }else {
            Util.showNoInternetConnectionDialog(this);
        }

    }

    private void initViews(){
        schoolFeesView = new SchoolFeesView(this, this);
        url = SessionManager.getInstance().getBaseUrl() + "/api/school_fees" ;

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new SchoolFeeAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        backBtn = findViewById(R.id.btn_back);
        skeletonLayout = findViewById(R.id.skeletonLayout);
        shimmer = findViewById(R.id.shimmer_view_container);
        pullRefreshLayout = findViewById(R.id.pullToRefresh);
    }
    private void getSchoolFees() {
        if (Util.isNetworkAvailable(this)) {
            if(nextPage == -1){
                 showSkeleton(true);
                showRecyclerView(true);
                schoolFeesView.getSchoolFees(url, 1,5);
            }else {
                if(nextPage != 0) {
                    schoolFeesView.getSchoolFees(url, nextPage, 5);
                }
            }
        } else {
            Util.showNoInternetConnectionDialog(this);
        }
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullRefreshLayout.setRefreshing(false);
                nextPage = -1;
                getSchoolFees();
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void showRecyclerView(boolean isToShowRecyclerView){
        if(isToShowRecyclerView){
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.GONE);
            showEmptyListDialog();

        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public void onReachPosition() {
        getSchoolFees();
    }

    @Override
    public void onGetSchoolFeesSuccess(ArrayList<SchoolFee> schoolFees, Meta meta) {
        showSkeleton(false);
        if (meta.getTotalCount() == 0) {
            showRecyclerView(false);
        } else {
            showRecyclerView(true);
            nextPage =  meta.getNextPage();
           // getSchoolFees();
            if(meta.getCurrentPage() == 1){
                adapter.schoolFeeArrayList.clear();
                adapter.totalCount = meta.getTotalCount();
            }
            adapter.addData(schoolFees);
        }
    }

    @Override
    public void onGetSchoolFeesFailure(String message, int errorCode) {
        showSkeleton(false);
        showErrorDialog(this, errorCode,"");
        if (nextPage == -1) {
            showRecyclerView(false);
        }
    }

    public void showSkeleton(boolean show) {
        if (show) {
            skeletonLayout.removeAllViews();

            int skeletonRows = Util.getSkeletonRowCount(this);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup)getLayoutInflater().inflate(R.layout.skeleton_row_layout, null);
                skeletonLayout.addView(rowLayout);
            }
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmer();
            shimmer.showShimmer(true);
            skeletonLayout.setVisibility(View.VISIBLE);
            skeletonLayout.bringToFront();
        } else {
            shimmer.stopShimmer();
            shimmer.hideShimmer();
            shimmer.setVisibility(View.GONE);
        }
    }


    private void showEmptyListDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View dialogLayout = layoutInflaterAndroid.inflate(R.layout.cancel_dialog, null);
        builder.setView(dialogLayout);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 40);
        alertDialog.getWindow().setBackgroundDrawable(inset);
        dialogLayout.findViewById(R.id.submit_btn).setOnClickListener(v1 ->{
            alertDialog.dismiss();
            finish();
        });
    }

}