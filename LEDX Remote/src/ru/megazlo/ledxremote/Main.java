package ru.megazlo.ledxremote;

import ru.megazlo.colorpicker.ColorCircle;
import ru.megazlo.colorpicker.ColorSlider;
import ru.megazlo.colorpicker.OnColorChangedListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class Main extends Activity implements OnColorChangedListener {
	private int clor = 0xfcca04;
	private ColorCircle mColorCircle;
	private ColorSlider mSaturation, mValue;
	private AlertDialog dial;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		initEvents();
	}

	private void initializeColor(View v, int color) {
		mColorCircle = (ColorCircle) v.findViewById(R.id.colorcircle);
		mColorCircle.setOnColorChangedListener(this);
		mColorCircle.setColor(color);
		mSaturation = (ColorSlider) v.findViewById(R.id.saturation);
		mSaturation.setOnColorChangedListener(this);
		mSaturation.setColors(color, Color.BLACK);
		mValue = (ColorSlider) v.findViewById(R.id.value);
		mValue.setOnColorChangedListener(this);
		mValue.setColors(Color.WHITE, color);
	}

	private void initEvents() {

		findViewById(R.id.imgEnable).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView im = (ImageView) v;
				if (im.getTag().equals("on")) {
					im.setImageResource(R.drawable.shutdown);
					im.setTag("off");
				} else {
					im.setImageResource(R.drawable.standby);
					im.setTag("on");
				}

			}
		});
		findViewById(R.id.btCustomColor).setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				final View formcon = LayoutInflater.from(Main.this).inflate(R.layout.color_dial, null);
				initializeColor(formcon, clor);
				dial = new AlertDialog.Builder(Main.this).setView(formcon).create();
				dial.show();
				return true;
			}
		});
	}

	@Override
	public void onColorChanged(View view, int newColor) {
		if (view == mColorCircle) {
			mValue.setColors(0xFFFFFFFF, newColor);
			mSaturation.setColors(newColor, 0xff000000);
		} else if (view == mSaturation) {
			mColorCircle.setColor(newColor);
			mValue.setColors(0xFFFFFFFF, newColor);
		} else if (view == mValue) {
			mColorCircle.setColor(newColor);
		}
	}

	@Override
	public void onColorPicked(View view, int newColor) {
		findViewById(R.id.btCustomColor).setBackgroundColor(newColor);
		clor = newColor;
		dial.dismiss();
	}
}