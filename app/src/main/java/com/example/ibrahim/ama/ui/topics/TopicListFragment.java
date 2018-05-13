package com.example.ibrahim.ama.ui.topics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ibrahim.ama.R;
import com.example.ibrahim.ama.data.model.Topic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicListFragment extends Fragment implements TopicsAdapter.TopicsProvider {

    public static final String KEY_TOPICS_TO_SHOW = "topics";
    public static final String SHOW_ALL_TOPICS = "all";
    public static final String SHOW_USER_TOPICS = "user";
    public static final String KEY_USER_UID = "user_uid";

    @BindView(R.id.topic_rv)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private String userUid;
    private TopicsAdapter adapter;
    private TopicsViewModel topicsViewModel;
    private List<String> myTopicsUids = new ArrayList<>();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        topicsViewModel = ViewModelProviders.of(getParentFragment()).get(TopicsViewModel.class);
        userUid = getArguments().getString(KEY_USER_UID);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bindData();
            }
        });
        bindData();
        topicsViewModel.getUserTopics(userUid).observe(this, new Observer<List<Topic>>() {
            @Override
            public void onChanged(@Nullable List<Topic> topics) {
                myTopicsUids.clear();
                for (Topic topic : topics) {
                    myTopicsUids.add(topic.getUid());
                }
            }
        });
    }

    private void bindData() {
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
                topicsViewModel.getUserTopics(getArguments().getString(KEY_USER_UID))
                        .observe(this, new Observer<List<Topic>>() {
                            @Override
                            public void onChanged(@Nullable List<Topic> topics) {
                                adapter.notifyDataSetChanged();
                            }
                        });
                break;
            case SHOW_USER_TOPICS:
                topicsViewModel.getUserTopics(getArguments().getString(KEY_USER_UID))
                        .observe(this, topicsObserver);
                break;
        }
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
    public boolean isFollowingTopic(String topicUid) {
        return myTopicsUids != null && myTopicsUids.contains(topicUid);
    }
}
