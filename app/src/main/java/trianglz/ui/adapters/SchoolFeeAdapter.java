package trianglz.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.AdapterPaginationInterface;
import trianglz.models.SchoolFee;

public class SchoolFeeAdapter extends RecyclerView.Adapter<SchoolFeeAdapter.SchoolFeeViewHolder> {
    public ArrayList<SchoolFee> notificationArrayList;
    public int totalCount = -1;
    private Context context;
    private AdapterPaginationInterface paginationInterface;


    public SchoolFeeAdapter(Context context, AdapterPaginationInterface adapterPaginationInterface) {
        this.context = context;
        this.notificationArrayList = new ArrayList<>();
        paginationInterface = adapterPaginationInterface;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NonNull
    @Override
    public SchoolFeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_school_fee, parent, false);
        return new SchoolFeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolFeeViewHolder holder, int position) {
        if (position == notificationArrayList.size() - 4 && totalCount != -1 && notificationArrayList.size() <= totalCount) {
            paginationInterface.onReachPosition();
        }
        SchoolFee schoolFee = notificationArrayList.get(position);
        holder.dateTv.setText(schoolFee.getDue_date());
        holder.nameTv.setText(schoolFee.getName());
        holder.studentNameTv.setText(schoolFee.getStudent_name());
        holder.amountTv.setText(schoolFee.getAmount());

    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    public void addData(ArrayList<SchoolFee> notifications) {
        this.notificationArrayList.addAll(notifications);
        notifyDataSetChanged();
    }

    public class SchoolFeeViewHolder extends RecyclerView.ViewHolder {
        TextView dateTv, nameTv, studentNameTv, amountTv;

        public SchoolFeeViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name_tv);
            studentNameTv = itemView.findViewById(R.id.student_name_tv);
            amountTv = itemView.findViewById(R.id.amount_tv);
            dateTv = itemView.findViewById(R.id.date);
        }
    }
}
