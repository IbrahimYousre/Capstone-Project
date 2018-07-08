package com.ibrahimyousre.ama.ui.questions;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ibrahimyousre.ama.data.Repository;
import com.ibrahimyousre.ama.data.model.Answer;

import java.util.List;

public class QuestionViewModel extends ViewModel {

    private final Repository repository;

    public QuestionViewModel() {
        repository = Repository.getInstance();
    }

    public LiveData<List<Answer>> getAnswersByQuestion(String uid) {
        return repository.getAnswersByQuestion(uid);
    }
}
