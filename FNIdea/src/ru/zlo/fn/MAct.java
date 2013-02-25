package ru.zlo.fn;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.SearchView;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.viewpagerindicator.LinePageIndicator;
import ru.zlo.fn.data.Note;
import ru.zlo.fn.fragments.*;
import ru.zlo.fn.util.MenuChecker;
import ru.zlo.fn.util.Options_;

@EActivity(R.layout.main_view)
@OptionsMenu(R.menu.main_menu)
public class MAct extends Activity implements NoteListFragment.OnListItemChoice, ViewPager.OnPageChangeListener {

	@Pref
	Options_ opt;
	SectionsPagerAdapter pAdapter;
	@ViewById(R.id.pager)
	ViewPager viewPager;
	@ViewById(R.id.indicator)
	LinePageIndicator indicator;
	@FragmentById(R.id.list_frag_left)
	NoteListFragment noteList;
	@FragmentById(R.id.list_frag_right)
	NoteDetailFragment noteDet;

	@AfterViews
	void afterInit() {
		if (viewPager != null) {
			noteList = NoteListFragment_.builder().build();
			noteDet = NoteDetailFragment_.builder().build();
			pAdapter = new SectionsPagerAdapter(getFragmentManager(), noteList, noteDet);
			viewPager.setAdapter(pAdapter);
			indicator.setViewPager(viewPager);
			viewPager.setOnPageChangeListener(this);
		}
		noteList.setOnListItemChoice(this);
		noteDet.setOnSaveChanges(noteList);
	}

	@Override
	protected void onResume() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (opt.fullScreen().get())
			getWindow().setFlags(flg, flg);
		else
			getWindow().clearFlags(flg);
		setRequestedOrientation(opt.orientation().get());
		super.onResume();
	}

	@OptionsItem(R.id.new_note)
	void createNote() {
		noteList.createNote();
	}

	@OptionsItem(R.id.quit)
	void exitApp() {
		noteDet.save();
		System.exit(0);
	}

	@OptionsItem({R.id.appsett, R.id.tutor})
	void otherOptions(MenuItem item) {
		MenuChecker.itemClick(this, item.getItemId());
	}

	@Override
	@OptionsItem(android.R.id.home)
	public void onBackPressed() {
		if (noteList.isSearch()) {
			noteList.clearSearchResult();
			return;
		}
		if (viewPager != null && viewPager.getCurrentItem() == 1) {
			noteDet.save();
			viewPager.setCurrentItem(0);
		} else {
			noteDet.save();
			System.exit(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction()))
			noteList.search(intent.getStringExtra(SearchManager.QUERY));
	}

	@Override
	public void onChoice(Note dat) {
		noteDet.setCurrentNote(dat);
		if (viewPager != null)
			viewPager.setCurrentItem(1);
	}

	@Override
	public void onPageScrolled(int i, float v, int i2) {
		indicator.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPageSelected(int i) {
		indicator.setCurrentItem(i);
	}

	@Override
	public void onPageScrollStateChanged(int i) {
		indicator.setVisibility(View.GONE);
	}
}