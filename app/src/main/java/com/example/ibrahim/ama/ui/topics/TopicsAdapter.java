package com.example.ibrahim.ama.ui.topics;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ibrahim.ama.R;
import com.example.ibrahim.ama.data.model.Topic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicViewHolder> {

    List<Topic> topics;

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
    }

    @Override
    public int getItemCount() {
        return topics == null ? 0 : topics.size();
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.topic_name)
        TextView topicNameTextView;

        public TopicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.setDebug(true);
            ButterKnife.bind(this, itemView);
        }
    }
}
