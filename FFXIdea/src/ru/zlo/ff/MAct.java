package ru.zlo.ff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;
import com.googlecode.androidannotations.annotations.*;
import com.viewpagerindicator.LinePageIndicator;
import ru.zlo.ff.engine.BaseEngine;
import ru.zlo.ff.engine.EngPool;
import ru.zlo.ff.fragments.FileListFragment;
import ru.zlo.ff.fragments.SectionsPagerAdapter;
import ru.zlo.ff.util.Commander;
import ru.zlo.ff.util.Options;

import java.io.File;

@EActivity(R.layout.main_activity)
@OptionsMenu({R.menu.main_down})
public class MAct extends Activity implements ViewPager.OnPageChangeListener, FileListFragment.OnEngineActivator {

	@Bean
	Options options;
	@Bean
	EngPool pool;
	SectionsPagerAdapter pAdapter;
	@ViewById(R.id.pager)
	ViewPager viewPager;
	@ViewById(R.id.indicator)
	LinePageIndicator indicator;
	@FragmentById(R.id.list_frag_left)
	FileListFragment fragLeft;
	@FragmentById(R.id.list_frag_right)
	FileListFragment fragRight;

	public final static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
	public final static String WIDGET_FILE_PATH = "WidgetFilePath";

	@AfterViews
	void initOnCreate() {
		checkIntentParametrs(getIntent().getExtras());
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
		int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (Options.FULL_SCR)
			getWindow().setFlags(flg, flg);
		else
			getWindow().clearFlags(flg);
		setRequestedOrientation(Options.ORIENT_TYPE);
		super.onResume();
	}

	private void checkIntentParametrs(Bundle extras) {
		if (extras != null && extras.containsKey(Widget.PREF_NAME)) {
			String path = extras.getString(Widget.PREF_NAME);
			if (pool != null && pool.getCurrent() != null) {
				pool.getCurrent().browseCatalog(new File(path));
				Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		checkIntentParametrs(intent.getExtras());
	}

	@Override
	@OptionsItem(android.R.id.home)
	public void onBackPressed() {
		if (!pool.getCurrent().browseUp()) {
			System.exit(0);
			super.onBackPressed();
		}
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
		pool.setCurrentPosition(arg0);
		setTitle(pAdapter.getPageTitle(arg0));
	}

	@Override
	public void activateEngine(BaseEngine engine) {
		if (engine == null)
			return;
		setTitle(engine.getTitle());
		pool.setCurrentEngine(engine);
	}
}
