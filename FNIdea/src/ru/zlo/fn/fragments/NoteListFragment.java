package ru.zlo.fn.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.googlecode.androidannotations.annotations.*;
import ru.zlo.fn.component.NoteAdapter;
import ru.zlo.fn.data.Note;
import ru.zlo.fn.data.SqlHelper;

import java.util.List;

@EFragment
public class NoteListFragment extends ListFragment implements SqlHelper.OnDeleteItem, NoteDetailFragment.OnSaveChanges {

	boolean search = false;
	//Integer lastPos;
	@Bean
	NoteAdapter adapter;
	@Bean
	SqlHelper helper;
	OnListItemChoice choicer;
	//private static final String SEL_POS_VALUE = "selectedPosition";

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
		setListAdapter(adapter);
		adapter.setListItems(notes);
		/*if (lastPos != null)
			noteListItemClicked(lastPos);*/
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		noteListItemClicked(position);
	}

	void noteListItemClicked(int pos) {
		//lastPos = pos;
		adapter.unsheckAll();
		Note dat = adapter.getItem(pos);
		dat.setChecked(true);
		adapter.notifyDataSetChanged();
		if (choicer != null)
			choicer.onChoice(dat);
	}

	public void setOnListItemChoice(OnListItemChoice choice) {
		choicer = choice;
	}

	public void createNote() {
		Note newNote = new Note();
		helper.createNote(newNote);
		adapter.add(newNote);
		noteListItemClicked(adapter.getCount() - 1);
	}

	@Background
	public void search(String text) {
		if (text == null || text.length() < 3)
			search = true;
		List<Note> lst = helper.searchByText(text);
		applyLoadedNotes(lst);
	}

	public boolean isSearch() {
		return search;
	}

	public void clearSearchResult() {
		search = false;
		preloadNotes();
	}

/*	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			lastPos = savedInstanceState.getInt(SEL_POS_VALUE);
			lastPos = lastPos == -1 ? null : lastPos;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SEL_POS_VALUE, lastPos == null ? -1 : lastPos);
	}*/
}
