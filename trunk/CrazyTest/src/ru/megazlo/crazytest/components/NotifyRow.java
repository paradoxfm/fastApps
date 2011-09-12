package ru.megazlo.crazytest.components;

import ru.megazlo.crazytest.R;
import ru.megazlo.crazytest.utils.NoteData;
import ru.megazlo.crazytest.utils.dbLay;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotifyRow extends RelativeLayout {

	private static double CONV;
	private static String rembe;
	private NoteData dat;
	private ImageView ico;
	private TextView title, date;

	protected DialogInterface.OnClickListener editdom = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dlg, int which) {
			NotifyList lst = (NotifyList) NotifyRow.this.getParent();
			NotifyAdapter adp = (NotifyAdapter) lst.getAdapter();
			adp.remove(NotifyRow.this.dat);
			dbLay.deleteNote(NotifyRow.this.dat.ID);
			adp.notifyDataSetChanged();
		}
	};

	public NotifyRow(Context c, NoteData noteData) {
		super(c);
		dat = noteData;
		if (CONV == 0)
			CONV = c.getResources().getDisplayMetrics().densityDpi / (float) DisplayMetrics.DENSITY_DEFAULT;
		if (rembe == null)
			rembe = c.getResources().getString(R.string.rembe) + "  ";
		this.setMinimumHeight(px(46.6));
		initChild();
	}

	private void initChild() {
		ico = new ImageView(getContext());
		ico.setImageResource(R.drawable.inote);
		ico.setPadding(px(5.3), px(6.6), 0, 0);
		addView(ico, px(42.6), px(42.6));

		title = new TextView(getContext());
		title.setTextSize(16);
		title.setHorizontallyScrolling(true);
		title.setMinimumHeight(px(24.6));
		title.setMaxHeight(px(24.6));
		title.setTypeface(null, Typeface.BOLD);
		title.setText(dat.Title);
		LayoutParams m_txt_dir_lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		m_txt_dir_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		title.setPadding(px(46.6), 0, 0, 0);
		this.addView(title, m_txt_dir_lp);

		date = new TextView(getContext());
		date.setText(rembe + dat.getDate());
		date.setMinimumHeight(px(16.6));
		date.setMaxHeight(px(16.6));
		date.setTextSize(12);
		LayoutParams m_txt_items_lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		m_txt_items_lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		date.setPadding(px(46.6), 0, 0, 0);
		this.addView(date, m_txt_items_lp);

		LayoutParams lp = new RelativeLayout.LayoutParams(px(36), px(36));
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		ImageView del = new ImageView(getContext());
		del.setPadding(0, px(10), px(6.6), 0);
		del.setImageResource(R.drawable.qa_delete);
		addView(del, lp);

		del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView vi = new TextView(getContext());
				vi.setText(R.string.are_want);
				new AlertDialog.Builder(getContext()).setTitle(R.string.tt_del).setIcon(R.drawable.qa_delete).setView(vi)
						.setNegativeButton(R.string.bt_cansel, null).setPositiveButton(R.string.bt_ok, editdom).create().show();
			}
		});
	}

	public void setData(NoteData noteData) {
		dat = noteData;
		title.setText(dat.Title);
		date.setText(rembe + dat.getDate());
	}

	public NoteData getData() {
		return dat;
	}

	private static int px(double d) {

		return (int) (d * CONV);
	}
}
