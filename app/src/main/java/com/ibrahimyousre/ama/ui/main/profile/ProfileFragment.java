package com.ibrahimyousre.ama.ui.main.profile;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.adapters.AnswersAdapter;
import com.ibrahimyousre.ama.data.model.Answer;
import com.ibrahimyousre.ama.data.model.Question;
import com.ibrahimyousre.ama.data.model.User;
import com.ibrahimyousre.ama.ui.profile.EditTextDialogFragment;
import com.ibrahimyousre.ama.ui.questions.QuestionActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ibrahimyousre.ama.ui.profile.EditTextDialogFragment.KEY_TEXT;
import static com.ibrahimyousre.ama.ui.profile.EditTextDialogFragment.KEY_TITLE;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_ANSWER_ID;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_QUESTION;

public class ProfileFragment extends Fragment
        implements AnswersAdapter.AnswerCallbacks, EditTextDialogFragment.TextInputListener {

    private final static String KEY_USER_ID = "user_id";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.user_image)
    ImageView userImageView;

    @BindView(R.id.camera_imageView)
    ImageView cameraImageView;

    @BindView(R.id.user_name)
    TextView userNameTextView;

    @BindView(R.id.user_title)
    TextView userTitleTextView;

    @BindView(R.id.edit_name_btn)
    ImageView editNameButton;

    @BindView(R.id.edit_title_btn)
    ImageView editTitleButton;

    AnswersAdapter adapter;
    ProfileViewModel profileViewModel;
    String userId;

    User user;

    @Override
    public void onTextInput(String text) {

    }

    public interface OnNavigateBackClickListender {
        void onNavigateBack();
    }

    public interface OnSignoutClickListender {
        void signout();
    }

    public ProfileFragment() {
    }

    public static ProfileFragment getInstance(String uid) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_USER_ID, uid);
        profileFragment.setArguments(bundle);
        return profileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, root);
        adapter = new AnswersAdapter(this,
                AnswersAdapter.QUESTION_NO_USER_INFO_INCLUDED);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        userId = getArguments().getString(KEY_USER_ID);
        profileViewModel.getAnswersByUser(userId)
                .observe(this, new Observer<List<Answer>>() {
                    @Override
                    public void onChanged(@Nullable List<Answer> answers) {
                        adapter.setAnswers(answers);
                        adapter.notifyDataSetChanged();
                    }
                });
        profileViewModel.getUserById(userId)
                .observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(@Nullable User user) {
                        ProfileFragment.this.user = user;
                        Glide.with(getActivity()).load(user.getPhotoUrl())
                                .apply(RequestOptions.circleCropTransform())
                                .into(userImageView);
                        userNameTextView.setText(user.getName());
                        userTitleTextView.setText(user.getTitle());
                    }
                });
        setupNavigation();
        if (userId != FirebaseAuth.getInstance().getUid()) {
            editNameButton.setVisibility(View.GONE);
            editTitleButton.setVisibility(View.GONE);
            cameraImageView.setVisibility(View.GONE);
        }
    }

    private void setupNavigation() {
        if (getActivity() instanceof OnNavigateBackClickListender) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OnNavigateBackClickListender) getActivity()).onNavigateBack();
                }
            });
        }
        if (getActivity() instanceof OnSignoutClickListender
                && userId == FirebaseAuth.getInstance().getUid()) {
            toolbar.inflateMenu(R.menu.logout);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_logout) {
                        ((OnSignoutClickListender) getActivity()).signout();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @OnClick(R.id.edit_name_btn)
    void onEditName() {
        if (user == null) return;
        EditTextDialogFragment editDialog = new EditTextDialogFragment();
        editDialog.setTextInputListener(new EditTextDialogFragment.TextInputListener() {
            @Override
            public void onTextInput(String text) {
                user.setName(text);
                profileViewModel.updatUser(user);
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, getString(R.string.user_name));
        bundle.putString(KEY_TEXT, user.getName());
        editDialog.setArguments(bundle);
        editDialog.show(getChildFragmentManager(), "edit_dialog");
    }

    @OnClick(R.id.edit_title_btn)
    void onEditTitle() {
        if (user == null) return;
        EditTextDialogFragment editDialog = new EditTextDialogFragment();
        editDialog.setTextInputListener(new EditTextDialogFragment.TextInputListener() {
            @Override
            public void onTextInput(String text) {
                user.setTitle(text);
                profileViewModel.updatUser(user);
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, getString(R.string.title));
        bundle.putString(KEY_TEXT, user.getTitle());
        editDialog.setArguments(bundle);
        editDialog.show(getChildFragmentManager(), "edit_dialog");
    }

    @Override
    public void onQuestionClicked(Question question) {
        Intent intent = new Intent(getActivity(), QuestionActivity.class);
        intent.putExtra(EXTRA_QUESTION, question);
        startActivity(intent);
    }

    @Override
    public void onUserClicked(String userId) {
    }

    @Override
    public void onAnswerClicked(Answer answer) {
        Intent intent = new Intent(getActivity(), QuestionActivity.class);
        intent.putExtra(EXTRA_QUESTION, new Question(answer));
        intent.putExtra(EXTRA_ANSWER_ID, answer.getUid());
        startActivity(intent);
    }
}
