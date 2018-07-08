package com.ibrahimyousre.ama.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.data.model.Answer;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private List<Answer> answers;
    private AnswerClickListener mAnswerClickListener;

    public interface AnswerClickListener {
        void onAnswerClicked(Answer answer);
    }

    public NotificationsAdapter(AnswerClickListener answerClickListener) {
        this.mAnswerClickListener = answerClickListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Answer answer = answers.get(position);
        Date date = new Date(answer.getTimestamp());
        CharSequence niceDateStr = DateUtils.getRelativeTimeSpanString(date.getTime());
        holder.timeTextView.setText(niceDateStr);
        holder.questionTextView.setText(answer.getQuestionBody());
        holder.userTextView.setText(context.getString(R.string.notification_format,
                answer.getUserName()));
    }

    @Override
    public int getItemCount() {
        return answers == null ? 0 : answers.size();
    }

    public void setAnswers(List<Answer> answers) {
        Collections.reverse(answers);
        this.answers = answers;
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_txt)
        TextView userTextView;

        @BindView(R.id.time_txt)
        TextView timeTextView;

        @BindView(R.id.question_txt)
        TextView questionTextView;

        NotificationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAnswerClickListener.onAnswerClicked(answers.get(getAdapterPosition()));
                }
            });
        }
    }
}
