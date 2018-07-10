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
import com.ibrahimyousre.ama.data.model.Topic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.AnswerViewHolder> {

    private List<Answer> answers;
    private FeedCallbacks mFeedCallbacks;

    private DateFormat dateFormat = SimpleDateFormat.getDateInstance();

    public interface FeedCallbacks {
        void onQuestionClicked(Question question);

        void onUserClicked(String userId);

        void onAnswerClicked(Answer answer);

        void onExploreTopic(Topic topic);
    }

    public FeedAdapter(FeedCallbacks mFeedCallbacks) {
        this.mFeedCallbacks = mFeedCallbacks;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feed, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Answer answer = answers.get(position);
        holder.topicTextView.setText(answer.getTopicName());
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

        @BindView(R.id.topic_txt)
        TextView topicTextView;

        AnswerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            int refIds[] = userInfoGroup.getReferencedIds();
            for (int id : refIds) {
                itemView.findViewById(id).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Answer answer = answers.get(getAdapterPosition());
                        mFeedCallbacks.onUserClicked(answer.getUserId());
                    }
                });
            }
        }

        @OnClick(R.id.question_txt)
        void questionClicked() {
            Answer answer = answers.get(getAdapterPosition());
            Question question = new Question(answer);
            mFeedCallbacks.onQuestionClicked(question);
        }

        @OnClick(R.id.answer_txt)
        void answerClicked() {
            Answer answer = answers.get(getAdapterPosition());
            mFeedCallbacks.onAnswerClicked(answer);
        }

        @OnClick(R.id.explore_btn)
        void exploreClicked() {
            Topic topic = new Topic(answers.get(getAdapterPosition()));
            mFeedCallbacks.onExploreTopic(topic);
        }

        @OnClick(R.id.topic_txt)
        void topicClicked() {
            Topic topic = new Topic(answers.get(getAdapterPosition()));
            mFeedCallbacks.onExploreTopic(topic);
        }
    }
}
