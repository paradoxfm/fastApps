package ru.zlo.ff.components.filerow;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class FileRowAdapter extends BaseAdapter {

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
			((FileRow_) convertView).setData(mItems.get(position));
			return convertView;
		}
		return FileRow_.build(parent.getContext(), mItems.get(position));
	}
}
