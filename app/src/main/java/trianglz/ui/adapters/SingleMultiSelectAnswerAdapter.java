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
import android.widget.LinearLayout;
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
    public TYPE type;

    public HashMap<Questions, Object> questionsObjectHashMap;
    public static final int TYPE_QUESTION = 0;
    public static final int TYPE_ANSWER_TEXT = 1;
    public static final int TYPE_QUESTION_ANSWER = 2;
    private static final int TYPE_TRUE_OR_FALSE = 3;

    private int mode;

    public SingleMultiSelectAnswerAdapter(Context context, int mode) {
        this.context = context;
        question = new Questions();
        questionsObjectHashMap = new HashMap<>();
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
        } else if (viewType == TYPE_TRUE_OR_FALSE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_true_or_false_answer, parent, false);
            return new TrueFalseAnswerViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_single_multi_selection_answer, parent, false);
            return new QuestionAnswerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        AnswersAttributes answersAttributes;

        if (position == 0) {
            final QuestionViewHolder holder = (QuestionViewHolder) viewHolder;
            holder.questionTextView.setText(question.getBody());
        } else if (position != 1 && !type.equals(TYPE.TRUE_OR_FALSE)) {
            answersAttributes = question.getAnswersAttributes().get(position - 2);
            final QuestionAnswerViewHolder holder = (QuestionAnswerViewHolder) viewHolder;
            holder.setViews();
            holder.questionAnswerTextView.setText(answersAttributes.getBody());
            if (mode == Constants.VIEW_ANSWERS) {
                if (type.equals(TYPE.SINGLE_SELECTION)) {
                    if (answersAttributes.isCorrect()) {
                        holder.radioButton.setChecked(true);
                    }
                } else if (type.equals(TYPE.MULTI_SELECTION)) {
                    if (answersAttributes.isCorrect()) {
                        holder.multiSelectionImageButton.setImageResource(R.drawable.ic_white_check);
                        holder.multiSelectionImageButton.setBackground(context.getDrawable(R.drawable.curved_check_box_background_student));
                    }
                } else if (type.equals(TYPE.REORDER_ANSWERS)) {
                    ArrayList<AnswersAttributes> sortMatchAnswers = sortMatchAnswers(question.getAnswersAttributes());
                    holder.questionAnswerTextView.setText(sortMatchAnswers.get(position - 2).getBody());
                } else if (type.equals(TYPE.MATCH_ANSWERS)) {
                    holder.matchAnswerEditText.setText(answersAttributes.getMatch());
                }
            } else {
                if (questionsObjectHashMap.containsKey(question)) {
                    if (questionsObjectHashMap.get(question) instanceof ArrayList) {
                        ArrayList<AnswersAttributes> answersAttributesArrayList = (ArrayList<AnswersAttributes>) questionsObjectHashMap.get(question);
                        for (int i = 0; i < answersAttributesArrayList.size(); i++) {
                            if (answersAttributesArrayList.get(i).getId() == question.getAnswersAttributes().get(position - 2).getId()) {
                                if (type.equals(TYPE.MULTI_SELECTION)) {
                                    holder.multiSelectionImageButton.setImageResource(R.drawable.ic_white_check);
                                    holder.multiSelectionImageButton.setBackground(context.getDrawable(R.drawable.curved_check_box_background_student));
                                } else if (type.equals(TYPE.SINGLE_SELECTION)) {
                                    holder.radioButton.setChecked(true);
                                } else if (type.equals(TYPE.MATCH_ANSWERS)) {

                                }
                                return;
                            }
                        }
                        if (type.equals(TYPE.MULTI_SELECTION)) {
                            holder.multiSelectionImageButton.setImageDrawable(null);
                            holder.multiSelectionImageButton.setBackground(context.getDrawable(R.drawable.curved_cool_grey_stroke));
                        } else if (type.equals(TYPE.SINGLE_SELECTION)) {
                            holder.radioButton.setChecked(false);
                        } else if (type.equals(TYPE.MATCH_ANSWERS)) {

                        }
                    } else if (questionsObjectHashMap.get(question) instanceof String) {
                        String state = (String) questionsObjectHashMap.get(question);
                        if (holder.questionAnswerTextView.getText().toString().toLowerCase().equals(state)) {
                            holder.radioButton.setChecked(true);
                        }
                    }
                } else {
                    if (type.equals(TYPE.MULTI_SELECTION)) {
                        holder.multiSelectionImageButton.setImageDrawable(null);
                        holder.multiSelectionImageButton.setBackground(context.getDrawable(R.drawable.curved_cool_grey_stroke));
                    } else if (type.equals(TYPE.SINGLE_SELECTION)) {
                        holder.radioButton.setChecked(false);
                    } else if (type.equals(TYPE.MATCH_ANSWERS)) {

                    }
                }

            }
        } else if (position != 1) {
            final TrueFalseAnswerViewHolder holder = (TrueFalseAnswerViewHolder) viewHolder;
            answersAttributes = question.getAnswersAttributes().get(position - 2);
            if (mode == Constants.VIEW_ANSWERS) {
                if (answersAttributes.isCorrect()) {
                    holder.trueRadioButton.setChecked(true);
                } else {
                    holder.falseRadioButton.setChecked(true);
                }
            } else if ((mode == Constants.SOLVE_QUIZ)) {
                if (questionsObjectHashMap.containsKey(question)) {
                    if (questionsObjectHashMap.get(question) instanceof Boolean) {
                        Boolean state = (Boolean) questionsObjectHashMap.get(question);
                        if (state) {
                            holder.trueRadioButton.setChecked(true);
                            holder.falseRadioButton.setChecked(false);
                        } else {
                            holder.trueRadioButton.setChecked(false);
                            holder.falseRadioButton.setChecked(true);
                        }
                    }
                }
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_QUESTION;
        } else if (position == 1) {
            return TYPE_ANSWER_TEXT;
        } else if (type == TYPE.TRUE_OR_FALSE) {
            return TYPE_TRUE_OR_FALSE;
        } else {
            return TYPE_QUESTION_ANSWER;
        }
    }

    @Override
    public int getItemCount() {
        return question.getAnswersAttributes().size() + 2;
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

            if (mode != Constants.SOLVE_QUIZ) {
                itemView.setClickable(false);
                radioButton.setClickable(false);
                radioButton.setFocusable(false);
                radioButton.setChecked(false);
                multiSelectionImageButton.setClickable(false);
                multiSelectionImageButton.setFocusable(false);
                matchAnswerEditText.setClickable(false);
                matchAnswerEditText.setFocusable(false);
                multiSelectionImageButton.setImageDrawable(null);
                multiSelectionImageButton.setBackground(context.getDrawable(R.drawable.curved_cool_grey_stroke));
            }
        }

        void setViews() {
            if (type == TYPE.MULTI_SELECTION) {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.white, null));
                multiSelectionImageButton.setVisibility(View.VISIBLE);
                radioButton.setVisibility(View.GONE);
                sortImageView.setVisibility(View.GONE);
                matchAnswerEditText.setVisibility(View.GONE);
            } else if (type == TYPE.SINGLE_SELECTION) {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.white, null));
                radioButton.setVisibility(View.VISIBLE);
                multiSelectionImageButton.setVisibility(View.GONE);
                sortImageView.setVisibility(View.GONE);
                matchAnswerEditText.setVisibility(View.GONE);
            } else if (type == TYPE.REORDER_ANSWERS) {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.white, null));
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
            notifyDataSetChanged();
        }

        @Override
        public void onClick(View view) {
            if (!type.equals(TYPE.TRUE_OR_FALSE)) {
                if (questionsObjectHashMap.containsKey(question)) {
                    if (questionsObjectHashMap.get(question) instanceof ArrayList) {
                        ArrayList<AnswersAttributes> answersAttributes = (ArrayList<AnswersAttributes>) questionsObjectHashMap.get(question);
                        if (type.equals(TYPE.MULTI_SELECTION)) {
                            for (int i = 0; i < answersAttributes.size(); i++) {
                                if (answersAttributes.get(i).getId() == question.getAnswersAttributes().get(getAdapterPosition() - 2).getId()) {
                                    answersAttributes.remove(i);
                                    questionsObjectHashMap.put(question, answersAttributes);
                                    notifyDataSetChanged();
                                    return;
                                }
                            }
                        }
                        if (type.equals(TYPE.SINGLE_SELECTION)) {
                            ArrayList<AnswersAttributes> singleAnswersAttributes = new ArrayList<>();
                            singleAnswersAttributes.add(question.getAnswersAttributes().get(getAdapterPosition() - 2));
                            questionsObjectHashMap.put(question, singleAnswersAttributes);
                        } else {
                            answersAttributes.add(question.getAnswersAttributes().get(getAdapterPosition() - 2));
                            questionsObjectHashMap.put(question, answersAttributes);
                        }
                    }
                } else {
                    ArrayList<AnswersAttributes> answersAttributes = new ArrayList<>();
                    answersAttributes.add(question.getAnswersAttributes().get(getAdapterPosition() - 2));
                    questionsObjectHashMap.put(question, answersAttributes);
                }
            }
            notifyDataSetChanged();
        }
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        private TextView questionTextView;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.tv_question);
        }
    }

    public class TrueFalseAnswerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout trueLinearLayout, falseLinearLayout;
        RadioButton trueRadioButton, falseRadioButton;

        public TrueFalseAnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            trueLinearLayout = itemView.findViewById(R.id.true_linear_layout);
            falseLinearLayout = itemView.findViewById(R.id.false_linear_layout);
            trueRadioButton = itemView.findViewById(R.id.radio_button_true);
            falseRadioButton = itemView.findViewById(R.id.radio_button_false);
            trueLinearLayout.setOnClickListener(this);
            falseLinearLayout.setOnClickListener(this);
            falseRadioButton.setClickable(false);
            falseRadioButton.setFocusable(false);
            falseRadioButton.setChecked(false);
            trueRadioButton.setClickable(false);
            trueRadioButton.setFocusable(false);
            trueRadioButton.setChecked(false);
            if (mode != Constants.SOLVE_QUIZ) {
                trueLinearLayout.setClickable(false);
                falseLinearLayout.setClickable(false);
            }
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.true_linear_layout:
                    questionsObjectHashMap.put(question, true);
                    break;
                case R.id.false_linear_layout:
                    questionsObjectHashMap.put(question, false);
                    break;
            }
            notifyDataSetChanged();
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
