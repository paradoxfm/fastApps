package ru.zlo.ff;

import java.io.File;

import com.viewpagerindicator.LinePageIndicator;
import ru.zlo.ff.components.RowDataSD;
import ru.zlo.ff.engine.BaseEngine;
import ru.zlo.ff.engine.EngPool;
import ru.zlo.ff.util.MenuChecker;
import ru.zlo.ff.util.Sets;
import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.main_activity)
public class MAct extends FragmentActivity implements OnPageChangeListener {

	SectionsPagerAdapter mSectionsPagerAdapter;
	@ViewById(R.id.pager)
	ViewPager mViewPager;
	@ViewById(R.id.indicator)
	LinePageIndicator indicator;
	public static MAct I;
	public int widgetID = -1;
	private boolean isToRoot = false; 
	private File startFile = null;

	@AfterViews
	void initOnCreate() {
		I = this;
		if (Build.VERSION.SDK_INT >= 11) {
			try {
				Object cc = this.getClass().getMethod("getActionBar").invoke(this);
				cc.getClass().getMethod("setDisplayHomeAsUpEnabled", Boolean.TYPE).invoke(cc, true);
			} catch (Exception ignored) {
			}
		}
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(this);

		Sets.load(getPreferences(0), this);
		// if (Sets.IS_COLORED) {
		// Bitmap bmp = Bitmap.createBitmap(new int[] { Sets.BACK_COLOR }, 1, 1,
		// Config.ARGB_8888);
		// Drawable drw = new BitmapDrawable(bmp);
		// getWindow().setBackgroundDrawable(drw);
		// }
		if (Sets.dat != null && Sets.dat.size() > 0) {
			checkIntentParametrs(getIntent().getExtras());
			if (widgetID != -1)
				MenuChecker.insertList(new RowDataSD());
			else
				Sets.restoreLists(this);
		} else {
			RowDataSD dt = new RowDataSD();
			checkIntentParametrs(getIntent().getExtras());
			if (startFile != null) {
				dt.PATH = startFile;
				startFile = null;
			}
			MenuChecker.insertList(dt);
		}
		I = this;
		//Bind the title indicator to the adapter
		indicator.setViewPager(mViewPager);
	}

	public void scrollToNew() {
		mViewPager.setCurrentItem(mViewPager.getChildCount() - 1);
	}

	public void removeFragment() {
		int i = mViewPager.getCurrentItem();
		mViewPager.setCurrentItem(0);
		FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
		tr.remove(mSectionsPagerAdapter.getItem(i));
		tr.commit();
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

	@Override
	public void onBackPressed() {
		if (!isToRoot && !getCurEng().browseUp())
			super.onBackPressed();
		isToRoot = false;
	}

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

	public BaseEngine getCurEng() {
		return EngPool.Inst().getEngine(mViewPager.getCurrentItem());
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		setTitle(getCurEng().getTitle());
		// getActionBar().setIcon(getCurEng().getIcoProtocol());
	}

	@Override
	public void onPageSelected(int arg0) {
	}

	public void update() {
		getCurEng().update();
	}

	public void configWidget() {
		final Context context = MAct.this;
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		Widget.updateAppWidget(context, appWidgetManager, widgetID, getCurrentDir(), true);
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
		setResult(RESULT_OK, resultValue);
		finish();
		MenuChecker.exitApp(this);
	}

	protected Object getCurrentDir() {
		return getCurEng().getCurrentDir();
	}
}
