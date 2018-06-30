package com.ibrahimyousre.ama.ui.answer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ibrahimyousre.ama.MyApplication;
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

        user = MyApplication.getCurrentUser();
        question = (Question) getIntent().getSerializableExtra(EXTRA_QUESTION);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        questionTextView.setText(question.getBody());
    }

    @OnClick(R.id.fab)
    void onSubmit() {
        String answerText = answerEditText.getText().toString().trim();
        if (!answerText.isEmpty()) {
            Answer answer = new Answer(question, user, answerText);
            answerViewModel.addAnswer(answer).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AnswerActivity.this,
                            "Answer added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AnswerActivity.this,
                            "Answer couldn't be added", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this,
                    "Please provide some answer!", Toast.LENGTH_SHORT).show();
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
