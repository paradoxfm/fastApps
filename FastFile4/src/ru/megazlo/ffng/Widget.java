package ru.megazlo.ffng;

import java.io.File;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			Widget.updateAppWidget(context, appWidgetManager, appWidgetId, "");
		}
	}

	public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int widgetID, Object file) {
		File fl = new File(file.toString());
		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		updateViews.setTextViewText(R.id.wid_text, fl.getName());
		appWidgetManager.updateAppWidget(widgetID, updateViews);
	}
}
