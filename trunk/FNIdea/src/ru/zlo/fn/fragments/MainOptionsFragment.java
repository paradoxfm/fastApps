package ru.zlo.fn.fragments;

import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.StringRes;
import ru.zlo.fn.R;
import ru.zlo.fn.data.Note;
import ru.zlo.fn.data.OldNote;
import ru.zlo.fn.data.SqlHelper;
import ru.zlo.fn.data.SqlImport;
import ru.zlo.fn.util.FileUtil;

import java.io.File;
import java.util.Date;
import java.util.List;

@EFragment
public class MainOptionsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

	@StringRes
	String app_name;
	@Bean
	SqlHelper helper;

	@AfterViews
	void afterInit() {
		addPreferencesFromResource(R.xml.preferences);
		findPreference("about").setOnPreferenceClickListener(this);
		findPreference("bakup").setOnPreferenceClickListener(this);
		findPreference("import").setOnPreferenceClickListener(this);
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
		if (key.equals("import")) {
			inportFromOld();
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

	@Background
	void inportFromOld() {
		String path = SqlImport.getDbPath(getActivity());
		File fl = new File(path);
		if (fl.exists()) {
			List<OldNote> data = new SqlImport(getActivity()).getAllNoteLists();
			for (OldNote itm : data) {
				Note newItem = new Note();
				newItem.setText(itm.getText());
				Date dt;
				try {
					dt = new Date(itm.getDate());
				} catch (Exception ignore) {
					dt = new Date();
				}
				newItem.setDate(dt);
				helper.createNote(newItem);
			}
			notifyLoaded(data.size());
		}
	}

	@UiThread
	void notifyLoaded(int count) {
		if (count > 0)
			Toast.makeText(getActivity(), R.string.restart_apply, Toast.LENGTH_SHORT);
	}
}
