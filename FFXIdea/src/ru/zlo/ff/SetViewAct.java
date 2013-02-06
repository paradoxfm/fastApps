package ru.zlo.ff;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.WindowManager;
import ru.zlo.ff.util.Options;
import ru.zlo.ff.util.Sets;

public class SetViewAct extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Sets.applySets(this);
		addPreferencesFromResource(R.xml.preferences_view);
		initPrefs();
	}

	private void initPrefs() {
		// -------
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

	@Override
	public void onBackPressed() {
		//Sets.save(MAct.I.getPreferences(0));
		super.onBackPressed();
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
			if (Options.FULL_SCR) {
				MAct.I.getWindow().setFlags(flg, flg);
				this.getWindow().setFlags(flg, flg);
			} else {
				MAct.I.getWindow().clearFlags(flg);
				this.getWindow().clearFlags(flg);
			}
		}
		if (key.equals("animate"))
			Options.ANIMATE = (Boolean) nval;
		if (key.equals("orientype")) {
			Options.ORIENT_TYPE = Integer.parseInt((String) nval);
			setRequestedOrientation(Options.ORIENT_TYPE);
			MAct.I.setRequestedOrientation(Options.ORIENT_TYPE);
		}
		return true;
	}
}