package ru.megazlo.fastfile;

import ru.megazlo.colorpicker.ColorCircle;
import ru.megazlo.colorpicker.ColorSlider;
import ru.megazlo.colorpicker.OnColorChangedListener;
import ru.megazlo.fastfile.util.Sets;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class fmSettingsView extends PreferenceActivity implements Preference.OnPreferenceChangeListener,
		Preference.OnPreferenceClickListener, OnColorChangedListener {

	private ColorCircle mColorCircle;
	private ColorSlider mSaturation, mValue;
	private AlertDialog dial;
	private String clickedKey;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Sets.IS_COLORED) {
			Bitmap bmp = Bitmap.createBitmap(new int[] { Sets.BACK_COLOR }, 1, 1, Config.ARGB_8888);
			Drawable drw = new BitmapDrawable(bmp);
			getWindow().setBackgroundDrawable(drw);
		}
		Sets.applySets(this);
		addPreferencesFromResource(R.xml.preferences_view);
		initPrefs();
	}

	private void initPrefs() {
		// -------
		CheckBoxPreference prf4 = (CheckBoxPreference) this.findPreference("fullscr");
		prf4.setChecked(Sets.FULL_SCR);
		prf4.setOnPreferenceChangeListener(this);
		// -------
		CheckBoxPreference prf5 = (CheckBoxPreference) this.findPreference("animate");
		prf5.setChecked(Sets.ANIMATE);
		prf5.setOnPreferenceChangeListener(this);
		// -------
		ListPreference prf7 = (ListPreference) this.findPreference("orientype");
		prf7.setOnPreferenceChangeListener(this);
		// -------
		CheckBoxPreference prf8 = (CheckBoxPreference) this.findPreference("hidden");
		prf8.setChecked(Sets.SHOW_HIDDEN);
		prf8.setOnPreferenceChangeListener(this);
		// -------
		CheckBoxPreference prf9 = (CheckBoxPreference) this.findPreference("imgpreview");
		prf9.setChecked(Sets.SHOW_IMG);
		prf9.setOnPreferenceChangeListener(this);

		CheckBoxPreference prf10 = (CheckBoxPreference) this.findPreference("apkpreview");
		prf10.setChecked(Sets.SHOW_APK);
		prf10.setOnPreferenceChangeListener(this);

		CheckBoxPreference prf11 = (CheckBoxPreference) this.findPreference("mp3preview");
		prf11.setChecked(Sets.SHOW_MP3);
		prf11.setOnPreferenceChangeListener(this);

		Preference prf12 = this.findPreference("bak_color");
		prf12.setOnPreferenceClickListener(this);

		CheckBoxPreference prf13 = (CheckBoxPreference) this.findPreference("colored");
		prf13.setChecked(Sets.IS_COLORED);
		prf13.setOnPreferenceChangeListener(this);
	}

	@Override
	public void onBackPressed() {
		Sets.save(fmMain.CONTEXT.getPreferences(0));
		super.onBackPressed();
	}

	@Override
	public boolean onPreferenceChange(Preference pref, Object nval) {
		String key = pref.getKey();
		if (key.equals("hidden"))
			Sets.SHOW_HIDDEN = (Boolean) nval;
		if (key.equals("imgpreview"))
			Sets.SHOW_IMG = (Boolean) nval;
		if (key.equals("mp3preview"))
			Sets.SHOW_MP3 = (Boolean) nval;
		if (key.equals("apkpreview"))
			Sets.SHOW_APK = (Boolean) nval;
		if (key.equals("colored")) {
			Sets.IS_COLORED = (Boolean) nval;
			if (Sets.IS_COLORED) {
				Bitmap bmp = Bitmap.createBitmap(new int[] { Sets.BACK_COLOR }, 1, 1, Config.ARGB_8888);
				Drawable drw = new BitmapDrawable(bmp);
				this.getWindow().setBackgroundDrawable(drw);
				fmMain.CONTEXT.getWindow().setBackgroundDrawable(drw);
				fmSettings.CONTEXT.getWindow().setBackgroundDrawable(drw);
			} else {
				this.getWindow().setBackgroundDrawableResource(R.drawable.app_background);
				fmMain.CONTEXT.getWindow().setBackgroundDrawableResource(R.drawable.app_background);
				fmSettings.CONTEXT.getWindow().setBackgroundDrawableResource(R.drawable.app_background);
			}
		}
		if (key.equals("fullscr")) {
			Sets.FULL_SCR = (Boolean) nval;
			int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
			if (Sets.FULL_SCR) {
				fmMain.CONTEXT.getWindow().setFlags(flg, flg);
				this.getWindow().setFlags(flg, flg);
			} else {
				fmMain.CONTEXT.getWindow().clearFlags(flg);
				this.getWindow().clearFlags(flg);
			}
		}
		if (key.equals("animate"))
			Sets.ANIMATE = (Boolean) nval;
		if (key.equals("orientype")) {
			Sets.ORIENT_TYPE = Integer.parseInt((String) nval);
			setRequestedOrientation(Sets.ORIENT_TYPE);
			fmMain.CONTEXT.setRequestedOrientation(Sets.ORIENT_TYPE);
		}
		return true;
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
		if (clickedKey.equals("bak_color")) {
			Sets.BACK_COLOR = newColor;
			Bitmap bmp = Bitmap.createBitmap(new int[] { Sets.BACK_COLOR }, 1, 1, Config.ARGB_8888);
			Drawable drw = new BitmapDrawable(bmp);
			this.getWindow().setBackgroundDrawable(drw);
			fmMain.CONTEXT.getWindow().setBackgroundDrawable(drw);
			fmSettings.CONTEXT.getWindow().setBackgroundDrawable(drw);
		}
		dial.dismiss();
	}

	@Override
	public boolean onPreferenceClick(Preference arg0) {
		clickedKey = arg0.getKey();
		if (clickedKey.equals("bak_color")) {
			final View formcon = LayoutInflater.from(this).inflate(R.layout.color_dial, null);
			initializeColor(formcon, Sets.BACK_COLOR);
			dial = new AlertDialog.Builder(this).setView(formcon).create();
			dial.show();
			return true;
		}
		return false;
	}
}