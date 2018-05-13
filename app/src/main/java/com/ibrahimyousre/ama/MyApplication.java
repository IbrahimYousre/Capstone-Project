package com.ibrahimyousre.ama;

import android.app.Application;
import android.support.annotation.NonNull;

import timber.log.Timber;

public class MyApplication extends Application {
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
    }
}
