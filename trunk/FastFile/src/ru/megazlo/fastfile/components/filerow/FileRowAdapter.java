package ru.megazlo.fastfile.components.filerow;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FileRowAdapter extends BaseAdapter {

	private Context m_Context;
	private List<FileRowData> m_Items = new ArrayList<FileRowData>();

	public FileRowAdapter(Context context) {
		this.m_Context = context;
	}

	public void addItem(FileRowData rowDat) {
		m_Items.add(rowDat);
	}

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
		FileRow btv = convertView == null ? new FileRow(m_Context, m_Items.get(position)) : (FileRow) convertView;
		if (convertView != null)
			btv.setData(m_Items.get(position));
		return btv;
	}

	public boolean areAllItemsSelectable() {
		return false;
	}

	public boolean isSelectable(int index) {
		return m_Items.get(index).isSelectable();
	}
}
