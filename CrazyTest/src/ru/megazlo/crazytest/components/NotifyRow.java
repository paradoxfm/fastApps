package ru.megazlo.crazytest.components;

import ru.megazlo.crazytest.classes.NoteData;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotifyRow extends RelativeLayout {

	private NoteData dat;
	private ImageView ico;
	private TextView title;

	public NotifyRow(Context c, NoteData noteData) {
		super(c);
		initChild();
	}

	private void initChild() {
		// TODO Auto-generated method stub
	}

	public void setData(NoteData noteData) {
		dat = noteData;
		title.setText(dat.Title);
	}

}
