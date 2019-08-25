package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
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
    private HashMap<Integer, String> multiSelectHashMap;
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
        holder.questionAnswerTextView.setText(mDataList.get(position));
        if (type.equals(TYPE.SINGLE_SELECTION)) {
            if (holder.getAdapterPosition() == selectedPosition) {
                holder.radioButton.setChecked(true);
            } else {
                holder.radioButton.setChecked(false);
            }
        } else if (type.equals(TYPE.MULTI_SELECTION)) {
            if (multiSelectHashMap.containsKey(holder.getAdapterPosition())) {
                holder.multiSelectionImageButton.setImageResource(R.drawable.ic_white_check);
                holder.multiSelectionImageButton.setBackground(context.getDrawable(R.drawable.curved_check_box_background_student));
            } else {
                holder.multiSelectionImageButton.setImageDrawable(null);
                holder.multiSelectionImageButton.setBackground(context.getDrawable(R.drawable.curved_cool_grey_stroke));
            }
        } else if (type.equals(TYPE.REORDER_ANSWERS)) {

        } else if (type.equals(TYPE.MATCH_ANSWERS)) {

        }

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
        MULTI_SELECTION,
        REORDER_ANSWERS,
        MATCH_ANSWERS
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView answerTextView;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            answerTextView = itemView.findViewById(R.id.answer_tv);
        }
    }

    class SingleMultiSelectionViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        TextView questionAnswerTextView, questionNumberTextView;
        EditText matchAnswerEditText;
        RadioButton radioButton;
        ImageView sortImageView;
        ImageButton multiSelectionImageButton;

        SingleMultiSelectionViewHolder(View itemView) {
            super(itemView);
            questionAnswerTextView = itemView.findViewById(R.id.tv_answer);
            radioButton = itemView.findViewById(R.id.radio_button);
            sortImageView = itemView.findViewById(R.id.image_sort);
            questionNumberTextView = itemView.findViewById(R.id.question_number_tv);
            matchAnswerEditText = itemView.findViewById(R.id.match_answer_et);
            radioButton.setOnClickListener(this);
            itemView.setOnClickListener(this);

            multiSelectionImageButton = itemView.findViewById(R.id.btn_multi_select);
            if (type == TYPE.MULTI_SELECTION) {
                multiSelectionImageButton.setVisibility(View.VISIBLE);
                radioButton.setVisibility(View.GONE);
                sortImageView.setVisibility(View.GONE);
                questionNumberTextView.setVisibility(View.GONE);
                matchAnswerEditText.setVisibility(View.GONE);
            } else if (type == TYPE.SINGLE_SELECTION) {
                radioButton.setVisibility(View.VISIBLE);
                multiSelectionImageButton.setVisibility(View.GONE);
                sortImageView.setVisibility(View.GONE);
                questionNumberTextView.setVisibility(View.GONE);
                matchAnswerEditText.setVisibility(View.GONE);
            } else if (type == TYPE.REORDER_ANSWERS) {
                sortImageView.setVisibility(View.VISIBLE);
                radioButton.setVisibility(View.GONE);
                multiSelectionImageButton.setVisibility(View.GONE);
                questionNumberTextView.setVisibility(View.GONE);
                matchAnswerEditText.setVisibility(View.GONE);
            } else if (type == TYPE.MATCH_ANSWERS) {
                questionNumberTextView.setVisibility(View.VISIBLE);
                matchAnswerEditText.setVisibility(View.VISIBLE);
                sortImageView.setVisibility(View.GONE);
                radioButton.setVisibility(View.GONE);
                multiSelectionImageButton.setVisibility(View.GONE);
            }
        }


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (!onBind) {
                selectedPosition = getAdapterPosition();
                notifyDataSetChanged();
            }
        }

        @Override
        public void onClick(View view) {
            if (!onBind) {
                if (type.equals(TYPE.MULTI_SELECTION)) {
                    if (multiSelectHashMap.containsKey(getAdapterPosition())) {
                        multiSelectHashMap.remove(getAdapterPosition());
                    } else {
                        multiSelectHashMap.put(getAdapterPosition(), mDataList.get(getAdapterPosition()));
                    }
                    notifyDataSetChanged();
                } else if (type.equals(TYPE.SINGLE_SELECTION)) {
                    selectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                }
            }
        }
    }

}
