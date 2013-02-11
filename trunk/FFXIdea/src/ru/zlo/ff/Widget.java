package ru.zlo.ff;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.io.File;

public class Widget extends AppWidgetProvider {

	private static final String PREF_SUFFIX = "Widget_Preference_";
	public static final String PREF_NAME = "WIDGET_PATH";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int appWidgetId : appWidgetIds)
			updateAppWidget(context, appWidgetManager, appWidgetId, null);

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int widgetID, Object file) {
		String setsPath = PREF_SUFFIX + Integer.toString(widgetID);
		if (file != null) {
			SharedPreferences.Editor prf = context.getSharedPreferences(setsPath, Activity.MODE_PRIVATE).edit();
			prf.putString(PREF_NAME, ((File) file).getPath());
			prf.commit();
		}
		SharedPreferences p = context.getSharedPreferences(setsPath, Activity.MODE_PRIVATE);
		String path = p.getString(PREF_NAME, "/");
		File fl = new File(path);

		Intent openFile = new Intent(context, MAct_.class);
		openFile.putExtra(PREF_NAME, fl.getPath());
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, openFile, PendingIntent.FLAG_UPDATE_CURRENT);

		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		updateViews.setTextViewText(R.id.wid_btn, fl.getName());
		updateViews.setOnClickPendingIntent(R.id.wid_btn, pendingIntent);
		appWidgetManager.updateAppWidget(widgetID, updateViews);
	}
}
