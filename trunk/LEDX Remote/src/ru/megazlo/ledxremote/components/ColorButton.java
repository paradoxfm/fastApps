package ru.megazlo.ledxremote.components;

import ru.megazlo.colorpicker.ColorCircle;
import ru.megazlo.colorpicker.ColorSlider;
import ru.megazlo.colorpicker.OnColorChangedListener;
import ru.megazlo.ledxremote.R;
import ru.megazlo.ledxremote.util.Util;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ColorButton extends Button implements OnColorChangedListener {

	private int cl;
	private static final String frm = "#%X";
	private static final int clr1 = 0xFFFFFFFF, clr2 = 0xFF000000;
	private ColorCircle mColorCircle;
	private ColorSlider mSaturation, mValue;
	//private TextView txcl;
	public static AlertDialog dial;

	private static View.OnLongClickListener lclick = new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			ColorButton bt = (ColorButton) v;
			final View formcon = LayoutInflater.from(v.getContext()).inflate(R.layout.color_dial, null);
			bt.initializeColor(formcon, bt.getCurrentColor());
			ColorButton.dial = new AlertDialog.Builder(v.getContext()).setView(formcon).setTitle(R.string.cl_ch)
					.setIcon(R.drawable.logo).create();
			ColorButton.dial.show();
			return false;
		}
	};

	public ColorButton(Context context) {
		super(context);
	}

	public ColorButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnLongClickListener(lclick);
	}

	@Override
	public boolean performClick() {
		if (Util.isErr())
			return false;
		return super.performClick();
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
		//txcl = (TextView) v.findViewById(R.id.tx_col);
		//txcl.setText(String.format(frm, color));
	}

	@Override
	public void onColorChanged(View v, int newColor) {
		switch (v.getId()) {
		case R.id.colorcircle:
			mValue.setColors(clr1, newColor);
			mSaturation.setColors(newColor, clr2);
			break;
		case R.id.saturation:
			mColorCircle.setColor(newColor);
			mValue.setColors(clr1, newColor);
			break;
		case R.id.value:
			mColorCircle.setColor(newColor);
			break;
		default:
			break;
		}
		//txcl.setText(String.format(frm, newColor));
	}

	@Override
	public void onColorPicked(View view, int newColor) {
		setBackgroundColor(newColor);
		dial.dismiss();
	}
}
