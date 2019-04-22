package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.List;

import trianglz.core.presenters.AdapterPaginationInterface;
import trianglz.models.PendingAssignment;

/**
 * Created by ${Aly} on 4/22/2019.
 */
public class PendingAssignmentAdapter extends RecyclerView.Adapter<GradesAdapter.Holder> {
    public Context context;
    public List<PendingAssignment> mDataList;
    private AdapterPaginationInterface paginationInterface;

    private boolean newData;



    public PendingAssignmentAdapter(Context context, AdapterPaginationInterface adapterPaginationInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.paginationInterface = adapterPaginationInterface;
    }

    @Override
    public GradesAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_assigment_detail, parent, false);
        return new GradesAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(GradesAdapter.Holder holder, final int position) {
        if (position == mDataList.size() - 2 && newData) {
            paginationInterface.onReachPosition();
        }


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(ArrayList<PendingAssignment> pendingAssignmentArrayList, boolean newIncomingPendingAssigmentData) {
        newData = newIncomingPendingAssigmentData;
        if (newData) {
            this.mDataList.addAll(pendingAssignmentArrayList);
            notifyDataSetChanged();
        }
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView subjectNameTextView, dateTextView,
                assignmentNameTextView,dayTextView,monthTextView;

        public Holder(View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.tv_subject_name);
            dateTextView = itemView.findViewById(R.id.tv_date);
            assignmentNameTextView = itemView.findViewById(R.id.img_subject);
            assignmentNameTextView = itemView.findViewById(R.id.tv_assignment_name);
            dayTextView = itemView.findViewById(R.id.tv_day_number);
            monthTextView = itemView.findViewById(R.id.tv_month);
        }
    }

}