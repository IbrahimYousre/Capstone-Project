package com.ibrahimyousre.ama.ui.main.notification;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ibrahimyousre.ama.data.Repository;
import com.ibrahimyousre.ama.data.model.Answer;

import java.util.List;

public class NotificationViewModel extends ViewModel {

    private final Repository repository;

    public NotificationViewModel() {
        repository = Repository.getInstance();
    }

    public LiveData<List<Answer>> getNotificationsForUser(String uid) {
        return repository.getNotificationsForUser(uid);
    }
}
