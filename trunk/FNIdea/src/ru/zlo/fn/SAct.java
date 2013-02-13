package ru.zlo.fn;

import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
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
import ru.megazlo.colorpicker.ColorCircle;
import ru.megazlo.colorpicker.ColorSlider;
import ru.megazlo.colorpicker.OnColorChangedListener;
import ru.zlo.fn.util.Options;

public class SAct extends PreferenceActivity implements Preference.OnPreferenceChangeListener,
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
		addPreferencesFromResource(R.xml.preferences);
		initPrefs();
	}

	@Override
	protected void onResume() {
		int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (Options.FULL_SCR)
			getWindow().setFlags(flg, flg);
		else
			getWindow().clearFlags(flg);
		setRequestedOrientation(Options.ORIENT_TYPE);
		super.onResume();
	}

	private void initPrefs() {
		CheckBoxPreference prf1 = (CheckBoxPreference) this.findPreference("fullscr");
		prf1.setChecked(Options.FULL_SCR);
		prf1.setOnPreferenceChangeListener(this);
		ListPreference prf2 = (ListPreference) this.findPreference("orientype");
		prf2.setDefaultValue(Integer.toString(Options.ORIENT_TYPE));
		prf2.setOnPreferenceChangeListener(this);
		this.findPreference("color_text").setOnPreferenceClickListener(this);
		this.findPreference("line_color").setOnPreferenceClickListener(this);
		this.findPreference("size_text").setOnPreferenceClickListener(this);
		this.findPreference("bakup").setOnPreferenceClickListener(this);
		this.findPreference("about").setOnPreferenceClickListener(this);
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
	public boolean onPreferenceChange(Preference preference, Object val) {
		String key = preference.getKey();
		if (key.equals("fullscr")) {
			Options.FULL_SCR = (Boolean) val;
			int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
			if (Options.FULL_SCR) {
				MAct.I.getWindow().setFlags(flg, flg);
				this.getWindow().setFlags(flg, flg);
			} else {
				MAct.I.getWindow().clearFlags(flg);
				this.getWindow().clearFlags(flg);
			}
		}
		if (key.equals("orientype")) {
			Options.ORIENT_TYPE = Integer.parseInt((String) val);
			setRequestedOrientation(Options.ORIENT_TYPE);
			MAct.I.setRequestedOrientation(Options.ORIENT_TYPE);
		}
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = clickedKey = preference.getKey();
		if (key.equals("bakup")) {
			//Options.backup(MAct.I);
			return true;
		}
		if (key.equals("about")) {
			showAbout();
			return true;
		}
		if (!key.equals("size_text")) {
			final View formcon = LayoutInflater.from(this).inflate(R.layout.color_dial, null);
			if (clickedKey.equals("color_text"))
				initializeColor(formcon, Options.FONT_COLOR);
			else if (clickedKey.equals("line_color"))
				initializeColor(formcon, Options.LINE_COLOR);
			dial = new AlertDialog.Builder(this).setView(formcon).create();
			dial.show();
		} else {
			LayoutInflater factory = LayoutInflater.from(this);
			final View formcon = factory.inflate(R.layout.font_size_dial, null);
			SeekBar bar = (SeekBar) formcon.findViewById(R.id.font_seek);
			final TextView tx = (TextView) formcon.findViewById(R.id.font_pre_size);
			tx.setText(textSize + Options.FONT_SIZE);
			tx.setTextSize(Options.FONT_SIZE);
			bar.setProgress(Options.FONT_SIZE - 14);
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
					Options.FONT_SIZE = progress + 14;
					tx.setText(textSize + Options.FONT_SIZE);
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
			Options.FONT_COLOR = newColor;
		else if (clickedKey.equals("line_color"))
			Options.LINE_COLOR = newColor;
		dial.dismiss();
	}

	@Override
	public void onBackPressed() {
		MAct.I.applyEditorSet();
		Options.save(MAct.I.getPreferences(0));
		super.onBackPressed();
	}

	private void showAbout() {
		String version = "";
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException ignored) {
		}
		String title = getResources().getString(R.string.app_name) + " v" + version;
		LayoutInflater factory = LayoutInflater.from(this);
		final View formcon = factory.inflate(R.layout.about, null);
		new AlertDialog.Builder(this).setTitle(title).setIcon(R.drawable.notepad).setView(formcon).create().show();
	}
}
