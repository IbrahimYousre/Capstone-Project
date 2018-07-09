package com.ibrahimyousre.ama.ui.ask;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.data.model.Question;
import com.ibrahimyousre.ama.data.model.Topic;
import com.ibrahimyousre.ama.ui.main.topics.TopicsViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ibrahimyousre.ama.ui.ask.TopicsListDialogFragment.KEY_TOPICS_LIST;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_TOPIC;

public class AskActivity extends AppCompatActivity
        implements TopicsListDialogFragment.TopicSelectListener {

    private static final int MAX_CHARACHTER_COUNTER = 150;

    @BindView(R.id.topic_txt)
    TextView topicTextView;

    @BindView(R.id.characters_counter_txt)
    TextView charactersCounterTextView;

    @BindView(R.id.question_txt)
    EditText questionEditText;

    private Topic selectedTopic;
    private List<Topic> topicList;
    private String[] topicNameArray;
    private boolean selectWhenReady = false;

    private TopicsViewModel topicsViewModel;
    private QuestionsViewModel questionsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        ButterKnife.bind(this);

        selectedTopic = (Topic) getIntent().getSerializableExtra(EXTRA_TOPIC);
        if (selectedTopic != null) {
            topicTextView.setText(selectedTopic.getName());
        }

        questionsViewModel = ViewModelProviders.of(this).get(QuestionsViewModel.class);
        topicsViewModel = ViewModelProviders.of(this).get(TopicsViewModel.class);
        topicsViewModel.getAllTopics().observe(this, new Observer<List<Topic>>() {
            @Override
            public void onChanged(@Nullable List<Topic> topics) {
                topicList = topics;
                topicNameArray = new String[topics.size()];
                for (int i = 0; i < topicNameArray.length; i++) {
                    topicNameArray[i] = topics.get(i).getName();
                }
                if (selectWhenReady) {
                    onSelectTopic();
                }
            }
        });
        setupView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {
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
    void onSelectTopic() {
        selectWhenReady = false;
        if (topicList != null) {
            DialogFragment topicsListDialog = new TopicsListDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArray(KEY_TOPICS_LIST, topicNameArray);
            topicsListDialog.setArguments(bundle);
            topicsListDialog.show(getSupportFragmentManager(), "topics_list");
        } else {
            selectWhenReady = true;
        }
    }

    @OnClick(R.id.fab)
    void onSubmit() {
        if (selectedTopic != null) {
            String questionBody = questionEditText.getText().toString().trim();
            if (!questionBody.isEmpty()) {
                Question question = new Question(selectedTopic, questionBody);
                questionsViewModel.addQuestion(question).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AskActivity.this,
                                "Question was added successfuly",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AskActivity.this,
                                "Failed to add question, please check network, and try again",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } else
                Toast.makeText(this, "Question can't be empty!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please select a topic!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTopicSelected(int topicIndex) {
        selectedTopic = topicList.get(topicIndex);
        topicTextView.setText(selectedTopic.getName());
    }
}
