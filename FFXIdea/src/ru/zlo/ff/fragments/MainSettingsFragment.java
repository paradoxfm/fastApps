package ru.zlo.ff.fragments;


import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import ru.zlo.ff.R;
import ru.zlo.ff.util.Options;

import java.io.File;

public class MainSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		initPrefs();
	}

	@Override
	public boolean onPreferenceChange(Preference pref, Object nval) {
		String key = pref.getKey();
		if (key.equals("extern")) {
			Options.OPEN_THIS = (Boolean) nval;
		}
		if (key.equals("homedir")) {
			File fl = new File((String) nval);
			if (fl.exists() && fl.canRead())
				Options.HOME_PATH = fl;
		}
		return true;
	}

	private void initPrefs() {
		CheckBoxPreference prf1 = (CheckBoxPreference) this.findPreference("extern");
		prf1.setChecked(Options.OPEN_THIS);
		prf1.setOnPreferenceChangeListener(this);
		// -------
		EditTextPreference prf2 = (EditTextPreference) this.findPreference("homedir");
		prf2.setText(Options.HOME_PATH.getPath());
		prf2.setOnPreferenceChangeListener(this);
		// -------
		this.findPreference("abt").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				showAbout();
				return true;
			}
		});
		// -------
		this.findPreference("bak").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				//Options.backUpFTP(Options.this);
				Toast.makeText(getView().getContext(), R.string.backet, Toast.LENGTH_SHORT).show();
				return true;
			}
		});
	}

	private void showAbout() {
		String version = "";
		try {
			PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
			version = pInfo.versionName;
		} catch (PackageManager.NameNotFoundException ignored) {
		}
		String title = getResources().getString(R.string.app_name) + " v" + version;
		LayoutInflater factory = LayoutInflater.from(getActivity());
		final View abtf = factory.inflate(R.layout.about, null);
		new AlertDialog.Builder(getActivity()).setTitle(title).setIcon(R.drawable.i_fold).setView(abtf).create().show();
	}
}
