package com.ibrahimyousre.ama.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
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
import com.ibrahimyousre.ama.data.model.Question;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.AnswerViewHolder> {

    public static final int QUESTION_NO_USER_INFO_INCLUDED = 1;
    public static final int NO_QUESTION_USER_INFO_INCLUDED = 2;
    public static final int QUESTION_AND_USER_INFO_INCLUDED = 3;

    private List<Answer> answers;
    private int type;
    private AnswerCallbacks mAnswerCallbacks;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

    public interface AnswerCallbacks {
        void onQuestionClicked(Question question);

        void onUserClicked(String userId);

        void onAnswerClicked(Answer answer);
    }

    public AnswersAdapter(AnswerCallbacks mAnswerCallbacks, int type) {
        this.mAnswerCallbacks = mAnswerCallbacks;
        this.type = type;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(view, type);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Answer answer = answers.get(position);
        holder.questionTextView.setText(answer.getQuestionBody());
        String userInfo = context.getString(R.string.user_info_format, answer.getUserName(), answer.getUserTitle());
        holder.userTextView.setText(userInfo);
        Date date;
        if (answer.getTimestamp() == 0) {
            date = new Date();
        } else {
            date = new Date(answer.getTimestamp());
        }
        holder.timeTextView.setText(
                context.getString(R.string.answer_timestamp_format,
                        dateFormat.format(date)));
        holder.answerTextView.setText(answer.getText());
        Glide.with(holder.itemView).load(answer.getUserPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.userImageView);
    }

    @Override
    public int getItemCount() {
        return answers == null ? 0 : answers.size();
    }

    public void setAnswers(List<Answer> answers) {
        Collections.reverse(answers);
        this.answers = answers;
    }

    class AnswerViewHolder extends RecyclerView.ViewHolder {

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

        @BindView(R.id.user_info)
        Group userInfoGroup;

        AnswerViewHolder(View itemView, int type) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            switch (type) {
                case QUESTION_NO_USER_INFO_INCLUDED:
                    userInfoGroup.setVisibility(View.GONE);
                    break;
                case NO_QUESTION_USER_INFO_INCLUDED:
                    questionTextView.setVisibility(View.GONE);
                    break;
                case QUESTION_AND_USER_INFO_INCLUDED:
                    break;
            }

            int refIds[] = userInfoGroup.getReferencedIds();
            for (int id : refIds) {
                itemView.findViewById(id).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Answer answer = answers.get(getAdapterPosition());
                        mAnswerCallbacks.onUserClicked(answer.getUserId());
                    }
                });
            }
        }

        @OnClick(R.id.question_txt)
        void questionClicked() {
            Answer answer = answers.get(getAdapterPosition());
            Question question = new Question(answer);
            mAnswerCallbacks.onQuestionClicked(question);
        }

        @OnClick(R.id.answer_txt)
        void answerClicked() {
            Answer answer = answers.get(getAdapterPosition());
            mAnswerCallbacks.onAnswerClicked(answer);
        }
    }
}
