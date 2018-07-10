package com.ibrahimyousre.ama;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibrahimyousre.ama.data.Repository;
import com.ibrahimyousre.ama.data.model.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ibrahimyousre.ama.data.DatabaseConstants.PATH_ANSWERS;
import static com.ibrahimyousre.ama.data.DatabaseConstants.PATH_USER_TOPICS;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_ANSWER_ID;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_QUESTION_BODY;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_QUESTION_ID;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FeedRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    public static class FeedRemoteViewsFactory implements RemoteViewsFactory {

        Context mContext;
        int widgetId;
        FirebaseAuth.AuthStateListener authListender;
        FirebaseDatabase firebaseDatabase;
        ValueEventListener answersListener;
        ValueEventListener topicListener;
        List<Answer> feed;
        Set<String> userTopics;


        public FeedRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            feed = new ArrayList<>();
            firebaseDatabase = Repository.getInstance().getFirebaseDatabase();
        }

        @Override
        public void onCreate() {
            // can do heavy operations here synchronously
            answersListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    feed.clear();
                    feed = new ArrayList<>((int) dataSnapshot.getChildrenCount());
                    for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                        Answer item = childSnapShot.getValue(Answer.class);
                        item.setUid(childSnapShot.getKey());
                        feed.add(item);
                    }
                    Collections.reverse(feed);
                    AppWidgetManager.getInstance(mContext)
                            .notifyAppWidgetViewDataChanged(widgetId, R.id.listview);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    feed.clear();
                    AppWidgetManager.getInstance(mContext)
                            .notifyAppWidgetViewDataChanged(widgetId, R.id.listview);
                }
            };
            topicListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userTopics = new HashSet<>();
                    for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                        userTopics.add(childSnapShot.getKey());
                    }
                    firebaseDatabase.getReference(PATH_ANSWERS)
                            .addValueEventListener(answersListener);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    feed.clear();
                    AppWidgetManager.getInstance(mContext)
                            .notifyAppWidgetViewDataChanged(widgetId, R.id.listview);
                }
            };
            authListender = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() != null) {
                        String uid = firebaseAuth.getUid();
                        firebaseDatabase.getReference(PATH_USER_TOPICS).child(uid)
                                .addValueEventListener(topicListener);
                    } else {
                        feed.clear();
                        AppWidgetManager.getInstance(mContext)
                                .notifyAppWidgetViewDataChanged(widgetId, R.id.listview);
                    }
                }
            };
            FirebaseAuth.getInstance().addAuthStateListener(authListender);
        }

        @Override
        public void onDestroy() {
            FirebaseAuth.getInstance().removeAuthStateListener(authListender);
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public int getCount() {
            return feed.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            // can do heavy operations here synchronously
            Answer answer = feed.get(position);

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(EXTRA_ANSWER_ID, answer.getUid());
            // Note: check the documentation for the starange behavior of serialzizable extra with
            // fill in intent the extra is not recieved in the activity so I had to send every
            // field on its own :: so ugly
            fillInIntent.putExtra(EXTRA_QUESTION_ID, answer.getQuestionId());
            fillInIntent.putExtra(EXTRA_QUESTION_BODY, answer.getQuestionBody());

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            Date date = new Date(answer.getTimestamp());
            CharSequence niceDateStr = DateUtils.getRelativeTimeSpanString(date.getTime());
            views.setTextViewText(R.id.time_txt, niceDateStr);
            views.setTextViewText(R.id.user_txt,
                    mContext.getString(R.string.widget_user_format, answer.getUserName()));
            views.setTextViewText(R.id.question_txt, answer.getQuestionBody());
            views.setTextViewText(R.id.answer_txt, answer.getText());
            views.setOnClickFillInIntent(R.id.widget_item_root, fillInIntent);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
