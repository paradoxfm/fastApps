package ru.zlo.fn.fragments;

import android.app.ListFragment;
import com.googlecode.androidannotations.annotations.*;
import ru.zlo.fn.R;
import ru.zlo.fn.component.NoteAdapter;
import ru.zlo.fn.data.Note;
import ru.zlo.fn.data.SqlHelper;

import java.util.List;

@EFragment(R.layout.notes_list)
public class NoteListFragment extends ListFragment {

	@Bean
	NoteAdapter adapter;
	@Bean
	SqlHelper helper;
	OnListItemChoice choicer;

	public interface OnListItemChoice {
		void onChoice(Note dat);
	}

	@AfterViews
	void afterInit() {
		setListAdapter(adapter);
		preloadNotes();
	}

	@Background
	void preloadNotes() {
		List<Note> notes = helper.getAllNoteLists();
		applyLoadedNotes(notes);
	}

	@UiThread
	void applyLoadedNotes(List<Note> notes) {
		adapter.setListItems(notes);
		adapter.notifyDataSetChanged();
	}

	@ItemClick
	void noteListItemClicked(int pos) {
		Note dat = adapter.getItem(pos);
		unsheckAll();
		dat.setChecked(true);
		adapter.notifyDataSetChanged();
		if (choicer != null)
			choicer.onChoice(dat);
	}

	public void setOnListItemChoice(OnListItemChoice choice) {
		choicer = choice;
	}

	protected void unsheckAll() {
		for (int i = 0; i < adapter.getCount(); i++)
			adapter.getItem(i).setChecked(false);
	}

	public void refreshList() {
		adapter.notifyDataSetChanged();
	}
}
