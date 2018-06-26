package com.ibrahimyousre.ama.ui.topics;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.data.model.Topic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicViewHolder> {

    List<Topic> topics;
    final TopicsProvider topicsProvider;

    public interface TopicsProvider {
        void onTopicSelected(Topic topic);

        void followTopic(Topic topic);

        void unfollowTopic(Topic topic);

        boolean isFollowingTopic(String topicUid);
    }

    public TopicsAdapter(TopicsProvider topicsProvider) {
        this.topicsProvider = topicsProvider;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topics.get(position);
        holder.topicNameTextView.setText(topic.getName());
        if (topicsProvider.isFollowingTopic(topic.getUid())) {
            holder.actionButton.setText(R.string.unfollow);
        } else {
            holder.actionButton.setText(R.string.follow);
        }
    }

    @Override
    public int getItemCount() {
        return topics == null ? 0 : topics.size();
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.topic_name)
        TextView topicNameTextView;

        @BindView(R.id.topic_action_btn)
        Button actionButton;

        public TopicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.topic_card_view)
        void onTopicClicked() {
            Topic topic = topics.get(getAdapterPosition());
            topicsProvider.onTopicSelected(topic);
        }

        @OnClick(R.id.topic_action_btn)
        void onActionClicked() {
            Topic topic = topics.get(getAdapterPosition());
            if (topicsProvider.isFollowingTopic(topic.getUid()))
                topicsProvider.unfollowTopic(topic);
            else
                topicsProvider.followTopic(topic);
        }
    }
}
