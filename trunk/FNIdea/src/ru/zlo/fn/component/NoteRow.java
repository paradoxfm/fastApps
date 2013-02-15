package ru.zlo.fn.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.DrawableRes;
import ru.zlo.fn.R;
import ru.zlo.fn.data.Note;
import ru.zlo.fn.util.ActionFactory;

@EViewGroup(R.layout.note_row)
public class NoteRow extends RelativeLayout {

	@DrawableRes
	public static Drawable i_rigth, i_rigth_mark;
	java.text.DateFormat ftime;
	java.text.DateFormat fdate;
	@ViewById
	TextView m_titile, m_word_count;
	@ViewById
	ImageView m_icon, m_arrow;
	@Bean
	ActionFactory factory;
	private Note data;

	public NoteRow(Context context, Note noteData) {
		super(context);
		data = noteData;
		fdate = DateFormat.getDateFormat(context);
		ftime = DateFormat.getTimeFormat(context);
	}

	@AfterViews
	void afterInit() {
		setData(data);
	}

	@Click(R.id.m_icon)
	protected void iconClick(View clickedView) {
		factory.create(clickedView, data).show();
	}

	public void setData(Note noteData) {
		data = noteData;
		m_titile.setText(data.getTitle());
		m_word_count.setText("words: " + data.getWordCount() + "  /  edit: " + fdate.format(data.getDate()) + " " + ftime.format(data.getDate()));
		m_arrow.setImageDrawable(data.isChecked() ? i_rigth_mark : i_rigth);
	}
}
