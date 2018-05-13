package com.ibrahimyousre.ama.data;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.ibrahimyousre.ama.data.model.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicsRepository {

    private static final String PATH_TOPICS = "topics";
    private static final String PATH_TOPIC_NAME = "name";
    private static final String PATH_USERS_TOPICS = "users_topics";

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
                firebaseDatabase.getReference(PATH_TOPICS));
        return Transformations.map(allTopicsLiveData, topicsDeserializer);
    }

    public LiveData<List<Topic>> getUserTopics(String userUid) {
        FirebaseQueryLiveData userTopicsLiveData = new FirebaseQueryLiveData(
                firebaseDatabase.getReference(PATH_USERS_TOPICS).child(userUid));
        return Transformations.map(userTopicsLiveData, topicsDeserializer);
    }

    public Task<Void> followTopic(String userUid, Topic topic) {
        return firebaseDatabase.getReference(PATH_USERS_TOPICS)
                .child(userUid).child(topic.getUid()).child(PATH_TOPIC_NAME)
                .setValue(topic.getName());
    }

    public Task<Void> unfollowTopic(String userUid, Topic topic) {
        return firebaseDatabase.getReference(PATH_USERS_TOPICS)
                .child(userUid).child(topic.getUid())
                .setValue(null);
    }
}
