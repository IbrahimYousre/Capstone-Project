package com.ibrahimyousre.ama;

import android.app.Application;
import android.support.annotation.NonNull;

import com.ibrahimyousre.ama.data.model.User;

import timber.log.Timber;

public class MyApplication extends Application {

    private User currentUser;

    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(@NonNull StackTraceElement element) {
                    return String.format("C:%s:%s", super.createStackElementTag(element),
                            element.getLineNumber());
                }
            });
        }
        sInstance = this;
    }

    public static User getCurrentUser() {
        return sInstance.currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Timber.d("user %s successfully loged int", currentUser.getName());
        sInstance.currentUser = currentUser;
    }
}
