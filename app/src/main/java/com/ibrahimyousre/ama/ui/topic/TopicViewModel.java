package com.ibrahimyousre.ama.ui.topic;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ibrahimyousre.ama.data.Repository;
import com.ibrahimyousre.ama.data.model.Answer;
import com.ibrahimyousre.ama.data.model.Question;

import java.util.List;

public class TopicViewModel extends ViewModel {

    private final Repository repository;

    public TopicViewModel() {
        repository = Repository.getInstance();
    }

    public LiveData<List<Answer>> getAnswersByTopic(String uid) {
        return repository.getAnswersByTopic(uid);
    }

    public LiveData<List<Question>> getQuestionsByTopic(String uid) {
        return repository.getQuestionsByTopic(uid);
    }
}
