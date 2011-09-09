package ru.megazlo.crazytest.components;

import java.util.ArrayList;

import ru.megazlo.crazytest.utils.NoteData;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NotifyAdapter extends BaseAdapter {

	private ArrayList<NoteData> dat = new ArrayList<NoteData>();
	private Context c;

	public NotifyAdapter(Context _c, ArrayList<NoteData> _dat) {
		c = _c;
		dat = _dat;
	}

	@Override
	public int getCount() {
		return dat.size();
	}

	@Override
	public Object getItem(int position) {
		return dat.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NotifyRow row = convertView == null ? new NotifyRow(c, dat.get(position)) : (NotifyRow) convertView;
		if (convertView != null)
			row.setData(dat.get(position));
		return row;
	}

	public void add(NoteData _dat) {
		dat.add(_dat);
	}

	public void remove(NoteData _dat) {
		dat.remove(_dat);
	}

}
