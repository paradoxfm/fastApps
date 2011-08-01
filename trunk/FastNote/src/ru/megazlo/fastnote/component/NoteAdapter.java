package ru.megazlo.fastnote.component;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NoteAdapter extends BaseAdapter {

	private Context m_Context;
	private ArrayList<NoteData> m_Items;

	public NoteAdapter(Context c, ArrayList<NoteData> dat) {
		m_Context = c;
		m_Items = dat;
	}

	@Override
	public int getCount() {
		return m_Items.size();
	}

	@Override
	public NoteData getItem(int position) {
		return m_Items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NoteRow row = convertView == null ? new NoteRow(m_Context, m_Items.get(position)) : (NoteRow) convertView;
		if (convertView != null)
			row.setData(m_Items.get(position));
		return row;
	}

	public void add(NoteData dat) {
		m_Items.add(dat);
	}

	public void remove(NoteData dat) {
		m_Items.remove(dat);
	}
}
