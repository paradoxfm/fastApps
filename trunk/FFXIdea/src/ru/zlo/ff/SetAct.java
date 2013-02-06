package ru.zlo.ff;

import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import ru.zlo.ff.util.Sets;

import java.util.List;

public class SetAct extends PreferenceActivity/* implements Preference.OnPreferenceChangeListener*/ {

	public static SetAct I;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPrefs()).commit();
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}

	public static class MainPrefs extends PreferenceFragment {
		//public static MainPrefs I;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
			//PreferenceManager.setDefaultValues(getActivity(), R.xml.advanced_preferences, false);
			//I = this;
			/*if (Sets.IS_COLORED) {
				Bitmap bmp = Bitmap.createBitmap(new int[]{Sets.BACK_COLOR}, 1, 1, Bitmap.Config.ARGB_8888);
				Drawable drw = new BitmapDrawable(bmp);
				getActivity().getWindow().setBackgroundDrawable(drw);
			}*/
		}
	}

	public static class DisplayPrefs extends PreferenceFragment {
		//public static DisplayPrefs I;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences_view);
			//PreferenceManager.setDefaultValues(getActivity(), R.xml.advanced_preferences, false);
			//I = this;
		}
	}

	/*@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		I = this;
		if (Sets.IS_COLORED) {
			Bitmap bmp = Bitmap.createBitmap(new int[] { Sets.BACK_COLOR }, 1, 1, Config.ARGB_8888);
			Drawable drw =  new BitmapDrawable(bmp);
			getWindow().setBackgroundDrawable(drw);
		}
		addPreferencesFromResource(R.xml.preferences);
		initPrefs();
	}*/

	@Override
	protected void onResume() {
		//Sets.applySets(this);
		super.onResume();
	}

	/*private void initPrefs() {
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
				Sets.backUpFTP(SetAct.this);
				Toast.makeText(SetAct.this, R.string.backet, Toast.LENGTH_SHORT).show();
				return true;
			}
		});
	}*/

	@Override
	public void onBackPressed() {
		//Sets.save(MAct.I.getPreferences(0));
		super.onBackPressed();
	}

	/*@Override
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
	}*/

	private void showAbout() {
		String version = "";
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException ignored) {
		}
		String title = getResources().getString(R.string.app_name) + " v" + version;
		LayoutInflater factory = LayoutInflater.from(this);
		final View abtf = factory.inflate(R.layout.about, null);
		new AlertDialog.Builder(this).setTitle(title).setIcon(R.drawable.i_fold).setView(abtf).create().show();
	}

}
