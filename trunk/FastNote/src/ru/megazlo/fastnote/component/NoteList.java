package ru.megazlo.fastnote.component;

import java.io.File;

import ru.megazlo.fastnote.fmMain;
import ru.megazlo.fastnote.util.Sets;
import ru.megazlo.fastnote.util.SqlBase;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

public class NoteList extends ListView {

	private NoteRow edited;

	public NoteList(Context context) {
		super(context);
	}

	public void loadData(File ext) {
		if (Sets.DAT == null)
			Sets.DAT = SqlBase.getList(ext);
		NoteAdapter adp = new NoteAdapter(this.getContext(), Sets.DAT);
		this.setAdapter(adp);
	}

	@Override
	public boolean performItemClick(View view, int position, long id) {
		edited = (NoteRow) view;
		fmMain frm = (fmMain) this.getContext();
		if (!edited.isChecked()) {
			unsheckAll();
			edited.setChecked(true);
			frm.setEditorText(edited.getData());
		} else {
			frm.scrollRigth();
		}
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

	private void unsheckAll() {
		for (int i = 0; i < this.getChildCount(); i++) {
			NoteRow row = (NoteRow) this.getChildAt(i);
			row.setChecked(false);
		}
		NoteAdapter adp = (NoteAdapter) this.getAdapter();
		for (int i = 0; i < adp.getCount(); i++)
			adp.getItem(i).setChecked(false);
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

}
