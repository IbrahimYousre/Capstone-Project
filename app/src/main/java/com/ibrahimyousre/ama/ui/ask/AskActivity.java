package com.ibrahimyousre.ama.ui.ask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ibrahimyousre.ama.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AskActivity extends AppCompatActivity {

    private static final int MAX_CHARACHTER_COUNTER = 150;

    @BindView(R.id.topic_txt)
    TextView topicTextView;

    @BindView(R.id.characters_counter_txt)
    TextView charactersCounterTextView;

    @BindView(R.id.question_txt)
    EditText questionEditText;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        final String counterFormat = getString(R.string.character_counter_format);
        charactersCounterTextView.setText(
                String.format(counterFormat, 0, MAX_CHARACHTER_COUNTER));

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(MAX_CHARACHTER_COUNTER);
        questionEditText.setFilters(filters);
        questionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                charactersCounterTextView.setText(
                        String.format(counterFormat, s.length(), MAX_CHARACHTER_COUNTER));
            }
        });
    }

    @OnClick(R.id.topic_txt)
    void onSelectTopic(View view) {
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab)
    void onSubmit() {
        finish();
    }
}
