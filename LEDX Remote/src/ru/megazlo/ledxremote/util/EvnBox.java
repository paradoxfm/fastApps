package ru.megazlo.ledxremote.util;

import ru.megazlo.ledxremote.Main;
import ru.megazlo.ledxremote.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;

public abstract class EvnBox {
	public static DialogInterface.OnClickListener dialEvn = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			AlertDialog alr = (AlertDialog) arg0;
			EditText edt = (EditText) alr.findViewById(R.id.cont_list_ed);
			String tmp = edt.getText().toString();
			if (tmp.length() > 0)
				Sets.CONTROL = tmp;
			Main.getInst().setControllers();
			Main.getInst().saveSets();
		}
	};

	public static OnItemSelectedListener selEvn = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Util.CONTROLLER = (byte) (arg2 + 1);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	public static View.OnLongClickListener dropLong = new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View arg0) {
			Main.getInst().showContDial();
			return true;
		}
	};

	public static View.OnClickListener prglsn = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Main.getInst().clickProgram(v);
		}
	};

	public static SeekEvent seekEvn = new SeekEvent();
}
