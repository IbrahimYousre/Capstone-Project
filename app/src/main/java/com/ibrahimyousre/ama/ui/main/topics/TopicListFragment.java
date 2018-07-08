package com.ibrahimyousre.ama.ui.main.topics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.adapters.TopicsAdapter;
import com.ibrahimyousre.ama.data.model.Topic;
import com.ibrahimyousre.ama.ui.topic.TopicActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ibrahimyousre.ama.util.Constants.EXTRA_TOPIC;

public class TopicListFragment extends Fragment implements TopicsAdapter.TopicsProvider {

    private static final String KEY_TOPICS_TO_SHOW = "topics_to_show";
    private static final String SHOW_ALL_TOPICS = "all";
    private static final String SHOW_USER_TOPICS = "user";
    private static final String KEY_USER_UID = "user_uid";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private TopicsAdapter adapter;

    private String userUid;
    private TopicsViewModel topicsViewModel;
    private final List<String> myTopicsUids = new ArrayList<>();

    public TopicListFragment() {
    }

    public static Fragment getAllTopicsInstance(String userUid) {
        Fragment fragment = new TopicListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TOPICS_TO_SHOW, SHOW_ALL_TOPICS);
        bundle.putString(KEY_USER_UID, userUid);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment getUserTopicsInstance(String userUid) {
        Fragment fragment = new TopicListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TOPICS_TO_SHOW, SHOW_USER_TOPICS);
        bundle.putString(KEY_USER_UID, userUid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);
        ButterKnife.bind(this, view);
        adapter = new TopicsAdapter(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userUid = getArguments().getString(KEY_USER_UID);

        topicsViewModel = ViewModelProviders.of(getParentFragment()).get(TopicsViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        refresh();

        topicsViewModel.getUserTopics(userUid).observe(this, new Observer<List<Topic>>() {
            @Override
            public void onChanged(@Nullable List<Topic> topics) {
                myTopicsUids.clear();
                for (Topic topic : topics) {
                    myTopicsUids.add(topic.getUid());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        Observer<List<Topic>> topicsObserver = new Observer<List<Topic>>() {
            @Override
            public void onChanged(@Nullable List<Topic> topics) {
                swipeRefreshLayout.setRefreshing(false);
                adapter.setTopics(topics);
                adapter.notifyDataSetChanged();
            }
        };
        switch (getArguments().getString(KEY_TOPICS_TO_SHOW)) {
            case SHOW_ALL_TOPICS:
                topicsViewModel.getAllTopics()
                        .observe(this, topicsObserver);
                break;
            case SHOW_USER_TOPICS:
                topicsViewModel.getUserTopics(getArguments().getString(KEY_USER_UID))
                        .observe(this, topicsObserver);
                break;
        }
    }

    @Override
    public void onTopicSelected(Topic topic) {
        Intent intent = new Intent(getContext(), TopicActivity.class);
        intent.putExtra(EXTRA_TOPIC, topic);
        startActivity(intent);
    }

    @Override
    public void followTopic(Topic topic) {
        topicsViewModel.followTopic(userUid, topic);
    }

    @Override
    public void unfollowTopic(Topic topic) {
        topicsViewModel.unfollowTopic(userUid, topic);
    }

    @Override
    public boolean isFollowingTopic(Topic topic) {
        return myTopicsUids != null && myTopicsUids.contains(topic.getUid());
    }
}
