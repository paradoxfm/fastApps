package ru.megazlo.crazytest.components;

import java.util.Date;

import ru.megazlo.crazytest.R;
import ru.megazlo.crazytest.utils.NoteData;
import ru.megazlo.crazytest.utils.dbLay;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class NotifyRow extends RelativeLayout {

	private static double CONV;
	private static String rembe;
	private NoteData dat;
	private ImageView ico;
	private TextView title, date;

	protected DialogInterface.OnClickListener delet = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dlg, int which) {
			NotifyList lst = (NotifyList) NotifyRow.this.getParent();
			NotifyAdapter adp = (NotifyAdapter) lst.getAdapter();
			adp.remove(NotifyRow.this.dat);
			dbLay.deleteNote(NotifyRow.this.dat.ID);
			adp.notifyDataSetChanged();
		}
	};

	protected DialogInterface.OnClickListener edit = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dlg, int which) {
			AlertDialog alr = (AlertDialog) dlg;
			Date data;
			if (((CheckBox) alr.findViewById(R.id.cb_now)).isChecked())
				data = new Date();
			else {
				TimePicker tp = (TimePicker) alr.findViewById(R.id.tp_time);
				DatePicker dp = (DatePicker) alr.findViewById(R.id.dp_date);
				data = new Date(dp.getYear() - 1900, dp.getMonth(), dp.getDayOfMonth(), tp.getCurrentHour(),
						tp.getCurrentMinute());
			}
			String title = ((EditText) alr.findViewById(R.id.et_title)).getEditableText().toString();
			NotifyAdapter adp = (NotifyAdapter) ((NotifyList) getParent()).getAdapter();
			NoteData dat = new NoteData(0, data, title);
			adp.add(dat);
			adp.notifyDataSetChanged();
			dbLay.insertNote(dat);
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

		LayoutParams lp = new RelativeLayout.LayoutParams(px(36), px(36));
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		ImageView del = new ImageView(getContext());
		int del_id = 453964705;
		del.setId(del_id);
		del.setPadding(0, px(10), px(6.6), 0);
		del.setImageResource(R.drawable.qa_delete);
		addView(del, lp);

		del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView vi = new TextView(getContext());
				vi.setText(R.string.are_want);
				new AlertDialog.Builder(getContext()).setTitle(R.string.tt_del).setIcon(R.drawable.qa_delete).setView(vi)
						.setNegativeButton(R.string.bt_cansel, null).setPositiveButton(R.string.bt_ok, delet).create().show();
			}
		});

		LayoutParams lp2 = new RelativeLayout.LayoutParams(px(36), px(36));
		lp2.addRule(RelativeLayout.LEFT_OF, del_id);
		ImageView edi = new ImageView(getContext());
		int edi_id = 453964735;
		edi.setId(edi_id);
		edi.setPadding(px(0), px(10), px(6.6), 0);
		edi.setImageResource(R.drawable.qa_rename);
		addView(edi, lp2);
		edi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater factory = LayoutInflater.from(getContext());
				final View frm = factory.inflate(R.layout.newnote, null);
				frm.findViewById(R.id.cb_now).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						((View) v.getParent()).findViewById(R.id.ll_now).setVisibility(
								((CheckBox) v).isChecked() ? View.GONE : View.VISIBLE);
					}
				});
				new AlertDialog.Builder(getContext()).setTitle(R.string.new_n).setIcon(R.drawable.notepad).setView(frm)
						.setNegativeButton(R.string.bt_cansel, null).setPositiveButton(R.string.bt_ok, null/* oke */).create()
						.show();
			}
		});

		title = new TextView(getContext());
		title.setTextSize(16);
		title.setHorizontallyScrolling(true);
		title.setMinimumHeight(px(24.6));
		title.setMaxHeight(px(24.6));
		title.setTypeface(null, Typeface.BOLD);
		title.setText(dat.Title);
		LayoutParams m_txt_dir_lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		m_txt_dir_lp.addRule(RelativeLayout.LEFT_OF, edi_id);
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
