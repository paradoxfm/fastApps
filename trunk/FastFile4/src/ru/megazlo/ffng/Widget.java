package ru.megazlo.ffng;

import java.io.File;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {

	private static final String PREF_SUFFIX = "Widget_Preference_";
	public static final String PREF_NAME = "WIDGET_PATH";
	public static final String PREF_EMP_NAME = "Empt";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// super.onUpdate(context, appWidgetManager, appWidgetIds);
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			SharedPreferences prf = context.getSharedPreferences(PREF_SUFFIX + Integer.toString(appWidgetId),
					Activity.MODE_PRIVATE);
			Widget.updateAppWidget(context, appWidgetManager, appWidgetId, prf.getString(PREF_NAME, PREF_EMP_NAME), false);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int widgetID, Object file,
			Boolean init) {
		File fl = new File(file.toString());
		if (!file.toString().equals(PREF_EMP_NAME)) {
			if (init) {
				SharedPreferences.Editor prf = context.getSharedPreferences(PREF_SUFFIX + Integer.toString(widgetID),
						Activity.MODE_PRIVATE).edit();
				prf.putString(PREF_NAME, file.toString());
				prf.commit();
			}
			Intent openFile = new Intent(context, fmMain.class);
			openFile.putExtra(PREF_NAME, fl.getAbsolutePath());
			PendingIntent pendingIntent = PendingIntent.getActivity(context, widgetID, openFile, 0);

			RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
			updateViews.setTextViewText(R.id.wid_text, fl.getName());
			updateViews.setOnClickPendingIntent(R.id.wid_icon, pendingIntent);

			appWidgetManager.updateAppWidget(widgetID, updateViews);
		}
	}
}
