package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class KidsAdapter extends RecyclerView.Adapter<KidsAdapter.KidsViewHolder> {


    private List<String> mDataList;
    private Context context;


    public KidsAdapter(Context context) {
        this.context = context;
        this.mDataList = new ArrayList<>();

    }


    @Override
    public KidsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_kid_home, parent, false);
        return new KidsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final KidsViewHolder holder, final int position) {

    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(ArrayList<String> mDataList) {
        this.mDataList.clear();
        this.mDataList.addAll(mDataList);
        notifyDataSetChanged();
    }

    public class KidsViewHolder extends RecyclerView.ViewHolder {


        public KidsViewHolder(View itemView) {
            super(itemView);

        }
    }


}