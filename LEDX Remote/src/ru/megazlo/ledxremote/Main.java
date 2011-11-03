package ru.megazlo.ledxremote;

import ru.megazlo.ledxremote.components.ColorButton;
import ru.megazlo.ledxremote.components.PlayTrack;
import ru.megazlo.ledxremote.util.EvnBox;
import ru.megazlo.ledxremote.util.MenuCheck;
import ru.megazlo.ledxremote.util.Sets;
import ru.megazlo.ledxremote.util.Util;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class Main extends Activity {
	private PlayTrack[] p_btns = new PlayTrack[Sets.SIZE];
	private ColorButton[] c_btns = new ColorButton[Sets.SIZE];
	private PlayTrack played;
	private SeekBar spd;
	private static Main inst_;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		inst_ = this;
		Sets.load(getPreferences(0)); // UDP порт: 5378 IP адрес 192.168.1.2
		initChild();
		initEvents();
		setControllers();
		this.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuCheck.itemClick(this, item.getItemId());
	}

	public void saveSets() {
		for (int i = 0; i < Sets.SIZE; i++)
			Sets.CLS[i] = c_btns[i].getCurrentColor();
		Sets.save(getPreferences(0));
	}

	public static Main getInst() {
		return inst_;
	}

	private void initChild() {
		spd = (SeekBar) findViewById(R.id.sb_speed);
		int[] id = new int[] { R.id.btProgOne, R.id.btProgTwo, R.id.btProgThree, R.id.btProgFour, R.id.btProgFive };
		for (int i = 0; i < Sets.SIZE; i++) {
			p_btns[i] = (PlayTrack) findViewById(id[i]);
			p_btns[i].setSpeed(spd.getProgress());
		}
		id = new int[] { R.id.btClOne, R.id.btClTwo, R.id.btClThree, R.id.btClFour, R.id.btClFive };
		for (int i = 0; i < Sets.SIZE; i++)
			c_btns[i] = (ColorButton) findViewById(id[i]);
		for (int i = 0; i < Sets.SIZE; i++)
			c_btns[i].setBackgroundColor(Sets.CLS[i]);
	}

	private void initEvents() {
		for (int i = 0; i < Sets.SIZE; i++)
			p_btns[i].setOnClickListener(EvnBox.prglsn);
		for (int i = 0; i < Sets.SIZE; i++)
			c_btns[i].setOnClickListener(EvnBox.clsn);
		((SeekBar) findViewById(R.id.sb_brigh)).setOnSeekBarChangeListener(EvnBox.seekEvn);
		spd.setOnSeekBarChangeListener(EvnBox.seekEvn);
		Spinner spn = (Spinner) findViewById(R.id.spn_loc);
		spn.setOnLongClickListener(EvnBox.dropLong);
		spn.setOnItemSelectedListener(EvnBox.selEvn);
		findViewById(R.id.imgEnable).setOnClickListener(EvnBox.clickEnable);
	}

	public void showContDial() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View formcon = factory.inflate(R.layout.cont_list, null);
		EditText edt = (EditText) formcon.findViewById(R.id.cont_list_ed);
		edt.setText(Sets.CONTROL);
		new AlertDialog.Builder(this).setTitle(R.string.conttrollers).setIcon(R.drawable.logo).setView(formcon)
				.setPositiveButton(R.string.OK, EvnBox.dialEvn).setNegativeButton(R.string.cans, null).create().show();
	}

	public void clickProgram(View v) {
		PlayTrack tr = (PlayTrack) v;
		for (int i = 0; i < p_btns.length; i++)
			if (p_btns[i] != tr && p_btns[i].getStatePlay() != PlayTrack.INACTIVE)
				p_btns[i].setStatePlay(PlayTrack.INACTIVE);
		byte prg = Byte.parseByte(tr.getTag().toString());
		if (!tr.isViewed()) {
			setPlayed(tr);
			Util.sendProgram(prg);
			tr.setViewed(true);
		} else
			Util.swapPlayPause();
	}

	public void setPlayedSpeed(int speed) {
		if (played != null)
			played.setSpeed(speed);
	}

	public void setControllers() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				Sets.CONTROL.split(","));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		((Spinner) findViewById(R.id.spn_loc)).setAdapter(adapter);
	}

	public void showAlert(int errWifi) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View alr = factory.inflate(R.layout.alert, null);
		TextView txt = (TextView) alr.findViewById(R.id.text_alert);
		txt.setText(errWifi);
		new AlertDialog.Builder(this).setTitle(R.string.conttrollers).setIcon(R.drawable.logo).setView(alr)
				.setPositiveButton(R.string.enb_wifi, EvnBox.EnableWiFi).setNegativeButton(R.string.cans, null).create().show();
	}

	public void swapPlayBtn() {
		for (int i = 0; i < p_btns.length; i++)
			p_btns[i].setStatePlay(PlayTrack.INACTIVE);
	}

	private void setPlayed(PlayTrack pl) {
		played = pl;
		spd.setProgress(pl.getSpeed());
	}
}