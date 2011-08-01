package ru.megazlo.fastfile;

import java.io.File;

import ru.megazlo.fastfile.util.Sets;
import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class fmSettings extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		initPrefs();
	}

	@Override
	protected void onResume() {
		Sets.applySets(this);
		super.onResume();
	}

	private void initPrefs() {
		CheckBoxPreference prf1 = (CheckBoxPreference) this.findPreference("extern");
		prf1.setChecked(Sets.OPEN_THIS);
		prf1.setOnPreferenceChangeListener(this);
		// -------
		EditTextPreference prf2 = (EditTextPreference) this.findPreference("homedir");
		prf2.setText(Sets.HOME_PATH.getPath());
		prf2.setOnPreferenceChangeListener(this);
		// -------
		this.findPreference("abt").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				showAbout();
				return true;
			}
		});
		// -------
		this.findPreference("bak").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Sets.backUpFTP(fmSettings.this);
				Toast.makeText(fmSettings.this, R.string.backet, Toast.LENGTH_SHORT).show();
				return true;
			}
		});
	}

	@Override
	public void onBackPressed() {
		Sets.save(fmMain.CONTEXT.getPreferences(0));
		super.onBackPressed();
	}

	@Override
	public boolean onPreferenceChange(Preference pref, Object nval) {
		String key = pref.getKey();
		if (key.equals("extern")) {
			Sets.OPEN_THIS = (Boolean) nval;
		}
		if (key.equals("homedir")) {
			File fl = new File((String) nval);
			if (fl.exists())
				Sets.HOME_PATH = fl;
		}
		return true;
	}

	private void showAbout() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View formcon = factory.inflate(R.layout.about, null);
		new AlertDialog.Builder(this).setTitle(R.string.about).setIcon(R.drawable.folder).setView(formcon).create().show();
	}

}
