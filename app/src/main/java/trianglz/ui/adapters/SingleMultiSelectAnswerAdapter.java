package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import com.skolera.skolera_android.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SingleMultiSelectAnswerAdapter extends RecyclerView.Adapter<SingleMultiSelectAnswerAdapter.SingleMultiSelectionViewHolder> {

    public List<String> mDataList;

    private Context context;
    private TYPE type;

    private int selectedPosition = -1;
    private HashMap<Integer,String> multiSelectHashMap;
    private boolean onBind;


    public SingleMultiSelectAnswerAdapter(Context context, TYPE type) {

        this.context = context;
        mDataList = new ArrayList<>();
        multiSelectHashMap = new HashMap<>();
        this.type = type;
    }

    @Override
    public SingleMultiSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_single_multi_selection_answer, parent, false);
        return new SingleMultiSelectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleMultiSelectionViewHolder holder, int position) {
        onBind = true;
        holder.answerTextView.setText(mDataList.get(position));
        if(type.equals(TYPE.SINGLE_SELECTION)){
            if(holder.getAdapterPosition() == selectedPosition){
                holder.radioButton.setChecked(true);
            }else {
                holder.radioButton.setChecked(false);
            }
        }
        holder.multiSelectionImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(multiSelectHashMap.containsKey(holder.getAdapterPosition())){
                    multiSelectHashMap.remove(holder.getAdapterPosition());
                    holder.multiSelectionImageButton.setImageResource(R.drawable.met_ic_clear);
                }else {
                    multiSelectHashMap.put(holder.getAdapterPosition(),mDataList.get(position));
                    holder.multiSelectionImageButton.setImageResource(R.drawable.absent_2);
                }
            }
        });

        onBind = false;
    }



    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public void addData(ArrayList<String> data) {
        mDataList.clear();
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    public enum TYPE {
        SINGLE_SELECTION,
        MULTI_SELECTION
    }

     class SingleMultiSelectionViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        TextView answerTextView;
        AppCompatRadioButton radioButton;
        ImageButton multiSelectionImageButton;

        SingleMultiSelectionViewHolder(View itemView) {
            super(itemView);
            answerTextView = itemView.findViewById(R.id.tv_answer);
            radioButton = itemView.findViewById(R.id.radio_button);
            radioButton.setOnCheckedChangeListener(this);

            multiSelectionImageButton = itemView.findViewById(R.id.btn_multi_select);
            if(type == TYPE.MULTI_SELECTION){
                radioButton.setVisibility(View.GONE);
                multiSelectionImageButton.setVisibility(View.VISIBLE);
            }else {
                radioButton.setVisibility(View.VISIBLE);
                multiSelectionImageButton.setVisibility(View.GONE);
            }
        }

         @Override
         public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
             if(!onBind){
                 selectedPosition = getAdapterPosition();
                 notifyDataSetChanged();
             }
         }
     }



}
