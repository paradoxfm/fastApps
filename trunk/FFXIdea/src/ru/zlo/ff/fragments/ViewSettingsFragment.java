package ru.zlo.ff.fragments;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.WindowManager;
import ru.zlo.ff.R;
import ru.zlo.ff.util.Options;

public class ViewSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_view);
		initPrefs();
	}

	@Override
	public boolean onPreferenceChange(Preference pref, Object nval) {
		String key = pref.getKey();
		if (key.equals("hidden"))
			Options.SHOW_HIDDEN = (Boolean) nval;
		if (key.equals("imgpreview"))
			Options.SHOW_IMG = (Boolean) nval;
		if (key.equals("mp3preview"))
			Options.SHOW_MP3 = (Boolean) nval;
		if (key.equals("apkpreview"))
			Options.SHOW_APK = (Boolean) nval;
		if (key.equals("fullscr")) {
			Options.FULL_SCR = (Boolean) nval;
			int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
			if (Options.FULL_SCR)
				getActivity().getWindow().setFlags(flg, flg);
			else
				getActivity().getWindow().clearFlags(flg);
		}
		if (key.equals("animate"))
			Options.ANIMATE = (Boolean) nval;
		if (key.equals("orientype")) {
			Options.ORIENT_TYPE = Integer.parseInt((String) nval);
			getActivity().setRequestedOrientation(Options.ORIENT_TYPE);
		}
		return true;
	}

	private void initPrefs() {
		CheckBoxPreference prf4 = (CheckBoxPreference) this.findPreference("fullscr");
		prf4.setChecked(Options.FULL_SCR);
		prf4.setOnPreferenceChangeListener(this);
		// -------
		CheckBoxPreference prf5 = (CheckBoxPreference) this.findPreference("animate");
		prf5.setChecked(Options.ANIMATE);
		prf5.setOnPreferenceChangeListener(this);
		// -------
		ListPreference prf7 = (ListPreference) this.findPreference("orientype");
		prf7.setOnPreferenceChangeListener(this);
		// -------
		CheckBoxPreference prf8 = (CheckBoxPreference) this.findPreference("hidden");
		prf8.setChecked(Options.SHOW_HIDDEN);
		prf8.setOnPreferenceChangeListener(this);
		// -------
		CheckBoxPreference prf9 = (CheckBoxPreference) this.findPreference("imgpreview");
		prf9.setChecked(Options.SHOW_IMG);
		prf9.setOnPreferenceChangeListener(this);

		CheckBoxPreference prf10 = (CheckBoxPreference) this.findPreference("apkpreview");
		prf10.setChecked(Options.SHOW_APK);
		prf10.setOnPreferenceChangeListener(this);

		CheckBoxPreference prf11 = (CheckBoxPreference) this.findPreference("mp3preview");
		prf11.setChecked(Options.SHOW_MP3);
		prf11.setOnPreferenceChangeListener(this);
	}
}
