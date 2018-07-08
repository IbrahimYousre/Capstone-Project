package com.ibrahimyousre.ama.ui.main.feed;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.ibrahimyousre.ama.data.Repository;
import com.ibrahimyousre.ama.data.model.Answer;
import com.ibrahimyousre.ama.data.model.Topic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeedViewModel extends ViewModel {

    private final Repository repository;
    private MediatorLiveData<List<Answer>> userFeed;

    public FeedViewModel() {
        repository = Repository.getInstance();
    }

    public LiveData<List<Answer>> getUserFeed(String uid) {
        userFeed = new MediatorLiveData<>();
        final LiveData<List<Topic>> topicListLiveData = repository.getUserTopics(uid);
        userFeed.addSource(topicListLiveData, new Observer<List<Topic>>() {
            @Override
            public void onChanged(@Nullable List<Topic> topics) {
                userFeed.removeSource(topicListLiveData);
                final Set<String> userTopics = new HashSet<>();
                for (Topic topic : topics) {
                    userTopics.add(topic.getUid());
                }
                userFeed.addSource(repository.getAllAnswers(), new Observer<List<Answer>>() {
                    @Override
                    public void onChanged(@Nullable List<Answer> answers) {
                        List<Answer> feed = new ArrayList<>();
                        for (Answer a : answers) {
                            if (userTopics.contains(a.getTopicId())) {
                                feed.add(a);
                            }
                        }
                        userFeed.setValue(feed);
                    }
                });
            }
        });
        return userFeed;
    }
}
