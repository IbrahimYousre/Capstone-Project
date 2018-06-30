package com.ibrahimyousre.ama.ui.answer;

import android.arch.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.ibrahimyousre.ama.data.Repository;
import com.ibrahimyousre.ama.data.model.Answer;

public class AnswerViewModel extends ViewModel {

    private final Repository repository;

    public AnswerViewModel() {
        repository = Repository.getInstance();
    }

    public Task<Void> addAnswer(Answer answer) {
        return repository.addAnswer(answer);
    }
}
