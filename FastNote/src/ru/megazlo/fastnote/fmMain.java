package ru.megazlo.fastnote;

import ru.megazlo.fastnote.component.NoteAdapter;
import ru.megazlo.fastnote.component.NoteData;
import ru.megazlo.fastnote.component.NoteEditor;
import ru.megazlo.fastnote.component.NoteList;
import ru.megazlo.fastnote.util.FileUtil;
import ru.megazlo.fastnote.util.MenuChecker;
import ru.megazlo.fastnote.util.Sets;
import ru.megazlo.fastnote.util.SqlBase;
import ru.megazlo.fastnote.R;
import ru.megazlo.pager.HorizontalPager;
import ru.megazlo.pager.PagerControl;
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

public class fmMain extends Activity {
	private static boolean fromFile = false;
	public static fmMain CONTEXT;
	private TextView title;
	private ImageView newnote, logo;
	public HorizontalPager pager;
	public PagerControl control;
	public NoteList nlist;
	public NoteEditor nedit;
	private NoteData noteDel;

	private View.OnClickListener lockNote = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			fmMain.this.lockNote();
		}
	};
	private View.OnClickListener unlockNote = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			fmMain.this.unlockNote();
		}
	};
	private View.OnClickListener saveFile = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			fmMain.this.saveFile();
		}
	};
	private View.OnClickListener unlockFile = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			fmMain.this.unlockFile();
		}
	};
	private View.OnClickListener saveFileBase = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			fmMain.this.saveFileBase();
		}
	};
	private View.OnClickListener newNote = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			fmMain.this.newNote();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		fromFile = false;
		super.onCreate(savedInstanceState);
		Sets.load(getPreferences(0), this);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		initChild();
		CONTEXT = this;
		if (fromFile || FileUtil.openText(getIntent())) {
			setEditorText(null);
			fromFile = true;
		} else {
			insertList();
			NoteData dt = nlist.getCheckedItem();
			if (dt != null)
				setEditorText(dt);
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
				control.setNumPages(pager.getChildCount() - 1);
				pager.removeView(nlist);
				pager.setCurrentPage(pager.getChildCount() - 1);
			}
		}
		super.onNewIntent(intent);
	}

	private void doSearchQuery(Intent intent, String stringExtra) {
		Toast.makeText(this, R.string.com_son, Toast.LENGTH_SHORT).show();
	}

	private void initChild() {
		title = (TextView) findViewById(R.id.title);
		newnote = (ImageView) findViewById(R.id.newnote);
		newnote.setOnClickListener(newNote);
		logo = (ImageView) findViewById(R.id.protocol);
		control = (PagerControl) findViewById(R.id.pageind);
		pager = (HorizontalPager) findViewById(R.id.pager);
		nlist = new NoteList(this);
		nedit = new NoteEditor(this);
		pager.addOnScrollListener(new HorizontalPager.OnScrollListener() {
			public void onScroll(int scrollX) {
				float scale = (float) (pager.getPageWidth() * pager.getChildCount()) / (float) control.getWidth();
				control.setPosition((int) (scrollX / scale));
			}

			public void onViewScrollFinished(int currentPage) {
				scrollFinish(currentPage);
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (nedit.isEdited() && fromFile) {
			new AlertDialog.Builder(this).setPositiveButton(R.string.ok, savefile).setNegativeButton(R.string.cansel, null)
					.setMessage(R.string.ischng).create().show();
		}
		if (pager.getChildCount() > 1 && control.getCurrentPage() == 1)
			pager.scrollLeft();
		else
			super.onBackPressed();
	}

	private void scrollFinish(int page) {
		control.setCurrentPage(page);
		if (pager.getChildAt(control.getCurrentPage()) == nlist) {
			if (nedit.isEdited() && nedit.isFromBase()) {
				nedit.saveText();
				NoteAdapter adp = (NoteAdapter) nlist.getAdapter();
				adp.notifyDataSetChanged();
			}
			// --------------- значки события заголовок
			logo.setImageResource(R.drawable.notepad);
			newnote.setImageResource(R.drawable.plus_64);
			logo.setOnClickListener(null);
			newnote.setOnClickListener(newNote);
			title.setText(R.string.app_name);
			// --------------- значки события заголовок
		} else if (pager.getChildAt(control.getCurrentPage()) == nedit) {
			if (nedit.isFromBase()) {
				logo.setImageResource(R.drawable.notepad);
				logo.setOnClickListener(null);
				newnote.setImageResource(nedit.isLocked() ? R.drawable.lock : R.drawable.unlock);
				newnote.setOnClickListener(nedit.isLocked() ? unlockNote : lockNote);
			} else {
				logo.setImageResource(nedit.isLocked() ? R.drawable.notepad : R.drawable.db_add);
				logo.setOnClickListener(nedit.isLocked() ? null : saveFileBase);
				newnote.setImageResource(nedit.isLocked() ? R.drawable.lock : R.drawable.save);
				newnote.setOnClickListener(nedit.isLocked() ? unlockFile : saveFile);
			}
			title.setText(nedit.getTitle());
		}
	}

	private void lockNote() {
		nedit.setLocked(true);
		newnote.setImageResource(R.drawable.lock);
		newnote.setOnClickListener(unlockNote);
	}

	private void unlockNote() {
		nedit.setLocked(false);
		newnote.setImageResource(R.drawable.unlock);
		newnote.setOnClickListener(lockNote);
	}

	private void saveFile() {
		logo.setImageResource(R.drawable.notepad);
		logo.setOnClickListener(null);
		newnote.setImageResource(R.drawable.lock);
		newnote.setOnClickListener(unlockFile);
		int msg = nedit.isEdited() ? R.string.f_svd : R.string.fdc;
		if (nedit.isEdited()) {
			FileUtil.saveToFile(nedit.getText());
			nedit.saveText();
		}
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private void unlockFile() {
		logo.setImageResource(R.drawable.db_add);
		newnote.setImageResource(R.drawable.save);
		logo.setOnClickListener(saveFileBase);
		newnote.setOnClickListener(saveFile);
		nedit.setLocked(false);
	}

	private void saveFileBase() {
		NoteData dat = new NoteData();
		dat.setTitle(nedit.getTitle());
		dat.setWordCount(nedit.getWordCount());
		if (!SqlBase.isConnected())
			Sets.DAT = SqlBase.getList(getExternalFilesDir(null));
		SqlBase.insertNote(dat);
		SqlBase.updateData(nedit.getText(), dat);
		fromFile = false;
		setEditorText(dat);
		insertList();
		NoteAdapter adp = (NoteAdapter) nlist.getAdapter();
		adp.add(dat);
		adp.notifyDataSetChanged();
		nlist.checkByID(dat.getID());
		nedit.setLocked(true);
		logo.setImageResource(R.drawable.notepad);
		logo.setOnClickListener(null);
		newnote.setImageResource(R.drawable.lock);
		newnote.setOnClickListener(unlockNote);
		Toast.makeText(this, R.string.save_base, Toast.LENGTH_SHORT).show();
	}

	private void newNote() {
		NoteData dat = new NoteData();
		SqlBase.insertNote(dat);
		NoteAdapter adp = (NoteAdapter) nlist.getAdapter();
		adp.add(dat);
		adp.notifyDataSetChanged();
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
		for (int i = 0; i < pager.getChildCount(); i++)
			if (pager.getChildAt(i) == v)
				return true;
		return false;
	}

	private void insertEditor() {
		pager.addView(nedit);
		control.setNumPages(pager.getChildCount());
	}

	private void insertList() {
		nlist.loadData(getExternalFilesDir(null)); // загрузка
		pager.addView(nlist, 0);
		control.setNumPages(pager.getChildCount());
	}

	public void scrollRigth() {
		pager.snapToPage(pager.getChildCount() - 1);
		pager.setCurrentPage(1);
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
		new AlertDialog.Builder(fmMain.CONTEXT).setNegativeButton(R.string.cansel, null).setMessage(R.string.del_q)
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
			Toast.makeText(fmMain.this, R.string.del_ok, Toast.LENGTH_SHORT).show();
		}
	};

	DialogInterface.OnClickListener savefile = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			nedit.saveTextFile();
			Toast.makeText(fmMain.this, R.string.saved, Toast.LENGTH_SHORT).show();
		}
	};
}