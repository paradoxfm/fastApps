package ru.megazlo.ledxremote;

import ru.megazlo.ledxremote.components.ColorButton;
import ru.megazlo.ledxremote.components.PlayTrack;
import ru.megazlo.ledxremote.util.MenuCheck;
import ru.megazlo.ledxremote.util.Sets;
import ru.megazlo.ledxremote.util.Util;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Main extends Activity {
	private PlayTrack[] p_btns = new PlayTrack[5];
	private ColorButton[] c_btns = new ColorButton[5];
	private static Main inst_;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		inst_ = this;
		Sets.load(getPreferences(0));
		initChild();
		initEvents();
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
		Sets.CL_1 = c_btns[0].getCurrentColor();
		Sets.CL_2 = c_btns[1].getCurrentColor();
		Sets.CL_3 = c_btns[2].getCurrentColor();
		Sets.CL_4 = c_btns[3].getCurrentColor();
		Sets.CL_5 = c_btns[4].getCurrentColor();
		Sets.save(getPreferences(0));
	}

	public static Main getInst() {
		return inst_;
	}

	private void initChild() {
		p_btns[0] = (PlayTrack) findViewById(R.id.btProgOne);
		p_btns[1] = (PlayTrack) findViewById(R.id.btProgTwo);
		p_btns[2] = (PlayTrack) findViewById(R.id.btProgThree);
		p_btns[3] = (PlayTrack) findViewById(R.id.btProgFour);
		p_btns[4] = (PlayTrack) findViewById(R.id.btProgFive);

		c_btns[0] = (ColorButton) findViewById(R.id.btClOne);
		c_btns[0].setBackgroundColor(Sets.CL_1);
		c_btns[1] = (ColorButton) findViewById(R.id.btClTwo);
		c_btns[1].setBackgroundColor(Sets.CL_2);
		c_btns[2] = (ColorButton) findViewById(R.id.btClThree);
		c_btns[2].setBackgroundColor(Sets.CL_3);
		c_btns[3] = (ColorButton) findViewById(R.id.btClFour);
		c_btns[3].setBackgroundColor(Sets.CL_4);
		c_btns[4] = (ColorButton) findViewById(R.id.btClFive);
		c_btns[4].setBackgroundColor(Sets.CL_5);
	}

	private void initEvents() {
		for (int i = 0; i < p_btns.length; i++)
			p_btns[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clickProgram(v);
				}
			});
		for (int i = 0; i < p_btns.length; i++)
			c_btns[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ColorButton bt = (ColorButton) v;
					int cl = bt.getCurrentColor();
					Util.sendColor(cl);
				}
			});

		findViewById(R.id.imgEnable).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Util.swapEnable();
			}
		});

		SeekBar sk1 = (SeekBar) findViewById(R.id.sb_speed);
		sk1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				Util.sendSpeed(progress);
			}
		});

		SeekBar sk2 = (SeekBar) findViewById(R.id.sb_speed);
		sk2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				Util.sendBrightness(progress);
			}
		});
	}

	private void clickProgram(View v) {
		PlayTrack tr = (PlayTrack) v;
		for (int i = 0; i < p_btns.length; i++)
			if (p_btns[i] != tr && p_btns[i].getStatePlay() != PlayTrack.INACTIVE)
				p_btns[i].setStatePlay(PlayTrack.INACTIVE);
		byte prg = Byte.parseByte(tr.getTag().toString());
		Util.sendProgram(prg);
	}

}