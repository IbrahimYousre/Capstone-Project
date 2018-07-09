package com.ibrahimyousre.ama;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class MyApplication extends Application {

    // don't send crash reports for debug
    static final boolean CRASHLYTICS_ENABLED = !BuildConfig.DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new CrashlyticsTree(this));
    }

    public static class CrashlyticsTree extends Timber.DebugTree {
        private static final String CRASHLYTICS_KEY_PRIORITY = "Priority";
        private static final String CRASHLYTICS_KEY_TAG = "Tag";
        private static final String CRASHLYTICS_KEY_MESSAGE = "Message";

        CrashlyticsTree(Application application) {
            CrashlyticsCore core = new CrashlyticsCore.Builder()
                    .disabled(CRASHLYTICS_ENABLED)
                    .build();
            Fabric.with(application,
                    new Crashlytics.Builder().core(core).build());
        }

        @Override
        protected String createStackElementTag(@NonNull StackTraceElement element) {
            return String.format("C:%s:%s", super.createStackElementTag(element),
                    element.getLineNumber());
        }

        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t) {
            if (BuildConfig.DEBUG) {
                super.log(priority, tag, message, t);
            }
            if (CRASHLYTICS_ENABLED) {
                if (priority < Log.WARN) {
                    return;
                }
                Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority);
                Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag);
                Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message);
                if (t == null) {
                    Crashlytics.logException(new Throwable(message));
                } else {
                    Crashlytics.logException(t);
                }
            }
        }
    }
}
