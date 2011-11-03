package ru.megazlo.ledxremote.util;

import ru.megazlo.ledxremote.Main;
import ru.megazlo.ledxremote.R;
import ru.megazlo.ledxremote.components.ColorButton;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.SeekBar;

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

	public static DialogInterface.OnClickListener EnableWiFi = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			AlertDialog alr = (AlertDialog) arg0;
			WifiManager wifi = (WifiManager) alr.getContext().getSystemService(Context.WIFI_SERVICE);
			wifi.setWifiEnabled(true);
			alr.getContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
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

	public static View.OnClickListener clickEnable = new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if (!Util.isErr())
				Util.swapEnable();
		}
	};

	public static View.OnClickListener clsn = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Main.getInst().swapPlayBtn();
			Util.sendColor(((ColorButton) v).getCurrentColor());
		}
	};

	public static SeekBar.OnSeekBarChangeListener seekEvn = new SeekBar.OnSeekBarChangeListener() {
		private int originalProgress;
		private boolean isErr = false;

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (isErr)
				seekBar.setProgress(originalProgress);
			isErr = false;
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			originalProgress = seekBar.getProgress();
			isErr = Util.isErr();
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (isErr)
				return;
			int id = seekBar.getId();
			if (id == R.id.sb_speed) {
				Main.getInst().setPlayedSpeed(progress);
				Util.sendSpeed(progress);
			} else if (id == R.id.sb_brigh)
				Util.sendBrightness(progress);
		}
	};
}
