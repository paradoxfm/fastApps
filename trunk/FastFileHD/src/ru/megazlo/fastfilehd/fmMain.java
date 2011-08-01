package ru.megazlo.fastfilehd;

import ru.megazlo.fastfilehd.components.list.ListBase;
import ru.megazlo.fastfilehd.components.list.ListSDC;
import ru.megazlo.fastfilehd.util.ActionFactory;
import ru.megazlo.fastfilehd.util.MenuChecker;
import ru.megazlo.fastfilehd.util.Sets;
import ru.megazlo.fastfilehd.util.file.FileTools;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class fmMain extends Activity {

	public static fmMain CONTEXT;
	public ListSDC lsdc;
	private TextView title;
	private ImageView icon;
	private ListBase left, rigth;
	// public ScrollerView scrv;
	private boolean isToRoot = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// -------
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		initChild();
		Sets.load(getPreferences(0), this);
		Sets.restoreLists(this);

		LinearLayout lay = (LinearLayout) findViewById(R.id.LayMain);
		left = (ListBase) lay.getChildAt(0);
		rigth = (ListBase) lay.getChildAt(1);
		CONTEXT = this;
	}

	@Override
	protected void onPause() {
		if (FileTools.M_PLAYER != null && FileTools.M_PLAYER.isPlaying())
			FileTools.M_PLAYER.stop();
		super.onPause();
	}

	private void initChild() {
		title = (TextView) findViewById(R.id.title);
		icon = (ImageView) findViewById(R.id.protocol);

		findViewById(R.id.custom_title_1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Object fl = fmMain.this.getCurrentDir();
				if (fl != null)
					ActionFactory.create(v, fl).show();
			}
		});

		findViewById(R.id.close_tab).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MenuChecker.exitApp(fmMain.this);
			}
		});
	}

	protected Object getCurrentDir() {
		return this.getCurrentList().getCurrentDir();
	}

	@Override
	protected void onStop() {
		Sets.save(getPreferences(0));
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		if (!isToRoot && !getCurrentList().browseUp())
			super.onBackPressed();
		isToRoot = false;
	}

	public void update() {
		getCurrentList().update();
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.isLongPress()) {
			getCurrentList().browseRoot();
			isToRoot = true;
			return false;
		}
		return super.onKeyLongPress(keyCode, event);
	}

	// ------------------------------------------------------ Вызов меню
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuChecker.itemClick(this, item.getItemId());
	}

	// ------------------------------------------------- Управление листами
	public ListBase getCurrentList() {
		return left.isLayoutRequested() ? left : rigth;
		// rigth
		// return null;// (ListBase) scrv.getChildAt(scrv.getDisplayedChild());
	}

	// ------------------------------------------------------ Поисковые
	@Override
	public boolean onSearchRequested() {
		if (getCurrentList().isAccessSearsh())
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
		getCurrentList().search(search);
	}

	public void setCurrentList(ListBase lst) {
		// TODO Auto-generated method stub

	}

}
