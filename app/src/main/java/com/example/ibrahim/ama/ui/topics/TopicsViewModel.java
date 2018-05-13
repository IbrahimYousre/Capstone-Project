package com.example.ibrahim.ama.ui.topics;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.ibrahim.ama.data.TopicsRepository;
import com.example.ibrahim.ama.data.model.Topic;
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

    public LiveData<List<Topic>> getUserTopics(String uid) {
        return topicsRepository.getUserTopics(uid);
    }
}
