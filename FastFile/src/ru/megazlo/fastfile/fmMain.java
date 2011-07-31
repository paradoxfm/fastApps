package ru.megazlo.fastfile;

import ru.megazlo.fastfile.components.filerow.FileList;
import ru.megazlo.fastfile.engine.BaseEngine;
import ru.megazlo.fastfile.util.ActionFactory;
import ru.megazlo.fastfile.util.MenuChecker;
import ru.megazlo.fastfile.util.Sets;
import ru.megazlo.fastfile.util.file.FileTools;
import ru.megazlo.scrollerview.OnScrollFinish;
import ru.megazlo.scrollerview.ScrollerView;
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
import android.widget.TextView;

public class fmMain extends Activity {

	public static fmMain CONTEXT;
	public FileList lsdc;
	private TextView title;
	private ImageView icon;
	public ScrollerView scrv;
	private boolean isToRoot = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		scrv = new ScrollerView(this, null);
		setContentView(scrv);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		initChild();
		Sets.load(getPreferences(0), this);
		if (Sets.dat != null && Sets.dat.size() > 0)
			Sets.restoreLists(this);
		else
			MenuChecker.insertListSD(this);
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
				MenuChecker.remList(fmMain.this.getCurEng().getList());
			}
		});

		scrv.setOnScrollFinish(new OnScrollFinish() {
			@Override
			public void onFinish() {
				title.setText(getCurEng().getTitle());
				icon.setImageDrawable(getCurEng().getIcoProtocol());
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
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
