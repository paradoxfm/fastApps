package ru.megazlo.ffng;

import ru.megazlo.ffng.R;
import ru.megazlo.ffng.components.RowDataSD;
import ru.megazlo.ffng.components.filerow.FileList;
import ru.megazlo.ffng.engine.BaseEngine;
import ru.megazlo.ffng.util.MenuChecker;
import ru.megazlo.ffng.util.Sets;
import ru.megazlo.ffng.util.file.FileTools;
import ru.megazlo.scrollerview.OnScrollFinish;
import ru.megazlo.scrollerview.ScrollerView;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.widget.SearchView;

public class fmMain extends Activity {

	public static fmMain CONTEXT;
	public FileList lsdc;
	public ScrollerView scrv;
	private boolean isToRoot = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(scrv = new ScrollerView(this));
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		initChild();
		Sets.load(getPreferences(0), this);
		if (Sets.IS_COLORED) {
			Bitmap bmp = Bitmap.createBitmap(new int[] { Sets.BACK_COLOR }, 1, 1, Config.ARGB_8888);
			Drawable drw = new BitmapDrawable(bmp);
			getWindow().setBackgroundDrawable(drw);
		}
		if (Sets.dat != null && Sets.dat.size() > 0)
			Sets.restoreLists(this);
		else
			MenuChecker.insertList(this, new RowDataSD());
		CONTEXT = this;
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
		getMenuInflater().inflate(R.menu.srh_menu, menu);
		//SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		getMenuInflater().inflate(R.menu.top, menu);

		getMenuInflater().inflate(R.menu.main, menu);
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
	public boolean onSearchRequested() {
		if (getCurEng().isAllowSearsh())
			return super.onSearchRequested();
		return false;
	}

	@Override
	public void onNewIntent(final Intent newIntent) {
		super.onNewIntent(newIntent);
		if (Intent.ACTION_SEARCH.equals(newIntent.getAction()))
			doSearchQuery(newIntent, newIntent.getStringExtra(SearchManager.QUERY));
	}

	private void doSearchQuery(Intent queryIntent, String search) {
		getCurEng().search(search);
	}

}
