package com.example.ibrahim.ama.data;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.example.ibrahim.ama.data.model.Topic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TopicsRepository {

    private FirebaseDatabase firebaseDatabase;
    private Function<DataSnapshot, List<Topic>> topicsDeserializer =
            new Function<DataSnapshot, List<Topic>>() {
                @Override
                public List<Topic> apply(DataSnapshot dataSnapshot) {
                    List<Topic> topics = new ArrayList<>((int) dataSnapshot.getChildrenCount());
                    for (DataSnapshot topicSnapShot : dataSnapshot.getChildren()) {
                        Topic topic = topicSnapShot.getValue(Topic.class);
                        topic.setUid(topicSnapShot.getKey());
                        topics.add(topic);
                    }
                    return topics;
                }
            };

    public TopicsRepository(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public LiveData<List<Topic>> getAllTopics() {
        FirebaseQueryLiveData allTopicsLiveData = new FirebaseQueryLiveData(
                firebaseDatabase.getReference("topics"));
        return Transformations.map(allTopicsLiveData, topicsDeserializer);
    }

    public LiveData<List<Topic>> getUserTopics(String userUid) {
        FirebaseQueryLiveData userTopicsLiveData = new FirebaseQueryLiveData(
                firebaseDatabase.getReference("users_topics").child(userUid));
        return Transformations.map(userTopicsLiveData, topicsDeserializer);
    }

}
