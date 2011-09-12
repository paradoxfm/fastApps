package ru.megazlo.crazytest;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import ru.megazlo.crazytest.components.NotifyAdapter;
import ru.megazlo.crazytest.components.NotifyList;
import ru.megazlo.crazytest.utils.NoteData;
import ru.megazlo.crazytest.utils.Sets;
import ru.megazlo.crazytest.utils.dbLay;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class fmMain extends Activity {
	private NotifyList lst;

	protected DialogInterface.OnClickListener oke = new DialogInterface.OnClickListener() {
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
			NotifyAdapter adp = (NotifyAdapter) lst.getAdapter();
			NoteData dat = new NoteData(0, data, title);
			adp.add(dat);
			adp.notifyDataSetChanged();
			dbLay.insertNote(dat);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(lst = new NotifyList(this));
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		initChild();
		File fl = getExternalFilesDir(null);
		if (dbLay.connectBase(fl)) {
			ArrayList<NoteData> dat = dbLay.getList(fl, null);
			lst.setAdapter(new NotifyAdapter(this, dat));
		}
		Sets.load(this);
	}

	private void initChild() {
		findViewById(R.id.newn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fmMain.this.createNew();
			}
		});
	}

	protected void createNew() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View frm = factory.inflate(R.layout.newnote, null);
		frm.findViewById(R.id.cb_now).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((View) v.getParent()).findViewById(R.id.ll_now).setVisibility(
						((CheckBox) v).isChecked() ? View.GONE : View.VISIBLE);
			}
		});
		AlertDialog alr = new AlertDialog.Builder(this).setTitle(R.string.new_n).setIcon(R.drawable.notepad).setView(frm)
				.setNegativeButton(R.string.bt_cansel, null).setPositiveButton(R.string.bt_ok, oke).create();
		alr.show();
	}

	@Override
	protected void onStop() {
		Sets.save(this);
		super.onStop();
	}
}
