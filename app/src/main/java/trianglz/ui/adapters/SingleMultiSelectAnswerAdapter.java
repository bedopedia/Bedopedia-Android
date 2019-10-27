package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
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

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import trianglz.models.Answers;
import trianglz.models.AnswersAttributes;
import trianglz.models.Questions;
import trianglz.utils.Constants;

public class SingleMultiSelectAnswerAdapter extends RecyclerView.Adapter {

    public Questions question;
    private Context context;
    public TYPE type;

    public HashMap<Integer, ArrayList<Answers>> questionsAnswerHashMap;
    public static final int TYPE_QUESTION = 0;
    public static final int TYPE_ANSWER_TEXT = 1;
    public static final int TYPE_QUESTION_ANSWER = 2;
    private static final int TYPE_TRUE_OR_FALSE = 3;
    private int mode;

    public SingleMultiSelectAnswerAdapter(Context context, int mode) {
        this.context = context;
        questionsAnswerHashMap = new HashMap<>();
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
            holder.questionTextView.setHtml(question.getBody());
        } else if (type.equals(TYPE.MATCH_ANSWERS)) {
            ArrayList<Answers> answers = (ArrayList<Answers>) question.getAnswers();
            if (position < answers.get(0).getOptions().size() + 1) {
                final QuestionAnswerViewHolder holder = (QuestionAnswerViewHolder) viewHolder;
                holder.setViews();
                holder.matchQuestionNumberTextView.setText(position + "");
                holder.matchAnswerEditText.setVisibility(View.GONE);
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.questionAnswerTextView.setHtml(answers.get(0).getOptions().get(position - 1).getBody());
            } else if (position > answers.get(0).getOptions().size() + 1) {
                final QuestionAnswerViewHolder holder = (QuestionAnswerViewHolder) viewHolder;
                holder.setViews();
                String digits = "";
                for (int i = 0; i < answers.get(0).getOptions().size(); i++) {
                    digits = digits + (i + 1);
                }
                if (answers.get(0).getOptions().size() < 10) {
                    holder.matchAnswerEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
                }
                holder.matchAnswerEditText.setKeyListener(DigitsKeyListener.getInstance(digits));
                holder.matchAnswerEditText.addTextChangedListener(holder);
                holder.matchAnswerEditText.setVisibility(View.VISIBLE);
                holder.matchQuestionNumberTextView.setVisibility(View.GONE);
                holder.questionAnswerTextView.setHtml(answers.get(0).getMatches().get(position - answers.get(0).getOptions().size() - 2));
                if (questionsAnswerHashMap.containsKey(question.getId())) {
                    ArrayList<Answers> answers1 = questionsAnswerHashMap.get(question.getId());
                    for (int i = 0; i < answers1.size(); i++) {
                        if (holder.questionAnswerTextView.getText().toString().equals(answers1.get(i).getMatch())) {
                            holder.matchAnswerEditText.setText(answers1.get(i).getMatchIndex() + "");
                        }
                    }
                }
            }
        } else if (position != 1 && !type.equals(TYPE.TRUE_OR_FALSE)) {
            final QuestionAnswerViewHolder holder = (QuestionAnswerViewHolder) viewHolder;
            holder.setViews();
            if (mode != Constants.SOLVE_QUIZ) {
                answersAttributes = question.getAnswersAttributes().get(position - 2);
                holder.questionAnswerTextView.setHtml(answersAttributes.getBody());
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
                        ArrayList<Object> objects = new ArrayList<>();
                        objects.addAll(question.getAnswersAttributes());
                        ArrayList<Object> sortMatchAnswers = sortMatchAnswers(objects);
                        if (sortMatchAnswers.get(position - 2) instanceof AnswersAttributes) {
                            AnswersAttributes answersAttribute = (AnswersAttributes) sortMatchAnswers.get(position - 2);
                            holder.questionAnswerTextView.setHtml(answersAttribute.getBody());
                        }
                    } else if (type.equals(TYPE.MATCH_ANSWERS)) {
                        holder.matchAnswerEditText.setText(answersAttributes.getMatch());
                    }
                }
            } else {
                ArrayList<Answers> answers = (ArrayList<Answers>) question.getAnswers();
                holder.questionAnswerTextView.setHtml(answers.get(position - 2).getBody());
                if (questionsAnswerHashMap.containsKey(question.getId())) {
                    ArrayList<Answers> answers1 = questionsAnswerHashMap.get(question.getId());
                    for (int i = 0; i < answers1.size(); i++) {
                        if (answers1.get(i).getId() == question.getAnswers().get(position - 2).getId() || answers1.get(i).getAnswerId() == question.getAnswers().get(position - 2).getId()) {
                            if (type.equals(TYPE.MULTI_SELECTION)) {
                                holder.multiSelectionImageButton.setImageResource(R.drawable.ic_white_check);
                                holder.multiSelectionImageButton.setBackground(context.getDrawable(R.drawable.curved_check_box_background_student));
                            } else if (type.equals(TYPE.SINGLE_SELECTION)) {
                                holder.radioButton.setChecked(true);
                            }
                            return;
                        }
                    }
                    if (type.equals(TYPE.MULTI_SELECTION)) {
                        holder.multiSelectionImageButton.setImageDrawable(null);
                        holder.multiSelectionImageButton.setBackground(context.getDrawable(R.drawable.curved_cool_grey_stroke));
                    } else if (type.equals(TYPE.SINGLE_SELECTION)) {
                        holder.radioButton.setChecked(false);
                    }
                } else {
                    if (type.equals(TYPE.MULTI_SELECTION)) {
                        holder.multiSelectionImageButton.setImageDrawable(null);
                        holder.multiSelectionImageButton.setBackground(context.getDrawable(R.drawable.curved_cool_grey_stroke));
                    } else if (type.equals(TYPE.SINGLE_SELECTION)) {
                        holder.radioButton.setChecked(false);
                    } else if (type.equals(TYPE.REORDER_ANSWERS)) {
                        ArrayList<Object> objects = new ArrayList<>();
                        objects.addAll(question.getAnswers());
                        ArrayList<Object> sortMatchAnswers = sortMatchAnswers(objects);
                        question.answers.clear();
                        question.answers.addAll((List) sortMatchAnswers);
                        if (sortMatchAnswers.get(position - 2) instanceof Answers) {
                            Answers answer = (Answers) sortMatchAnswers.get(position - 2);
                            holder.questionAnswerTextView.setHtml(answer.getBody());
                        }
                    }
                }
            }
        } else if (position != 1) {
            final TrueFalseAnswerViewHolder holder = (TrueFalseAnswerViewHolder) viewHolder;
            if (mode != Constants.SOLVE_QUIZ) {
                answersAttributes = question.getAnswersAttributes().get(position - 2);
                if (mode == Constants.VIEW_ANSWERS) {
                    if (answersAttributes.isCorrect()) {
                        holder.trueRadioButton.setChecked(true);
                    } else {
                        holder.falseRadioButton.setChecked(true);
                    }
                }
            } else {
                if (questionsAnswerHashMap.containsKey(question.getId())) {
                    ArrayList<Answers> answers = questionsAnswerHashMap.get(question.getId());
                    if (answers.get(position - 2).isCorrect()) {
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


    @Override
    public int getItemViewType(int position) {
        if (type == TYPE.MATCH_ANSWERS) {
            ArrayList<Answers> answers = (ArrayList<Answers>) question.getAnswers();
            if (position == 0)
                return TYPE_QUESTION;
            else if (position == answers.get(0).getOptions().size() + 1) {
                return TYPE_ANSWER_TEXT;
            } else {
                return TYPE_QUESTION_ANSWER;
            }
        } else {
            if (position == 0)
                return TYPE_QUESTION;
            else if (position == 1) {
                return TYPE_ANSWER_TEXT;
            } else if (type == TYPE.TRUE_OR_FALSE) {
                return TYPE_TRUE_OR_FALSE;
            } else {
                return TYPE_QUESTION_ANSWER;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (question != null) {
            if (mode == Constants.SOLVE_QUIZ) {
                ArrayList<Answers> answers = (ArrayList<Answers>) question.getAnswers();
                if (type.equals(TYPE.MATCH_ANSWERS)) {
                    return answers.get(0).getOptions().size() + answers.get(0).getMatches().size() + 2;
                } else {
                    return answers.size() + 2;
                }
            } else {
                return question.getAnswersAttributes().size() + 2;
            }
        } else {
            return 0;
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

    class QuestionAnswerViewHolder extends RecyclerView.ViewHolder implements TextWatcher, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        HtmlTextView questionAnswerTextView;
        TextView matchQuestionNumberTextView;
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
            matchQuestionNumberTextView = itemView.findViewById(R.id.question_number_tv);
            itemView.setOnClickListener(this);
            radioButton.setOnClickListener(this);
            multiSelectionImageButton = itemView.findViewById(R.id.btn_multi_select);

            if (mode != Constants.SOLVE_QUIZ) {
                itemView.setClickable(false);
                radioButton.setClickable(false);
                radioButton.setChecked(false);
                multiSelectionImageButton.setClickable(false);
                multiSelectionImageButton.setFocusable(false);
                matchAnswerEditText.setClickable(false);
                matchAnswerEditText.setFocusable(false);
                matchAnswerEditText.setEnabled(false);
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
                matchQuestionNumberTextView.setVisibility(View.GONE);
            } else if (type == TYPE.SINGLE_SELECTION) {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.white, null));
                radioButton.setVisibility(View.VISIBLE);
                radioButton.setChecked(false);
                multiSelectionImageButton.setVisibility(View.GONE);
                sortImageView.setVisibility(View.GONE);
                matchAnswerEditText.setVisibility(View.GONE);
                matchQuestionNumberTextView.setVisibility(View.GONE);
            } else if (type == TYPE.REORDER_ANSWERS) {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.white, null));
                sortImageView.setVisibility(View.VISIBLE);
                radioButton.setVisibility(View.GONE);
                multiSelectionImageButton.setVisibility(View.GONE);
                matchAnswerEditText.setVisibility(View.GONE);
                matchQuestionNumberTextView.setVisibility(View.GONE);
            } else if (type == TYPE.MATCH_ANSWERS) {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.transparent, null));
                questionAnswerTextView.setPadding(22, 0, 0, 0);
                matchAnswerEditText.setVisibility(View.VISIBLE);
                sortImageView.setVisibility(View.GONE);
                radioButton.setVisibility(View.GONE);
                multiSelectionImageButton.setVisibility(View.GONE);
                matchQuestionNumberTextView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            notifyDataSetChanged();
        }

        @Override
        public void onClick(View view) {
            if (!type.equals(TYPE.MATCH_ANSWERS) && !type.equals(TYPE.REORDER_ANSWERS)) {
                if (questionsAnswerHashMap.containsKey(question.getId())) {
                    ArrayList<Answers> answers = questionsAnswerHashMap.get(question.getId());
                    if (type.equals(TYPE.MULTI_SELECTION)) {
                        for (int i = 0; i < answers.size(); i++) {
                            if (answers.get(i).getId() == question.getAnswers().get(getAdapterPosition() - 2).getId()) {
                                answers.remove(i);
                                questionsAnswerHashMap.put(question.getId(), answers);
                                notifyDataSetChanged();
                                return;
                            }
                        }
                    }
                    if (type.equals(TYPE.SINGLE_SELECTION)) {
                        ArrayList<Answers> singleAnswersAttributes = new ArrayList<>();
                        singleAnswersAttributes.add(question.getAnswers().get(getAdapterPosition() - 2));
                        questionsAnswerHashMap.put(question.getId(), singleAnswersAttributes);
                    } else {
                        answers.add(question.getAnswers().get(getAdapterPosition() - 2));
                        questionsAnswerHashMap.put(question.getId(), answers);
                    }
                } else {
                    ArrayList<Answers> answersAttributes = new ArrayList<>();
                    answersAttributes.add(question.getAnswers().get(getAdapterPosition() - 2));
                    questionsAnswerHashMap.put(question.getId(), answersAttributes);
                }

                notifyDataSetChanged();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!editable.toString().isEmpty()) {
                String answerText = editable.toString();
                int index = Integer.parseInt(answerText);
                if (index - 1 < question.getAnswers().get(0).getOptions().size() && index - 1 >= 0) {
                    Answers answer = question.getAnswers().get(0).getOptions().get(index - 1);
                    if (questionsAnswerHashMap.containsKey(question.getId())) {
                        ArrayList<Answers> answers = questionsAnswerHashMap.get(question.getId());
                        for (int i = 0; i < answers.size(); i++) {
                            if (answers.get(i).getId() == answer.getId()) {
                                answers.get(i).setMatchIndex(index);
                                answers.get(i).setMatch(questionAnswerTextView.getText().toString());
                                questionsAnswerHashMap.put(question.getId(), answers);
                                return;
                            }
                        }
                        answer.setMatchIndex(index);
                        answer.setMatch(questionAnswerTextView.getText().toString());
                        answers.add(answer);
                        questionsAnswerHashMap.put(question.getId(), answers);

                    } else {
                        ArrayList<Answers> answers = new ArrayList<>();
                        Answers answer1 = question.getAnswers().get(0).getOptions().get(index - 1);
                        answer.setMatchIndex(index);
                        answer.setMatch(questionAnswerTextView.getText().toString());
                        answers.add(answer1);
                        questionsAnswerHashMap.put(question.getId(), answers);
                    }
                }
            } else {

            }
        }
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        private HtmlTextView questionTextView;

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
                    question.getAnswers().get(getAdapterPosition() - 2).setCorrect(true);
                    questionsAnswerHashMap.put(question.getId(), (ArrayList) question.getAnswers());
                    break;
                case R.id.false_linear_layout:
                    question.getAnswers().get(getAdapterPosition() - 2).setCorrect(false);
                    questionsAnswerHashMap.put(question.getId(), (ArrayList) question.getAnswers());
                    break;
            }
            notifyDataSetChanged();
        }
    }

    public class AnswerTextViewHolder extends RecyclerView.ViewHolder {
        AnswerTextViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private ArrayList<Object> sortMatchAnswers(ArrayList<Object> answersAttributes) {
        ArrayList<Object> sortedAnswersAttributes = new ArrayList<>(answersAttributes);
        for (int i = 0; i < answersAttributes.size(); i++) {
            for (int k = 0; k < answersAttributes.size(); k++) {
                Log.d("test", "" + i + " " + k);
                if (answersAttributes.get(i) instanceof AnswersAttributes) {
                    AnswersAttributes answersAttribute = (AnswersAttributes) answersAttributes.get(i);
                    if (Integer.parseInt(answersAttribute.getMatch()) == k + 1) {
                        sortedAnswersAttributes.set(k, answersAttributes.get(i));
                    }
                } else if (answersAttributes.get(i) instanceof Answers) {
                    Answers answers = (Answers) answersAttributes.get(i);
                    int x = k + 1;
                    if (Integer.parseInt(answers.getMatch()) == x) {
                        sortedAnswersAttributes.set(k, answersAttributes.get(i));
                    }
                }
            }
        }
        return sortedAnswersAttributes;
    }


}
