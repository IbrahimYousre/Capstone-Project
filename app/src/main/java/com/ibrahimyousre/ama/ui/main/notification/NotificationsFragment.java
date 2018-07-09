package com.ibrahimyousre.ama.ui.main.notification;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
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
import com.ibrahimyousre.ama.adapters.NotificationsAdapter;
import com.ibrahimyousre.ama.data.model.Answer;
import com.ibrahimyousre.ama.ui.ask.AskActivity;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ibrahimyousre.ama.util.Constants.EXTRA_TOPIC;

public class NotificationsFragment extends Fragment
        implements NotificationsAdapter.AnswerClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    NotificationsAdapter adapter;
    NotificationViewModel notificationViewModel;

    public NotificationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this, root);
        toolbar.setTitle(R.string.navigation_notifications);
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
        adapter = new NotificationsAdapter(this);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        notificationViewModel = ViewModelProviders.of(this)
                .get(NotificationViewModel.class);
        notificationViewModel.getNotificationsForUser(FirebaseAuth.getInstance().getUid())
                .observe(this, new Observer<List<Answer>>() {
                    @Override
                    public void onChanged(@Nullable List<Answer> answers) {
                        adapter.setAnswers(answers);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onAnswerClicked(Answer answer) {

    }
}
