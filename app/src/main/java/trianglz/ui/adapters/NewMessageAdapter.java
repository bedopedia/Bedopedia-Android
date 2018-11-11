package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.List;

import trianglz.models.Subject;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class NewMessageAdapter extends RecyclerView.Adapter<NewMessageAdapter.Holder> {
    public Context context;
    public ArrayList<Subject> mDataList;
    NewMessageAdapterInterface newMessageAdapterInterface;


    public NewMessageAdapter(Context context, NewMessageAdapterInterface newMessageAdapterInterface) {
        this.context = context;
        this.mDataList = new ArrayList<>();
        this.newMessageAdapterInterface = newMessageAdapterInterface;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_grade, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(ArrayList<Subject> courseGroupList) {
        this.mDataList.clear();
        this.mDataList.addAll(courseGroupList);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {



        public Holder(View itemView) {
            super(itemView);

        }
    }

    public interface NewMessageAdapterInterface {
        void onSubjectSelected(int position);
    }

}