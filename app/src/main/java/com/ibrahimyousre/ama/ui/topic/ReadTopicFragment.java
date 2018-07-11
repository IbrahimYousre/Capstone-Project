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
import com.ibrahimyousre.ama.adapters.AnswersAdapter;
import com.ibrahimyousre.ama.data.model.Answer;
import com.ibrahimyousre.ama.data.model.Question;
import com.ibrahimyousre.ama.ui.profile.ProfileActivity;
import com.ibrahimyousre.ama.ui.questions.QuestionActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ibrahimyousre.ama.util.Constants.EXTRA_ANSWER_ID;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_QUESTION;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_USER_ID;
import static com.ibrahimyousre.ama.util.Constants.STATE_SCROLL_POSITION;

public class ReadTopicFragment extends Fragment implements AnswersAdapter.AnswerCallbacks {

    public static final String KEY_TOPIC_UID = "topic_id";

    @BindView(R.id.recycler_view)
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
        adapter = new AnswersAdapter(this, AnswersAdapter.QUESTION_AND_USER_INFO_INCLUDED);
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
        topicViewModel.getAnswersByTopic(topicId).observe(this, new Observer<List<Answer>>() {
            @Override
            public void onChanged(@Nullable List<Answer> answers) {
                adapter.setAnswers(answers);
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
    public void onQuestionClicked(Question question) {
        Intent intent = new Intent(getActivity(), QuestionActivity.class);
        intent.putExtra(EXTRA_QUESTION, question);
        startActivity(intent);
    }

    @Override
    public void onUserClicked(String userId) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        startActivity(intent);
    }

    @Override
    public void onAnswerClicked(Answer answer) {
        Intent intent = new Intent(getActivity(), QuestionActivity.class);
        intent.putExtra(EXTRA_QUESTION, new Question(answer));
        intent.putExtra(EXTRA_ANSWER_ID, answer.getUid());
        startActivity(intent);
    }

    Parcelable recyclerViewState;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_SCROLL_POSITION,
                recyclerView.getLayoutManager().onSaveInstanceState());
    }
}
