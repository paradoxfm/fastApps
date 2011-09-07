package ru.megazlo.fastfile.components.filerow;

import java.util.ArrayList;
import java.util.List;

import ru.megazlo.fastfile.util.Sets;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FileRowAdapter extends BaseAdapter {

	protected int stat = 0;
	private List<FileRowData> m_Items = new ArrayList<FileRowData>();

	public void setListItems(List<FileRowData> lst) {
		m_Items.clear();
		m_Items.addAll(lst);
	}

	@Override
	public int getCount() {
		return m_Items.size();
	}

	@Override
	public FileRowData getItem(int index) {
		return this.m_Items.get(index);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			((BaseCellRow) convertView).setData(m_Items.get(position));
			return convertView;
		}
		return newItem(parent.getContext(), position);
	}

	private BaseCellRow newItem(Context c, int pos) {
		return Sets.ISGRID ? new FileGridCell(c, m_Items.get(pos)) : new FileRow(c, m_Items.get(pos));
	}
}
