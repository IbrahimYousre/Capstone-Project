package com.ibrahimyousre.ama.ui.ask;

import android.arch.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.ibrahimyousre.ama.data.Repository;
import com.ibrahimyousre.ama.data.model.Question;

public class QuestionsViewModel extends ViewModel {

    private final Repository repository;

    public QuestionsViewModel() {
        this.repository = Repository.getInstance();
    }

    public Task<Void> addQuestion(Question question) {
        return repository.addQuestion(question);
    }

}
