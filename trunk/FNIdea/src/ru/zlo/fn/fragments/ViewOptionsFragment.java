package ru.zlo.fn.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import ru.megazlo.colorpicker.ColorCircle;
import ru.megazlo.colorpicker.ColorSlider;
import ru.megazlo.colorpicker.OnColorChangedListener;
import ru.zlo.fn.R;
import ru.zlo.fn.util.Options_;

@EFragment
public class ViewOptionsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener, OnColorChangedListener {

	@Pref
	Options_ opt;
	@StringRes(R.string.text_size)
	String textSize;
	ColorCircle mColorCircle;
	ColorSlider mSaturation, mValue;
	AlertDialog dial;
	String clickedKey;

	@AfterViews
	void initPrefs() {
		addPreferencesFromResource(R.xml.preferences_view);
		CheckBoxPreference prf1 = (CheckBoxPreference) this.findPreference("fullscr");
		prf1.setChecked(opt.fullScreen().get());
		prf1.setOnPreferenceChangeListener(this);
		ListPreference prf2 = (ListPreference) this.findPreference("orientype");
		prf2.setDefaultValue(Integer.toString(opt.orientation().get()));
		prf2.setOnPreferenceChangeListener(this);
		findPreference("color_text").setOnPreferenceClickListener(this);
		findPreference("line_color").setOnPreferenceClickListener(this);
		findPreference("size_text").setOnPreferenceClickListener(this);
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
			opt.fullScreen().put((Boolean) val);
			int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
			if (opt.fullScreen().get())
				getActivity().getWindow().setFlags(flg, flg);
			else
				getActivity().getWindow().clearFlags(flg);
		}
		if (key.equals("orientype")) {
			opt.orientation().put(Integer.parseInt((String) val));
			getActivity().setRequestedOrientation(opt.orientation().get());
		}
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = clickedKey = preference.getKey();
		if (!key.equals("size_text")) {
			final View formcon = LayoutInflater.from(getActivity()).inflate(R.layout.color_dial, null);
			if (clickedKey.equals("color_text"))
				initializeColor(formcon, opt.fontColor().get());
			else if (clickedKey.equals("line_color"))
				initializeColor(formcon, opt.lineColor().get());
			dial = new AlertDialog.Builder(getActivity()).setView(formcon).create();
			dial.show();
		} else {
			LayoutInflater factory = LayoutInflater.from(getActivity());
			final View formcon = factory.inflate(R.layout.font_size_dial, null);
			SeekBar bar = (SeekBar) formcon.findViewById(R.id.font_seek);
			final TextView tx = (TextView) formcon.findViewById(R.id.font_pre_size);
			tx.setText(textSize + opt.fontSize().get());
			tx.setTextSize(opt.fontSize().get());
			bar.setProgress(opt.fontSize().get() - 14);
			bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					tx.setTextSize(progress + 14);
					opt.fontSize().put(progress + 14);
					tx.setText(textSize + opt.fontSize().get());
				}
			});

			dial = new AlertDialog.Builder(getActivity()).setView(formcon).create();
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
			opt.fontColor().put(newColor);
		else if (clickedKey.equals("line_color"))
			opt.lineColor().put(newColor);
		dial.dismiss();
	}
}
