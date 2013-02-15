package ru.zlo.fn.fragments;

import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.res.StringRes;
import ru.zlo.fn.R;
import ru.zlo.fn.data.SqlHelper;
import ru.zlo.fn.util.FileUtil;

@EFragment
public class MainOptionsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

	@StringRes
	String app_name;

	@AfterViews
	void afterInit() {
		addPreferencesFromResource(R.xml.preferences);
		findPreference("about").setOnPreferenceClickListener(this);
		findPreference("bakup").setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();
		if (key.equals("bakup")) {
			backup();
			return true;
		}
		if (key.equals("about")) {
			showAbout();
			return true;
		}
		return false;
	}

	private void showAbout() {
		String version = "";
		try {
			PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
			version = pInfo.versionName;
		} catch (PackageManager.NameNotFoundException ignored) {
		}
		String title = app_name + " v" + version;
		final View about = LayoutInflater.from(getActivity()).inflate(R.layout.about, null);
		new AlertDialog.Builder(getActivity()).setTitle(title).setIcon(R.drawable.notepad).setView(about).create().show();
	}

	private void backup() {
		String path = SqlHelper.getDbPath(getActivity());
		FileUtil.backupBase(path);
	}
}
