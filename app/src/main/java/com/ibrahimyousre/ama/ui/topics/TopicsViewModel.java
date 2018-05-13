package com.ibrahimyousre.ama.ui.topics;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ibrahimyousre.ama.data.TopicsRepository;
import com.ibrahimyousre.ama.data.model.Topic;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TopicsViewModel extends ViewModel {

    TopicsRepository topicsRepository;

    public TopicsViewModel() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        topicsRepository = new TopicsRepository(database);
    }

    public LiveData<List<Topic>> getAllTopics() {
        return topicsRepository.getAllTopics();
    }

    public LiveData<List<Topic>> getUserTopics(String userUid) {
        return topicsRepository.getUserTopics(userUid);
    }

    public Task<Void> followTopic(String userUid, Topic topic) {
        return topicsRepository.followTopic(userUid, topic);
    }

    public Task<Void> unfollowTopic(String userUid, Topic topic) {
        return topicsRepository.unfollowTopic(userUid, topic);
    }
}
