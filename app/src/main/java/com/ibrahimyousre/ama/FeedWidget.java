package com.ibrahimyousre.ama;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.ibrahimyousre.ama.ui.questions.QuestionActivity;

public class FeedWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent widgetServiceIntent = new Intent(context, WidgetService.class);
        widgetServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        widgetServiceIntent.setData(Uri.parse(widgetServiceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        Intent answerIntent = new Intent(context, QuestionActivity.class);
        answerIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntentTemplate =
                PendingIntent.getActivity(context, 1,
                        answerIntent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_feed);
        views.setRemoteAdapter(R.id.listview, widgetServiceIntent);
        views.setEmptyView(R.id.listview, R.id.emptyview);
        views.setPendingIntentTemplate(R.id.listview, pendingIntentTemplate);
        views.setOnClickPendingIntent(R.id.title,
                PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class), 0));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

