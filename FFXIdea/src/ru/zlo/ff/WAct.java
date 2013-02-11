package ru.zlo.ff;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import com.googlecode.androidannotations.annotations.*;
import ru.zlo.ff.engine.EngPool;
import ru.zlo.ff.util.Options;

@EActivity(R.layout.widget_activity)
@OptionsMenu(R.menu.widget_chiose)
public class WAct extends Activity {

	@Bean
	Options options;
	public int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;

	@AfterViews
	void initOnCreate() {
		checkIntentParametrs(getIntent().getExtras());
	}

	@OptionsItem(R.id.menu_set_widget_path)
	void menuWidget() {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		Widget.updateAppWidget(this, appWidgetManager, widgetID, EngPool.Inst().getCurrent().getCurrentDir());
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
		setResult(RESULT_OK, resultValue);
		finish();
	}

	private void checkIntentParametrs(Bundle extras) {
		if (extras != null && extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
			widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			setResult(RESULT_CANCELED);
			invalidateOptionsMenu();
		}
		if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID)
			finish();
	}
}
