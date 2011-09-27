package ru.megazlo.fastnote.component;

import java.io.File;

import ru.megazlo.fastnote.fmMain;
import ru.megazlo.fastnote.util.NotesLoader;
import ru.megazlo.fastnote.util.Sets;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

public class NoteList extends ListView {

	private NoteRow edited;
	private Handler handler;
	private NotesLoader loader;

	public NoteList(Context context) {
		super(context);
		NoteAdapter adp = new NoteAdapter(this.getContext(), Sets.DAT);
		this.setAdapter(adp);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					NoteData dat = (NoteData) msg.obj;
					Sets.DAT.add(dat);
					((NoteAdapter) NoteList.this.getAdapter()).notifyDataSetChanged();
				}
			}
		};
	}

	@Override
	public boolean performItemClick(View view, int position, long id) {
		edited = (NoteRow) view;
		fmMain frm = (fmMain) this.getContext();
		unsheckAll();
		edited.setChecked(true);
		frm.setEditorText(edited.getData());
		frm.scrollRigth();
		return super.performItemClick(view, position, id);
	}

	public void checkByID(int id) {
		unsheckAll();
		NoteAdapter adp = (NoteAdapter) this.getAdapter();
		for (int i = 0; i < adp.getCount(); i++)
			if (adp.getItem(i).getID() == id) {
				adp.getItem(i).setChecked(true);
				break;
			}
	}

	public void unsheckAll() {
		NoteAdapter adp = (NoteAdapter) this.getAdapter();
		for (int i = 0; i < adp.getCount(); i++)
			adp.getItem(i).setChecked(false);
		adp.notifyDataSetChanged();
	}

	public NoteData getCheckedItem() {
		NoteAdapter adp = (NoteAdapter) this.getAdapter();
		for (int i = 0; i < adp.getCount(); i++)
			if (adp.getItem(i).isChecked())
				return adp.getItem(i);
		return null;
	}

	public void updateData(String title, int id, int wCount) {
		NoteData dat = null;
		NoteAdapter adp = (NoteAdapter) this.getAdapter();
		for (int i = 0; i < adp.getCount(); i++)
			if (adp.getItem(i).isChecked()) {
				dat = adp.getItem(i);
				break;
			}
		dat.setTitle(title);
		adp.notifyDataSetChanged();
	}

	public void startLoad(File dirDB, String query) {
		Sets.DAT.clear();
		loader = new NotesLoader(handler, dirDB, query);
		loader.start();
	}

}
