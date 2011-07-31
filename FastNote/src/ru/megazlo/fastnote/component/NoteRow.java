package ru.megazlo.fastnote.component;

import ru.megazlo.fastnote.util.ActionFactory;
import ru.megazlo.fastnote.util.Sets;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NoteRow extends RelativeLayout {
	private TextView m_titile, m_word_count;
	private ImageView m_icon, m_arrow;
	private NoteData data;

	public NoteRow(Context context, NoteData noteData) {
		super(context);
		data = noteData;
		this.setMinimumHeight(px(46.6));
		initChild(context);
	}

	private void initChild(Context context) {
		this.m_icon = new ImageView(context);
		this.m_icon.setImageDrawable(Sets.I_NOTE);
		this.m_icon.setPadding(px(5.3), px(6.6), 0, 0);
		this.addView(m_icon, px(42.6), px(42.6));

		this.m_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		m_titile = new TextView(context);
		m_titile.setTextSize(16);
		m_titile.setHorizontallyScrolling(true);
		m_titile.setMinimumHeight(px(24.6));
		m_titile.setMaxHeight(px(24.6));
		m_titile.setTypeface(null, Typeface.BOLD);
		m_titile.setText(data.getTitle());
		LayoutParams m_txt_dir_lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		m_txt_dir_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		m_titile.setPadding(px(46.6), 0, 0, 0);
		this.addView(m_titile, m_txt_dir_lp);

		m_word_count = new TextView(context);
		m_word_count.setText("words: " + data.getWordCount() + "  /  edit: " + data.getDate());
		m_word_count.setMinimumHeight(px(16.6));
		m_word_count.setMaxHeight(px(16.6));
		m_word_count.setTextSize(12);
		LayoutParams m_txt_items_lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		m_txt_items_lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		m_word_count.setPadding(px(46.6), 0, 0, 0);
		this.addView(m_word_count, m_txt_items_lp);

		LayoutParams lp = new RelativeLayout.LayoutParams(px(36), px(36));
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		m_arrow = new ImageView(context);
		m_arrow.setPadding(0, px(10), px(6.6), 0);
		m_arrow.setImageDrawable(data.isChecked() ? Sets.I_RIGTH_MARK : Sets.I_RIGTH);
		addView(m_arrow, lp);

		m_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActionFactory.create(v, data).show();
			}
		});
	}

	public void setData(NoteData noteData) {
		data = noteData;
		m_titile.setText(data.getTitle());
		m_word_count.setText("words: " + data.getWordCount() + "  /  edit: " + data.getDate());
		m_arrow.setImageDrawable(data.isChecked() ? Sets.I_RIGTH_MARK : Sets.I_RIGTH);
	}

	public NoteData getData() {
		return data;
	}

	public boolean isChecked() {
		return data.isChecked();
	}

	private static int px(double d) {
		return (int) (d * Sets.DIP_CONV);
	}

	public void setChecked(boolean check) {
		data.setChecked(check);
		m_arrow.setImageDrawable(data.isChecked() ? Sets.I_RIGTH_MARK : Sets.I_RIGTH);
	}

	public void setTitle(String title) {
		m_titile.setText(title);
	}

}
