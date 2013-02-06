package ru.zlo.ff;

import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;
import com.googlecode.androidannotations.annotations.*;
import com.viewpagerindicator.LinePageIndicator;
import ru.zlo.ff.components.RowDataSD;
import ru.zlo.ff.engine.BaseEngine;
import ru.zlo.ff.engine.EngPool;
import ru.zlo.ff.fragments.SectionsPagerAdapter;
import ru.zlo.ff.util.Commander;
import ru.zlo.ff.util.Options;
import ru.zlo.ff.util.Sets;

import java.io.File;

@EActivity(R.layout.main_activity)
@OptionsMenu({R.menu.actionbar, R.menu.main_down})
public class MAct extends FragmentActivity implements ViewPager.OnPageChangeListener {

	@Bean
	Options options;
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
		customiseUI();
		if (Sets.dat != null && Sets.dat.size() > 0) {
			checkIntentParametrs(getIntent().getExtras());
			if (widgetID != -1)
				Commander.insertList(new RowDataSD());
			else
				Sets.restoreLists(this);
		} else {
			RowDataSD dt = new RowDataSD();
			checkIntentParametrs(getIntent().getExtras());
			if (startFile != null) {
				dt.PATH = startFile;
				startFile = null;
			}
			Commander.createPanes();
		}
	}

	private void customiseUI() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
		indicator.setViewPager(mViewPager);
		if (Options.FULL_SCR) {
			int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setFlags(flg, flg);
		}
		setRequestedOrientation(Options.ORIENT_TYPE);
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
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		} else {
			getMenuInflater().inflate(R.menu.actionbarw, menu);
		}
		return true;
	}

	@OptionsItem(android.R.id.home)
	boolean menuHome() {
		onBackPressed();
		return false;
	}

	@OptionsItem(R.id.menu_set_widget_path)
	boolean menuWidget() {
		configWidget();
		return true;
	}

	@OptionsItem({R.id.appsett2, R.id.appsett, R.id.tutor2, R.id.tutor, R.id.quit2, R.id.quit})
	boolean menuOther(MenuItem item) {
		return Commander.itemClick(this, item.getItemId());
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
		Commander.exitApp(this);
	}

	protected Object getCurrentDir() {
		return getCurEng().getCurrentDir();
	}
}
