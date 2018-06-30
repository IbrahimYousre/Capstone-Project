package com.ibrahimyousre.ama.ui.topic;

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

import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.adapters.AnswersAdapter;
import com.ibrahimyousre.ama.data.model.Answer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadTopicFragment extends Fragment {

    public static final String KEY_TOPIC_UID = "topic_id";

    @BindView(R.id.topic_rv)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private String topicId;

    TopicViewModel topicViewModel;

    AnswersAdapter adapter;

    public ReadTopicFragment() {
    }

    public static Fragment getInstance(String topicId) {
        Fragment fragment = new ReadTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TOPIC_UID, topicId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_topic, container, false);
        ButterKnife.bind(this, view);
        adapter = new AnswersAdapter(AnswersAdapter.QUESTION_AND_USER_INFO_INCLUDED);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        topicId = getArguments().getString(KEY_TOPIC_UID);
        topicViewModel = ViewModelProviders.of(getActivity()).get(TopicViewModel.class);
        topicViewModel.getAnswersByTopic(topicId).observe(this, new Observer<List<Answer>>() {
            @Override
            public void onChanged(@Nullable List<Answer> answers) {
                adapter.setAnswers(answers);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
