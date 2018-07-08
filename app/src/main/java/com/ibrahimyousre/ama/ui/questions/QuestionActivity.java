package com.ibrahimyousre.ama.ui.questions;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.adapters.AnswersAdapter;
import com.ibrahimyousre.ama.data.model.Answer;
import com.ibrahimyousre.ama.data.model.Question;
import com.ibrahimyousre.ama.ui.answer.AnswerActivity;
import com.ibrahimyousre.ama.ui.profile.ProfileActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.ibrahimyousre.ama.util.Constants.EXTRA_ANSWER_ID;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_QUESTION;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_USER_ID;

public class QuestionActivity extends AppCompatActivity implements AnswersAdapter.AnswerCallbacks {

    @BindView(R.id.question_txt)
    TextView questionTextView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    Question question;
    String answerId;

    QuestionViewModel questionViewModel;
    AnswersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);

        questionViewModel = ViewModelProviders.of(this).get(QuestionViewModel.class);

        question = (Question) getIntent().getSerializableExtra(EXTRA_QUESTION);
        answerId = getIntent().getStringExtra(EXTRA_ANSWER_ID);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        questionTextView.setText(question.getBody());
        adapter = new AnswersAdapter(this, AnswersAdapter.NO_QUESTION_USER_INFO_INCLUDED);
        recyclerView.setAdapter(adapter);

        questionViewModel.getAnswersByQuestion(question.getUid()).observe(this, new Observer<List<Answer>>() {
            @Override
            public void onChanged(@Nullable List<Answer> answers) {
                int adapterPosition = 0;
                if (answerId != null) {
                    int i = 0;
                    for (Answer a : answers) {
                        Timber.d("%s", a.getUid());
                        if (a.getUid().equals(answerId)) {
                            adapterPosition = i;
                            answerId = null;
                            break;
                        }
                        i++;
                    }
                }
                adapter.setAnswers(answers);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapterPosition);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    void onAddAnswer() {
        Intent intent = new Intent(this, AnswerActivity.class);
        intent.putExtra(EXTRA_QUESTION, question);
        startActivity(intent);
    }

    @Override
    public void onQuestionClicked(Question question) {

    }

    @Override
    public void onUserClicked(String userId) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        startActivity(intent);
    }

    @Override
    public void onAnswerClicked(Answer answer) {

    }
}
