package ru.zlo.fn.component;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.googlecode.androidannotations.annotations.EBean;
import ru.zlo.fn.data.Note;

import java.util.ArrayList;
import java.util.List;

@EBean
public class NoteAdapter extends BaseAdapter {

	private ArrayList<Note> mItems = new ArrayList<Note>();

	public NoteAdapter() {
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Note getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setListItems(List<Note> lst) {
		mItems.clear();
		mItems.addAll(lst);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			((NoteRow_) convertView).setData(mItems.get(position));
			return convertView;
		}
		return NoteRow_.build(parent.getContext(), mItems.get(position));
	}

	public void add(Note dat) {
		mItems.add(dat);
	}

	public void remove(Note dat) {
		mItems.remove(dat);
	}
}
