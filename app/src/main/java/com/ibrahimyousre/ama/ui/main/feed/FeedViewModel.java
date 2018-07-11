package com.ibrahimyousre.ama.ui.main.feed;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ibrahimyousre.ama.data.Repository;
import com.ibrahimyousre.ama.data.model.Answer;

import java.util.List;

public class FeedViewModel extends ViewModel {

    private final Repository repository;

    public FeedViewModel() {
        repository = Repository.getInstance();
    }

    public LiveData<List<Answer>> getUserFeed(String uid) {
        return repository.getUserFeed(uid);
    }
}
