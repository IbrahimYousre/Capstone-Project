package com.ibrahimyousre.ama.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.data.model.Question;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    private List<Question> questions;
    private final OnAnswerQuestionClickListender answerQuestionClickListender;

    public interface OnAnswerQuestionClickListender {
        void onAnswerQuestionClicked(Question question);
    }

    public QuestionsAdapter(@NonNull OnAnswerQuestionClickListender answerQuestionClickListender) {
        this.answerQuestionClickListender = answerQuestionClickListender;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.questionTextView.setText(question.getBody());
    }

    @Override
    public int getItemCount() {
        return questions == null ? 0 : questions.size();
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.question_txt)
        TextView questionTextView;

        QuestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.answer_btn)
        void answer() {
            answerQuestionClickListender.onAnswerQuestionClicked(questions.get(getAdapterPosition()));
        }
    }
}
