package ru.zlo.fn.fragments;

import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import com.googlecode.androidannotations.annotations.*;
import ru.zlo.fn.component.NoteAdapter;
import ru.zlo.fn.data.Note;
import ru.zlo.fn.data.SqlHelper;

import java.util.List;

@EFragment
public class NoteListFragment extends ListFragment implements SqlHelper.OnDeleteItem, NoteDetailFragment.OnSaveChanges {

	@Bean
	NoteAdapter adapter;
	@Bean
	SqlHelper helper;
	OnListItemChoice choicer;
	boolean search = false;

	@Override
	public void saveChanges() {
		adapter.notifyDataSetChanged();
	}


	public interface OnListItemChoice {
		void onChoice(Note dat);
	}

	@AfterViews
	void afterInit() {
		helper.addDeleteItemListener(this);
		preloadNotes();
	}

	@Override
	public void deleteItem(Note item) {
		adapter.remove(item);
		adapter.notifyDataSetChanged();
	}

	@Background
	void preloadNotes() {
		List<Note> notes = helper.getAllNoteLists();
		applyLoadedNotes(notes);
	}

	@UiThread
	void applyLoadedNotes(List<Note> notes) {
		adapter.setListItems(notes);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		noteListItemClicked(position);
	}

	void noteListItemClicked(int pos) {
		unsheckAll();
		Note dat = adapter.getItem(pos);
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

	public void createNote() {
		Note newNote = new Note();
		helper.createNote(newNote);
		adapter.add(newNote);
		noteListItemClicked(adapter.getCount() - 1);
	}

	public void search(String text) {
		if (text == null || text.length() < 3)
			search = true;
	}

	public void clearSearchResult() {
		search = false;
	}
}
