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

import trianglz.models.AnswersAttributes;
import trianglz.models.Questions;
import trianglz.utils.Constants;

public class SingleMultiSelectAnswerAdapter extends RecyclerView.Adapter {

    public Questions question;

    private Context context;
    private TYPE type;

    private int selectedPosition = -1;
    private HashMap<Integer, Integer> multiSelectHashMap;
    private boolean onBind;
    public static final int TYPE_QUESTION = 0;
    public static final int TYPE_ANSWER_TEXT = 1;
    public static final int TYPE_QUESTION_ANSWER = 2;
    private int mode;

    public SingleMultiSelectAnswerAdapter(Context context, TYPE type, int mode) {

        this.context = context;
        question = new Questions();
        multiSelectHashMap = new HashMap<>();
        this.type = type;
        this.mode = mode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_QUESTION) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
            return new QuestionViewHolder(view);
        } else if (viewType == TYPE_ANSWER_TEXT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_quiz_answer, parent, false);
            return new AnswerTextViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_single_multi_selection_answer, parent, false);
            return new QuestionAnswerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        onBind = true;
        AnswersAttributes answersAttributes;
        if (position == 0) {
            final QuestionViewHolder holder = (QuestionViewHolder) viewHolder;
            holder.questionTextView.setText(question.getBody());
        } else if (position != 1) {
            final QuestionAnswerViewHolder holder = (QuestionAnswerViewHolder) viewHolder;
            if (type.equals(TYPE.TRUE_OR_FALSE)) {
                answersAttributes = question.getAnswersAttributes().get(0);
                if (position - 2 == 0) {
                    holder.questionAnswerTextView.setText(context.getString(R.string.true_answer));
                } else {
                    holder.questionAnswerTextView.setText(context.getString(R.string.false_answer));
                }
            } else {
                answersAttributes = question.getAnswersAttributes().get(position - 2);
                holder.questionAnswerTextView.setText(answersAttributes.getBody());
            }
            if (mode != Constants.SOLVE_QUIZ) {
                holder.radioButton.setClickable(false);
                holder.radioButton.setFocusable(false);
                holder.radioButton.setChecked(false);
                holder.multiSelectionImageButton.setClickable(false);
                holder.multiSelectionImageButton.setFocusable(false);
                holder.matchAnswerEditText.setClickable(false);
                holder.matchAnswerEditText.setFocusable(false);
                holder.multiSelectionImageButton.setImageDrawable(null);
                holder.multiSelectionImageButton.setBackground(context.getDrawable(R.drawable.curved_cool_grey_stroke));
                if (mode == Constants.VIEW_ANSWERS) {
                    if (type.equals(TYPE.SINGLE_SELECTION)) {
                        if (answersAttributes.isCorrect() == true) {
                            holder.radioButton.setChecked(true);
                        }
                    } else if (type.equals(TYPE.MULTI_SELECTION)) {
                        if (answersAttributes.isCorrect() == true) {
                            holder.multiSelectionImageButton.setImageResource(R.drawable.ic_white_check);
                            holder.multiSelectionImageButton.setBackground(context.getDrawable(R.drawable.curved_check_box_background_student));
                        }
                    } else if (type.equals(TYPE.TRUE_OR_FALSE)) {
                        if (answersAttributes.isCorrect() == true) {
                            if (holder.questionAnswerTextView.getText().toString().toLowerCase().equals("true")) {
                                holder.radioButton.setChecked(true);
                            }
                        }
                    } else if (type.equals(TYPE.REORDER_ANSWERS)) {
                        ArrayList<AnswersAttributes> sortMatchAnswers = sortMatchAnswers(question.getAnswersAttributes());
                        holder.questionAnswerTextView.setText(sortMatchAnswers.get(position - 2).getBody());
                    } else if (type.equals(TYPE.MATCH_ANSWERS)) {
                        holder.matchAnswerEditText.setText(answersAttributes.getMatch());
                    }
                }
            } else {
                if (type.equals(TYPE.SINGLE_SELECTION) || type.equals(TYPE.TRUE_OR_FALSE)) {
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
            }
        }
        onBind = false;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_QUESTION;
        } else if (position == 1) {
            return TYPE_ANSWER_TEXT;
        } else {
            return TYPE_QUESTION_ANSWER;
        }
    }

    @Override
    public int getItemCount() {
        if (type.equals(TYPE.TRUE_OR_FALSE)) {
            return question.getAnswersAttributes().size() + 3;
        } else {
            return question.getAnswersAttributes().size() + 2;
        }
    }


    public void addData(Questions data) {
        this.question = data;
        notifyDataSetChanged();
    }

    public enum TYPE {
        SINGLE_SELECTION,
        MULTI_SELECTION,
        REORDER_ANSWERS,
        MATCH_ANSWERS,
        TRUE_OR_FALSE
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView answerTextView;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            answerTextView = itemView.findViewById(R.id.answer_tv);
        }
    }

    class QuestionAnswerViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        TextView questionAnswerTextView;
        EditText matchAnswerEditText;
        RadioButton radioButton;
        ImageView sortImageView;
        ImageButton multiSelectionImageButton;

        QuestionAnswerViewHolder(View itemView) {
            super(itemView);
            questionAnswerTextView = itemView.findViewById(R.id.tv_answer);
            radioButton = itemView.findViewById(R.id.radio_button);
            sortImageView = itemView.findViewById(R.id.image_sort);
            matchAnswerEditText = itemView.findViewById(R.id.match_answer_et);
            radioButton.setOnClickListener(this);
            itemView.setOnClickListener(this);

            multiSelectionImageButton = itemView.findViewById(R.id.btn_multi_select);
            if (type == TYPE.MULTI_SELECTION) {
                multiSelectionImageButton.setVisibility(View.VISIBLE);
                radioButton.setVisibility(View.GONE);
                sortImageView.setVisibility(View.GONE);
                matchAnswerEditText.setVisibility(View.GONE);
            } else if (type == TYPE.SINGLE_SELECTION || type == TYPE.TRUE_OR_FALSE) {
                radioButton.setVisibility(View.VISIBLE);
                multiSelectionImageButton.setVisibility(View.GONE);
                sortImageView.setVisibility(View.GONE);
                matchAnswerEditText.setVisibility(View.GONE);
            } else if (type == TYPE.REORDER_ANSWERS) {
                sortImageView.setVisibility(View.VISIBLE);
                radioButton.setVisibility(View.GONE);
                multiSelectionImageButton.setVisibility(View.GONE);
                matchAnswerEditText.setVisibility(View.GONE);
            } else if (type == TYPE.MATCH_ANSWERS) {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.transparent, null));
                questionAnswerTextView.setPadding(22, 0, 0, 0);
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
                        multiSelectHashMap.put(getAdapterPosition(), question.getAnswersAttributes().get(getAdapterPosition() - 2).getId());
                    }
                    notifyDataSetChanged();
                } else if (type.equals(TYPE.SINGLE_SELECTION) || type.equals(TYPE.TRUE_OR_FALSE)) {
                    selectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                }
            }
        }
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        private TextView questionTextView;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.tv_question);
        }
    }

    public class AnswerTextViewHolder extends RecyclerView.ViewHolder {
        public AnswerTextViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private ArrayList<AnswersAttributes> sortMatchAnswers(ArrayList<AnswersAttributes> answersAttributes) {
        ArrayList<AnswersAttributes> sortedAnswersAttributes = new ArrayList<>(answersAttributes);
        for (int i = 0; i < answersAttributes.size(); i++) {
            for (int k = 0; k < answersAttributes.size(); k++) {
                if (Integer.parseInt(answersAttributes.get(i).getMatch()) == k + 1) {
                    sortedAnswersAttributes.set(k, answersAttributes.get(i));
                }
            }
        }
        return sortedAnswersAttributes;
    }
}
