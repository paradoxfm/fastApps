package ru.megazlo.ledxremote.components;

import ru.megazlo.colorpicker.ColorCircle;
import ru.megazlo.colorpicker.ColorSlider;
import ru.megazlo.colorpicker.OnColorChangedListener;
import ru.megazlo.ledxremote.R;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class ColorButton extends Button implements OnColorChangedListener {

	private int cl;
	private ColorCircle mColorCircle;
	private ColorSlider mSaturation, mValue;
	private AlertDialog dial;

	public ColorButton(Context context) {
		super(context);
	}

	public ColorButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				final View formcon = LayoutInflater.from(getContext()).inflate(R.layout.color_dial, null);
				initializeColor(formcon, getCurrentColor());
				dial = new AlertDialog.Builder(getContext()).setView(formcon).create();
				dial.show();
				return false;
			}
		});
	}

	public int getCurrentColor() {
		return cl;
	}

	@Override
	public void setBackgroundColor(int color) {
		cl = color;
		super.setBackgroundColor(color);
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
		setBackgroundColor(newColor);
		dial.dismiss();
	}
}
