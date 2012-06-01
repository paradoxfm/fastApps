package ru.megazlo.fastnote;

import java.io.File;

import ru.megazlo.fastnote.component.NoteAdapter;
import ru.megazlo.fastnote.component.NoteData;
import ru.megazlo.fastnote.component.NoteEditor;
import ru.megazlo.fastnote.component.NoteList;
import ru.megazlo.fastnote.util.FileUtil;
import ru.megazlo.fastnote.util.MenuChecker;
import ru.megazlo.fastnote.util.Sets;
import ru.megazlo.fastnote.util.SqlBase;
import ru.megazlo.scrollerview.OnScrollFinish;
import ru.megazlo.scrollerview.ScrollerView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Mfm extends Activity {
	private static boolean fromFile = false;
	private static boolean isSearch = false;
	public static Mfm I;
	private TextView title;
	private ImageView newnote, logo;
	public ScrollerView scrv;
	public NoteList nlist;
	public NoteEditor nedit;
	private NoteData noteDel;
	private File dirDB;

	private View.OnClickListener saveFile = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Mfm.this.saveFile();
		}
	};

	private View.OnClickListener saveFileBase = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Mfm.this.saveFileBase();
		}
	};

	private View.OnClickListener newNote = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Mfm.this.newNote();
		}
	};

	@Override
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
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (fromFile)
			saveFile();
		else if (nedit.isFromBase()) {
			nedit.setLocked(true);
			if (nedit.isEdited()) {
				nedit.saveText();
				NoteAdapter adp = (NoteAdapter) nlist.getAdapter();
				adp.notifyDataSetChanged();
			}
		}
	}

	@Override
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
	}

	private void doSearchQuery(Intent intent, String stringExtra) {
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
	}

	private void initChild() {
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
	}

	@Override
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
	}

	private void scrollFinish() {
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
	}

	private void saveFile() {
		logo.setImageResource(R.drawable.notepad);
		logo.setOnClickListener(null);
		int msg = nedit.isEdited() ? R.string.f_svd : R.string.fdc;
		if (nedit.isEdited()) {
			FileUtil.saveToFile(nedit.getText());
			nedit.saveText();
		}
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private void saveFileBase() {
		NoteData dat = new NoteData();
		dat.setTitle(nedit.getTitle());
		dat.setWordCount(nedit.getWordCount());
		if (!SqlBase.isConnected())
			Sets.DAT = SqlBase.getList(dirDB, null);
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
	}

	private void newNote() {
		NoteData dat = new NoteData();
		nlist.unsheckAll();
		SqlBase.insertNote(dat);
		NoteAdapter adp = (NoteAdapter) nlist.getAdapter();
		adp.add(dat);
		setEditorText(dat);
	}

	public void setEditorText(NoteData data) {
		if (!pagerContainsView(nedit))
			insertEditor();
		if (data != null) {
			nedit.setText(data);
			scrollRigth();
		} else
			nedit.setText(FileUtil.file_text);
	}

	private boolean pagerContainsView(View v) {
		for (int i = 0; i < scrv.getChildCount(); i++)
			if (scrv.getChildAt(i) == v)
				return true;
		return false;
	}

	private void insertEditor() {
		scrv.addView(nedit);
		scrv.scrollToScreen(scrv.getChildCount() - 1);
	}

	private void insertList() {
		nlist.startLoad(dirDB, null); // загрузка
		if (scrv.getChildAt(0) != nlist)
			scrv.addView(nlist, 0);
		nlist.setVisibility(View.VISIBLE);
	}

	public void scrollRigth() {
		scrv.scrollToScreen(scrv.getChildCount() - 1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuChecker.itemClick(this, item.getItemId());
	}

	public void applyEditorSet() {
		nedit.applyEditorSet();
	}

	public void deleteNote(NoteData note) {
		noteDel = note;
		new AlertDialog.Builder(Mfm.I).setNegativeButton(R.string.cansel, null).setMessage(R.string.del_q)
				.setPositiveButton(R.string.ok, delnote).setIcon(R.drawable.qa_delete).setTitle(R.string.del_ing).show();
	}

	DialogInterface.OnClickListener delnote = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			SqlBase.deleteNote(noteDel.getID());
			NoteAdapter adp = (NoteAdapter) nlist.getAdapter();
			adp.remove(noteDel);
			noteDel = null;
			adp.notifyDataSetChanged();
			Toast.makeText(Mfm.this, R.string.del_ok, Toast.LENGTH_SHORT).show();
		}
	};

	DialogInterface.OnClickListener savefile = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			nedit.saveTextFile();
			Toast.makeText(Mfm.this, R.string.saved, Toast.LENGTH_SHORT).show();
		}
	};
}