package com.ibrahimyousre.ama.ui.topic;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.data.model.Answer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.AnswerViewHolder> {

    private List<Answer> answers;

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer answer = answers.get(position);
        holder.questionTextView.setText(answer.getQuestionBody());
        holder.userTextView.setText(answer.getUserName() + ", " + answer.getUserTitle());
        holder.timeTextView.setText("Answered 26 Apr 201");
        holder.answerTextView.setText(answer.getText());
        Glide.with(holder.itemView).load(answer.getUserPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.userImageView);
    }

    @Override
    public int getItemCount() {
        return answers == null ? 0 : answers.size();
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.question_txt)
        TextView questionTextView;

        @BindView(R.id.user_txt)
        TextView userTextView;

        @BindView(R.id.time_txt)
        TextView timeTextView;

        @BindView(R.id.answer_txt)
        TextView answerTextView;

        @BindView(R.id.user_image)
        ImageView userImageView;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
