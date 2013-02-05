package ru.zlo.ff.components.filerow;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FileRowAdapter extends BaseAdapter {

	protected int stat = 0;
	private List<FileRowData> mItems = new ArrayList<FileRowData>();

	public void setListItems(List<FileRowData> lst) {
		mItems.clear();
		mItems.addAll(lst);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public FileRowData getItem(int index) {
		return mItems.get(index);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			((BaseCellRow) convertView).setData(mItems.get(position));
			return convertView;
		}
		return newItem(parent.getContext(), position);
	}

	private BaseCellRow newItem(Context c, int pos) {
		return /* Sets.ISGRID ? new FileGridCell(c, m_Items.get(pos)) : */new FileRow(c, mItems.get(pos));
	}
}
