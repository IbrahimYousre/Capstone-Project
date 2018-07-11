package com.ibrahimyousre.ama.ui.answer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.data.model.Answer;
import com.ibrahimyousre.ama.data.model.Question;
import com.ibrahimyousre.ama.data.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ibrahimyousre.ama.util.Constants.EXTRA_QUESTION;

public class AnswerActivity extends AppCompatActivity {

    @BindView(R.id.question_txt)
    TextView questionTextView;

    @BindView(R.id.answer_txt)
    EditText answerEditText;

    Question question;
    User user;

    AnswerViewModel answerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        ButterKnife.bind(this);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        answerViewModel = ViewModelProviders.of(this).get(AnswerViewModel.class);
        answerViewModel.getUser(userId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                AnswerActivity.this.user = user;
                if (submitWhenReady) onSubmit();
            }
        });
        question = (Question) getIntent().getSerializableExtra(EXTRA_QUESTION);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        questionTextView.setText(question.getBody());
    }

    boolean submitWhenReady = false;

    @OnClick(R.id.fab)
    void onSubmit() {
        if (user != null) {
            submitWhenReady = false;
            String answerText = answerEditText.getText().toString().trim();
            if (!answerText.isEmpty()) {
                Answer answer = new Answer(question, user, answerText);
                answerViewModel.addAnswer(answer).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AnswerActivity.this,
                                R.string.message_answer_added_successfully, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AnswerActivity.this,
                                R.string.error_answer, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this,
                        R.string.error_empty_answer, Toast.LENGTH_SHORT).show();
            }
        } else {
            submitWhenReady = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
