package ru.zlo.fn;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.googlecode.androidannotations.annotations.*;
import com.viewpagerindicator.LinePageIndicator;
import ru.zlo.fn.data.Note;
import ru.zlo.fn.fragments.*;
import ru.zlo.fn.util.FileUtil;
import ru.zlo.fn.util.MenuChecker;
import ru.zlo.fn.util.Options;

@EActivity(R.layout.main_view)
@OptionsMenu(R.menu.main_menu)
public class MAct extends Activity implements NoteListFragment.OnListItemChoice, NoteDetailFragment.OnSaveChanges {

	@Bean
	Options options;
	SectionsPagerAdapter pAdapter;
	@ViewById(R.id.pager)
	ViewPager viewPager;
	@ViewById(R.id.indicator)
	LinePageIndicator indicator;
	@FragmentById(R.id.list_frag_left)
	NoteListFragment noteList;
	@FragmentById(R.id.list_frag_right)
	NoteDetailFragment noteDet;

	private static boolean fromFile = false;
	private static boolean isSearch = false;
	public static MAct I;

	@AfterViews
	void afterInit() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (viewPager != null) {
			noteList = NoteListFragment_.builder().build();
			noteDet = NoteDetailFragment_.builder().build();
			pAdapter = new SectionsPagerAdapter(getFragmentManager(), noteList, noteDet);
			viewPager.setAdapter(pAdapter);
			indicator.setViewPager(viewPager);
		}
		noteList.setOnListItemChoice(null);
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

	/*@Override
	public void onCreate(Bundle savedInstanceState) {
		fromFile = false;
		super.onCreate(savedInstanceState);
		Sets.load(getPreferences(0), this);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(scrv = new ScrollerView(this));
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		dirDB = getExternalFilesDir(null);
		initChild();
		I = this;
		if (!(fromFile || FileUtil.openText(getIntent()))) {
			insertList();
			NoteData dt = nlist.getCheckedItem();
			if (dt != null)
				setEditorText(dt);
			scrv.removeView(nedit);
		} else {
			setEditorText(null);
			scrv.removeView(nlist);
			fromFile = true;
		}
	}*/

	/*@Override
	protected void onPause() {
		super.onPause();
	}*/

	/*@Override
	protected void onNewIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction()))
			doSearchQuery(intent, intent.getStringExtra(SearchManager.QUERY));
		if (FileUtil.openText(intent)) {
			setEditorText(null);
			fromFile = true;
			if (pagerContainsView(nlist)) {
				scrv.setCurrentScreen(0);
				scrv.removeView(nlist);
			}
		}
		super.onNewIntent(intent);
	}*/

	/*private void doSearchQuery(Intent intent, String stringExtra) {
		View v = scrv.getChildAt(scrv.getDisplayedChild());
		if (nlist == v) {
			nlist.startLoad(dirDB, stringExtra); // загрузка
			isSearch = true;
		} else if (nedit == v) {
			if (fromFile)
				saveFile();
			else
				nedit.setLocked(false);
			nedit.search(stringExtra.toLowerCase());
		}
	}*/

	/*private void initChild() {
		title = (TextView) findViewById(R.id.title);
		newnote = (ImageView) findViewById(R.id.newnote);
		newnote.setOnClickListener(newNote);
		logo = (ImageView) findViewById(R.id.protocol);
		nlist = new NoteList(this);
		scrv.addView(nlist);
		nedit = new NoteEditor(this);
		scrv.addView(nedit);
		scrv.setOnScrollFinish(new OnScrollFinish() {
			@Override
			public void onFinish() {
				scrollFinish();
			}
		});
	}*/

	/*@Override
	public void onBackPressed() {
		if (nedit.isEdited() && fromFile) {
			new AlertDialog.Builder(this).setPositiveButton(R.string.ok, savefile).setNegativeButton(R.string.cansel, null)
					.setMessage(R.string.ischng).create().show();
		}
		if (scrv.getChildCount() > 1 && scrv.getDisplayedChild() == 1)
			scrv.scrollToScreen(0);
		else if (isSearch) {
			nlist.startLoad(dirDB, null);
			isSearch = false;
		} else
			super.onBackPressed();
	}*/

	/*private void scrollFinish() {
		View vc = scrv.getChildAt(scrv.getDisplayedChild());
		if (vc == nlist) {
			if (nedit.isEdited() && nedit.isFromBase()) {
				nedit.saveText();
				NoteAdapter adp = (NoteAdapter) nlist.getAdapter();
				adp.notifyDataSetChanged();
			}
			logo.setImageResource(R.drawable.notepad);
			logo.setOnClickListener(null);
			newnote.setImageResource(R.drawable.plus_64);
			newnote.setVisibility(View.VISIBLE);
			newnote.setOnClickListener(newNote);
			title.setText(R.string.app_name);
		} else if (vc == nedit) {
			Boolean flg = nedit.isFromBase();
			newnote.setVisibility(flg ? View.GONE : View.VISIBLE);
			if (!flg) {
				newnote.setImageResource(R.drawable.save);
				newnote.setOnClickListener(saveFile);
			}
			logo.setImageResource(flg ? R.drawable.notepad : R.drawable.db_add);
			logo.setOnClickListener(flg ? null : saveFileBase);
			title.setText(nedit.getTitle());
		}
	}*/

	/*private void saveFileBase() {
		NoteData dat = new NoteData();
		dat.setTitle(nedit.getTitle());
		dat.setWordCount(nedit.getWordCount());
		if (!SqlBase.isConnected())
			//Sets.DAT = SqlBase.getList(dirDB, null);
			SqlBase.insertNote(dat);
		SqlBase.updateData(nedit.getText(), dat);
		fromFile = false;
		setEditorText(dat);
		insertList();
		nlist.checkByID(dat.getID());
		nedit.setLocked(true);
		logo.setImageResource(R.drawable.notepad);
		logo.setOnClickListener(null);
		Toast.makeText(this, R.string.save_base, Toast.LENGTH_SHORT).show();
	}*/

	/*private void newNote() {
		NoteData dat = new NoteData();
		nlist.unsheckAll();
		SqlBase.insertNote(dat);
		NoteAdapter adp = (NoteAdapter) nlist.getAdapter();
		adp.add(dat);
		setEditorText(dat);
	}*/

	/*public void setEditorText(NoteData data) {
		if (!pagerContainsView(nedit))
			insertEditor();
		if (data != null) {
			nedit.setText(data);
			scrollRigth();
		} else
			nedit.setText(FileUtil.file_text);
	}*/

	private boolean pagerContainsView(View v) {
		/*for (int i = 0; i < scrv.getChildCount(); i++)
			if (scrv.getChildAt(i) == v)
				return true;*/
		return false;
	}

	private void insertEditor() {
		/*scrv.addView(nedit);
		scrv.scrollToScreen(scrv.getChildCount() - 1);*/
	}

	private void insertList() {
		/*nlist.startLoad(dirDB, null); // загрузка
		if (scrv.getChildAt(0) != nlist)
			scrv.addView(nlist, 0);
		nlist.setVisibility(View.VISIBLE);*/
	}

	public void scrollRigth() {
		/*scrv.scrollToScreen(scrv.getChildCount() - 1);*/
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuChecker.itemClick(this, item.getItemId());
	}

	public void applyEditorSet() {
		//nedit.applyEditorSet();
	}

	public void deleteNote(Note note) {
		//noteDel = note;
		/*new AlertDialog.Builder(Mfm.I).setNegativeButton(R.string.cansel, null).setMessage(R.string.del_q)
				.setPositiveButton(R.string.ok, delnote).setIcon(R.drawable.qa_delete).setTitle(R.string.del_ing).show();*/
	}

	DialogInterface.OnClickListener delnote = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			/*SqlBase.deleteNote(noteDel.getID());
			NoteAdapter adp = (NoteAdapter) nlist.getAdapter();
			adp.remove(noteDel);
			noteDel = null;
			adp.notifyDataSetChanged();
			Toast.makeText(Mfm.this, R.string.del_ok, Toast.LENGTH_SHORT).show();*/
		}
	};

	@Override
	public void onChoice(Note dat) {
		noteDet.setCurrentNote(dat);
		if (viewPager != null)
			viewPager.setCurrentItem(1);
	}

	@Override
	public void saveChanges() {
		noteList.refreshList();
	}
}