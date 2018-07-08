package com.ibrahimyousre.ama.ui.main.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.ibrahimyousre.ama.data.Repository;
import com.ibrahimyousre.ama.data.model.Answer;
import com.ibrahimyousre.ama.data.model.User;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private final Repository repository;

    public ProfileViewModel() {
        repository = Repository.getInstance();
    }

    public LiveData<List<Answer>> getAnswersByUser(String uid) {
        return repository.getAnswersByUser(uid);
    }

    public LiveData<User> getUserById(String uid) {
        return repository.getUserById(uid);
    }

    public Task<Void> updatUser(User user) {
        return repository.updateUser(user);
    }
}
