package com.ibrahimyousre.ama.ui.topic;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.adapters.QuestionsAdapter;
import com.ibrahimyousre.ama.data.model.Question;
import com.ibrahimyousre.ama.ui.answer.AnswerActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ibrahimyousre.ama.util.Constants.EXTRA_QUESTION;
import static com.ibrahimyousre.ama.util.Constants.STATE_SCROLL_POSITION;

public class AnswerTopicFragment extends Fragment implements QuestionsAdapter.OnAnswerQuestionClickListender {

    public static final String KEY_TOPIC_UID = "topic_id";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private String topicId;

    TopicViewModel topicViewModel;

    QuestionsAdapter adapter;

    public AnswerTopicFragment() {
    }

    public static Fragment getInstance(String topicId) {
        Fragment fragment = new AnswerTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TOPIC_UID, topicId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_topic, container, false);
        ButterKnife.bind(this, view);
        adapter = new QuestionsAdapter(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            recyclerViewState = savedInstanceState.getParcelable(STATE_SCROLL_POSITION);
        topicId = getArguments().getString(KEY_TOPIC_UID);
        topicViewModel = ViewModelProviders.of(getActivity()).get(TopicViewModel.class);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        refresh();
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        topicViewModel.getQuestionsByTopic(topicId).observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                adapter.setQuestions(questions);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                if (recyclerViewState != null) {
                    recyclerView.getLayoutManager()
                            .onRestoreInstanceState(recyclerViewState);
                }
            }
        });
    }

    @Override
    public void onAnswerQuestionClicked(Question question) {
        Intent intent = new Intent(getActivity(), AnswerActivity.class);
        intent.putExtra(EXTRA_QUESTION, question);
        startActivity(intent);
    }

    @Override
    public void onFollowQuestion(Question question) {
    }

    Parcelable recyclerViewState;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_SCROLL_POSITION,
                recyclerView.getLayoutManager().onSaveInstanceState());
    }
}
