package com.example.ibrahim.ama;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTopicActivity extends AppCompatActivity {

    @BindView(R.id.topic_name_txt)
    EditText topicNameEditText;

    FirebaseDatabase database;
    DatabaseReference mTopicsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        mTopicsReference = database.getReference("topics");
    }

    @OnClick(R.id.add_btn)
    void onAddTopic() {
        String topicName = topicNameEditText.getText().toString().trim();
        if (!topicName.isEmpty()) {
            mTopicsReference.push().child("name").setValue(topicName)
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void a) {
                            Toast.makeText(AddTopicActivity.this,
                                    R.string.topic_add_message, Toast.LENGTH_SHORT).show();
                            topicNameEditText.setText("");
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddTopicActivity.this,
                                    R.string.topic_add_error, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
