package com.example.ibrahim.ama.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.ibrahim.ama.data.model.Topic;
import com.example.ibrahim.ama.util.DatabaseErrorUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TopicsRepository {

    private FirebaseDatabase firebaseDatabase;

    public TopicsRepository(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public LiveData<List<Topic>> getAllTopics() {
        final MutableLiveData<List<Topic>> data = new MutableLiveData<>();
        firebaseDatabase.getReference("topics").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Topic> topics = new ArrayList<>((int) dataSnapshot.getChildrenCount());
                for (DataSnapshot topicSnapShot : dataSnapshot.getChildren()) {
                    Topic topic = topicSnapShot.getValue(Topic.class);
                    topic.setUid(topicSnapShot.getKey());
                    topics.add(topic);
                }
                data.setValue(topics);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DatabaseErrorUtils.log(databaseError);
            }
        });
        return data;
    }

    public LiveData<List<Topic>> getUserTopics(String userUid) {
        final MutableLiveData<List<Topic>> data = new MutableLiveData<>();
        firebaseDatabase.getReference("users_topics").child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Topic> topics = new ArrayList<>((int) dataSnapshot.getChildrenCount());
                for (DataSnapshot topicSnapShot : dataSnapshot.getChildren()) {
                    Topic topic = topicSnapShot.getValue(Topic.class);
                    topic.setUid(topicSnapShot.getKey());
                    topics.add(topic);
                }
                data.setValue(topics);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DatabaseErrorUtils.log(databaseError);
            }
        });
        return data;
    }

}
