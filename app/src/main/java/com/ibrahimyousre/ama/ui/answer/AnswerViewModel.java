package com.ibrahimyousre.ama.ui.answer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.ibrahimyousre.ama.data.Repository;
import com.ibrahimyousre.ama.data.model.Answer;
import com.ibrahimyousre.ama.data.model.User;

public class AnswerViewModel extends ViewModel {

    private final Repository repository;

    public AnswerViewModel() {
        repository = Repository.getInstance();
    }

    public Task<Void> addAnswer(Answer answer) {
        return repository.addAnswer(answer);
    }

    public LiveData<User> getUser(String uid) {
        return repository.getUserById(uid);
    }
}
