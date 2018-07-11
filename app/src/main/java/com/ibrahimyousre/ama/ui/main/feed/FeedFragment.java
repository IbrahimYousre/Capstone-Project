package com.ibrahimyousre.ama.ui.main.feed;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.adapters.FeedAdapter;
import com.ibrahimyousre.ama.data.model.Answer;
import com.ibrahimyousre.ama.data.model.Question;
import com.ibrahimyousre.ama.data.model.Topic;
import com.ibrahimyousre.ama.ui.ask.AskActivity;
import com.ibrahimyousre.ama.ui.profile.ProfileActivity;
import com.ibrahimyousre.ama.ui.questions.QuestionActivity;
import com.ibrahimyousre.ama.ui.topic.TopicActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ibrahimyousre.ama.util.Constants.EXTRA_ANSWER_ID;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_QUESTION;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_TOPIC;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_USER_ID;
import static com.ibrahimyousre.ama.util.Constants.STATE_SCROLL_POSITION;

public class FeedFragment extends Fragment implements FeedAdapter.FeedCallbacks {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    FeedViewModel feedViewModel;
    FeedAdapter adapter;

    public FeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, root);
        toolbar.setTitle(R.string.navigation_feed);
        toolbar.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.white));
        toolbar.inflateMenu(R.menu.ask);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_ask) {
                    Intent intent = new Intent(getActivity(), AskActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        adapter = new FeedAdapter(this);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            recyclerViewState = savedInstanceState.getParcelable(STATE_SCROLL_POSITION);
        feedViewModel = ViewModelProviders.of(this).get(FeedViewModel.class);
        feedViewModel.getUserFeed(FirebaseAuth.getInstance().getUid()).observe(this, new Observer<List<Answer>>() {
            @Override
            public void onChanged(@Nullable List<Answer> answers) {
                adapter.setAnswers(answers);
                adapter.notifyDataSetChanged();
                if (recyclerViewState != null) {
                    recyclerView.getLayoutManager()
                            .onRestoreInstanceState(recyclerViewState);
                }
            }
        });
    }

    @Override
    public void onExploreTopic(Topic topic) {
        Intent intent = new Intent(getContext(), TopicActivity.class);
        intent.putExtra(EXTRA_TOPIC, topic);
        startActivity(intent);
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
