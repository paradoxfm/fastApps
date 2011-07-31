package ru.megazlo.fastnote;

import ru.megazlo.fastnote.color.ColorCircle;
import ru.megazlo.fastnote.color.ColorSlider;
import ru.megazlo.fastnote.color.OnColorChangedListener;
import ru.megazlo.fastnote.util.Sets;
import ru.megazlo.fastnote.R;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class fmSettings extends PreferenceActivity implements Preference.OnPreferenceChangeListener,
		Preference.OnPreferenceClickListener, OnColorChangedListener {

	private ColorCircle mColorCircle;
	private ColorSlider mSaturation, mValue;
	private AlertDialog dial;
	private String textSize;
	private String clickedKey;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		textSize = getResources().getString(R.string.text_size);
		Sets.applySets(this);
		addPreferencesFromResource(R.xml.preferences);
		initPrefs();
	}

	private void initPrefs() {
		CheckBoxPreference prf1 = (CheckBoxPreference) this.findPreference("fullscr");
		prf1.setChecked(Sets.FULL_SCR);
		prf1.setOnPreferenceChangeListener(this);
		ListPreference prf2 = (ListPreference) this.findPreference("orientype");
		prf2.setDefaultValue(Integer.toString(Sets.ORIENT_TYPE));
		prf2.setOnPreferenceChangeListener(this);
		this.findPreference("color_text").setOnPreferenceClickListener(this);
		this.findPreference("line_color").setOnPreferenceClickListener(this);
		this.findPreference("size_text").setOnPreferenceClickListener(this);
		this.findPreference("bakup").setOnPreferenceClickListener(this);
		this.findPreference("about").setOnPreferenceClickListener(this);
	}

	void initializeColor(View v, int color) {
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
	public boolean onPreferenceChange(Preference preference, Object val) {
		String key = preference.getKey();
		if (key.equals("fullscr")) {
			Sets.FULL_SCR = (Boolean) val;
			int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
			if (Sets.FULL_SCR) {
				fmMain.CONTEXT.getWindow().setFlags(flg, flg);
				this.getWindow().setFlags(flg, flg);
			} else {
				fmMain.CONTEXT.getWindow().clearFlags(flg);
				this.getWindow().clearFlags(flg);
			}
		}
		if (key.equals("orientype")) {
			Sets.ORIENT_TYPE = Integer.parseInt((String) val);
			setRequestedOrientation(Sets.ORIENT_TYPE);
			fmMain.CONTEXT.setRequestedOrientation(Sets.ORIENT_TYPE);
		}
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = clickedKey = preference.getKey();
		if (key.equals("bakup")) {
			Sets.backup(fmMain.CONTEXT);
			return true;
		}
		if (key.equals("about")) {
			showAbout();
			return true;
		}
		if (!key.equals("size_text")) {
			final View formcon = LayoutInflater.from(this).inflate(R.layout.color_dial, null);
			if (clickedKey.equals("color_text"))
				initializeColor(formcon, Sets.FONT_COLOR);
			else if (clickedKey.equals("line_color"))
				initializeColor(formcon, Sets.LINE_COLOR);
			dial = new AlertDialog.Builder(this).setView(formcon).create();
			dial.show();
		} else {
			LayoutInflater factory = LayoutInflater.from(this);
			final View formcon = factory.inflate(R.layout.font_size_dial, null);
			SeekBar bar = (SeekBar) formcon.findViewById(R.id.font_seek);
			final TextView tx = (TextView) formcon.findViewById(R.id.font_pre_size);
			tx.setText(textSize + Sets.FONT_SIZE);
			tx.setTextSize(Sets.FONT_SIZE);
			bar.setProgress(Sets.FONT_SIZE - 14);
			bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					tx.setTextSize(progress + 14);
					Sets.FONT_SIZE = progress + 14;
					tx.setText(textSize + Sets.FONT_SIZE);
				}
			});

			dial = new AlertDialog.Builder(this).setView(formcon).create();
			dial.show();
		}
		return false;
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
		if (clickedKey.equals("color_text"))
			Sets.FONT_COLOR = newColor;
		else if (clickedKey.equals("line_color"))
			Sets.LINE_COLOR = newColor;
		dial.dismiss();
	}

	@Override
	public void onBackPressed() {
		fmMain.CONTEXT.applyEditorSet();
		Sets.save(fmMain.CONTEXT.getPreferences(0));
		super.onBackPressed();
	}

	private void showAbout() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View formcon = factory.inflate(R.layout.about, null);
		new AlertDialog.Builder(this).setTitle(R.string.about).setIcon(R.drawable.notepad).setView(formcon).create().show();
	}
}
