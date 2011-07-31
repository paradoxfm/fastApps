package ru.megazlo.fastfilehd;

import ru.megazlo.fastfilehd.util.Sets;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.WindowManager;

public class fmSettingsView extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
}