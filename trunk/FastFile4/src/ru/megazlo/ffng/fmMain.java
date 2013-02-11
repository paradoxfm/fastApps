package ru.megazlo.ffng;

import java.io.File;

import ru.megazlo.ffng.R;
import ru.megazlo.ffng.components.RowDataSD;
import ru.megazlo.ffng.components.filerow.FileList;
import ru.megazlo.ffng.engine.BaseEngine;
import ru.megazlo.ffng.util.MenuChecker;
import ru.megazlo.ffng.util.Sets;
import ru.megazlo.ffng.util.file.FileTools;
import ru.megazlo.scrollerview.OnScrollFinish;
import ru.megazlo.scrollerview.ScrollerView;
import android.app.Activity;
import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

public class fmMain extends Activity {

	public static fmMain I;
	public FileList lsdc;
	public ScrollerView scrv;
	private boolean isToRoot = false;
	public int widgetID = -1;
	private File startFile = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		scrv = new ScrollerView(this);
		setContentView(scrv);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		initChild();
		Sets.load(getPreferences(0), this);
		if (Sets.IS_COLORED) {
			Bitmap bmp = Bitmap.createBitmap(new int[] { Sets.BACK_COLOR }, 1, 1, Config.ARGB_8888);
			Drawable drw = new BitmapDrawable(bmp);
			getWindow().setBackgroundDrawable(drw);
		}
		if (Sets.dat != null && Sets.dat.size() > 0) {
			checkIntentParametrs(getIntent().getExtras());
			if (widgetID != -1)
				MenuChecker.insertList(this, new RowDataSD());
			else
				Sets.restoreLists(this);
		} else {
			RowDataSD dt = new RowDataSD();
			checkIntentParametrs(getIntent().getExtras());
			if (startFile != null) {
				dt.PATH = startFile;
				startFile = null;
			}
			MenuChecker.insertList(this, dt);
		}
		I = this;
	}

	private void checkIntentParametrs(Bundle extras) {
		if (extras != null) {
			if (extras.containsKey(Widget.PREF_NAME)) {
				String path = extras.get(Widget.PREF_NAME).toString();
				Toast.makeText(this, path, Toast.LENGTH_LONG).show();
				startFile = new File(path);
			} else if (extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
				widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
				setResult(RESULT_CANCELED);
				invalidateOptionsMenu();
				if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID)
					finish();
			}
		}
	}

	public void configWidget() {
		final Context context = fmMain.this;
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		Widget.updateAppWidget(context, appWidgetManager, widgetID, getCurrentDir(), true);
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
		setResult(RESULT_OK, resultValue);
		finish();
		MenuChecker.exitApp(this);
	}

	@Override
	protected void onPause() {
		if (FileTools.M_PLAYER != null && FileTools.M_PLAYER.isPlaying())
			FileTools.M_PLAYER.stop();
		for (int i = 0; i < scrv.getChildCount(); i++)
			((FileList) scrv.getChildAt(i)).getEngine().stopThreads();
		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currView", scrv.getDisplayedChild());
		int[] poss = new int[scrv.getChildCount()];
		int[] ofss = new int[scrv.getChildCount()];
		for (int i = 0; i < ofss.length; i++) {
			FileList fl = (FileList) scrv.getChildAt(0);
			if (fl.getChildCount() > 0) {
				poss[i] = fl.getFirstVisiblePosition();
				ofss[i] = fl.getChildAt(0).getTop();
			} else
				poss[i] = -1;
		}
		outState.putIntArray("poss", poss);
		outState.putIntArray("ofss", ofss);
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		int[] poss = state.getIntArray("poss");
		int[] ofss = state.getIntArray("ofss");
		for (int i = 0; i < ofss.length; i++)
			if (poss[i] != -1)
				((FileList) scrv.getChildAt(0)).setSelectionFromTop(poss[i], ofss[i]);
	}

	private void initChild() {
		scrv.setOnScrollFinish(new OnScrollFinish() {
			@Override
			public void onFinish() {
				fmMain.this.setTitle(getCurEng().getTitle());
				fmMain.this.getActionBar().setIcon(getCurEng().getIcoProtocol());
			}
		});
	}

	protected Object getCurrentDir() {
		return getCurEng().getCurrentDir();
	}

	@Override
	protected void onStop() {
		Sets.save(getPreferences(0));
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		if (!isToRoot && !getCurEng().browseUp())
			super.onBackPressed();
		isToRoot = false;
	}

	public void update() {
		getCurEng().update();
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.isLongPress()) {
			getCurEng().browseRoot();
			isToRoot = true;
			return false;
		}
		return super.onKeyLongPress(keyCode, event);
	}

	// ------------------------------------------------------ Вызов меню
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (widgetID == -1) {
			getMenuInflater().inflate(R.menu.actionbar, menu);
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
			getMenuInflater().inflate(R.menu.main_down, menu);
		} else {
			getMenuInflater().inflate(R.menu.actionbarw, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return false;
		}
		return MenuChecker.itemClick(this, item.getItemId());
	}

	// ------------------------------------------------- Управление листами
	public BaseEngine getCurEng() {
		return ((FileList) scrv.getChildAt(scrv.getDisplayedChild())).getEngine();
	}

	// ------------------------------------------------------ Поисковые
	@Override
	public void onNewIntent(final Intent newIntent) {
		super.onNewIntent(newIntent);
		checkIntentParametrs(newIntent.getExtras());
		if (startFile != null) {
			RowDataSD dt = new RowDataSD();
			dt.PATH = startFile;
			MenuChecker.insertList(this, dt);
			startFile = null;
		}
		if (Intent.ACTION_SEARCH.equals(newIntent.getAction()) && getCurEng().isAllowSearsh())
			doSearchQuery(newIntent, newIntent.getStringExtra(SearchManager.QUERY));
	}

	private void doSearchQuery(Intent queryIntent, String search) {
		getCurEng().search(search);
	}

}