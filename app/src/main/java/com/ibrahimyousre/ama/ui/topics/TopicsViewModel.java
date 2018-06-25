package com.ibrahimyousre.ama.ui.topics;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.ibrahimyousre.ama.data.Repository;
import com.ibrahimyousre.ama.data.model.Topic;

import java.util.List;

public class TopicsViewModel extends ViewModel {

    private final Repository repository;

    public TopicsViewModel() {
        repository = Repository.getInstance();
    }

    public LiveData<List<Topic>> getAllTopics() {
        return repository.getAllTopics();
    }

    public LiveData<List<Topic>> getUserTopics(String userUid) {
        return repository.getUserTopics(userUid);
    }

    public Task<Void> followTopic(String userUid, Topic topic) {
        return repository.followTopic(userUid, topic);
    }

    public Task<Void> unfollowTopic(String userUid, Topic topic) {
        return repository.unfollowTopic(userUid, topic);
    }
}
