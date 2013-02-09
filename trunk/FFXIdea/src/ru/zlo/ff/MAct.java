package ru.zlo.ff;

import android.app.Activity;
import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;
import com.googlecode.androidannotations.annotations.*;
import com.viewpagerindicator.LinePageIndicator;
import ru.zlo.ff.engine.BaseEngine;
import ru.zlo.ff.engine.EngPool;
import ru.zlo.ff.fragments.FileListFragment;
import ru.zlo.ff.fragments.SectionsPagerAdapter;
import ru.zlo.ff.util.Commander;
import ru.zlo.ff.util.Options;

@EActivity(R.layout.main_activity)
@OptionsMenu({R.menu.actionbar})
public class MAct extends Activity implements ViewPager.OnPageChangeListener, FileListFragment.OnEngineActivator {

	@Bean
	Options options;
	SectionsPagerAdapter pAdapter;
	@ViewById(R.id.pager)
	ViewPager viewPager;
	@ViewById(R.id.indicator)
	LinePageIndicator indicator;
	@FragmentById(R.id.list_frag_left)
	FileListFragment fragLeft;
	@FragmentById(R.id.list_frag_right)
	FileListFragment fragRight;
	public int widgetID = -1;
	private boolean isToRoot = false;
	private String startFile = null;

	@AfterViews
	void initOnCreate() {
		customiseUI();
		/*if (Sets.dat != null && Sets.dat.size() > 0) {
			checkIntentParametrs(getIntent().getExtras());
			if (widgetID != -1)
				Commander.insertList(new RowDataSD());
			else
				Sets.restoreLists(this);
		} else */
		checkIntentParametrs(getIntent().getExtras());
		//Commander.createPanes(this, startFile, left, right);
		startFile = null;
	}

	private void customiseUI() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (viewPager != null) {
			pAdapter = new SectionsPagerAdapter(getFragmentManager());
			viewPager.setAdapter(pAdapter);
			indicator.setViewPager(viewPager);
			viewPager.setOnPageChangeListener(this);
		} else {
			fragLeft.setOnEngineActivator(this);
			fragRight.setOnEngineActivator(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (Options.FULL_SCR)
			getWindow().setFlags(flg, flg);
		else
			getWindow().clearFlags(flg);
		setRequestedOrientation(Options.ORIENT_TYPE);
	}

	private void checkIntentParametrs(Bundle extras) {
		if (extras != null) {
			if (extras.containsKey(Widget.PREF_NAME)) {
				String path = extras.get(Widget.PREF_NAME).toString();
				Toast.makeText(this, path, Toast.LENGTH_LONG).show();
				startFile = path;
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
		if (!isToRoot && !EngPool.Inst().getCurrent().browseUp())
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

	@OptionsItem({R.id.appsett, R.id.tutor, R.id.quit})
	boolean menuOther(MenuItem item) {
		return Commander.itemClick(this, item.getItemId());
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		indicator.setCurrentItem(arg0);
		EngPool.Inst().setCurrentPosition(arg0);
		setTitle(pAdapter.getPageTitle(arg0));
	}

	public void configWidget() {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		Widget.updateAppWidget(this, appWidgetManager, widgetID, EngPool.Inst().getCurrent().getCurrentDir(), true);
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
		setResult(RESULT_OK, resultValue);
		finish();
		Commander.exitApp();
	}

	@Override
	public void activateEngine(BaseEngine engine) {
		if (engine == null)
			return;
		setTitle(engine.getTitle());
		EngPool.Inst().setCurrentEngine(engine);
	}
}
