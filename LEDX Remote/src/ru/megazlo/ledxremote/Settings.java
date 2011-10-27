package ru.megazlo.ledxremote;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ru.megazlo.ledxremote.util.Sets;
import ru.megazlo.ledxremote.util.Util;
import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;

public class Settings extends PreferenceActivity implements Preference.OnPreferenceChangeListener,
		Preference.OnPreferenceClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		initPrefs();
	}

	@Override
	public void onBackPressed() {
		Main.getInst().saveSets();
		super.onBackPressed();
	}

	private void initPrefs() {
		CheckBoxPreference prf1 = (CheckBoxPreference) this.findPreference("broadcast");
		prf1.setChecked(Sets.BY_IP);
		prf1.setOnPreferenceChangeListener(this);

		EditTextPreference prf2 = (EditTextPreference) this.findPreference("ipadr");
		prf2.setText(Sets.IP_ADR.toString());
		prf2.setOnPreferenceChangeListener(this);

		EditTextPreference prf3 = (EditTextPreference) this.findPreference("port");
		prf3.setText(Integer.toString(Sets.PORT));
		prf3.setOnPreferenceChangeListener(this);

		this.findPreference("about").setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference pref) {
		String key = pref.getKey();
		if (key.equals("about")) {
			LayoutInflater factory = LayoutInflater.from(this);
			final View formcon = factory.inflate(R.layout.about, null);
			new AlertDialog.Builder(this).setTitle(R.string.about).setIcon(R.drawable.logo).setView(formcon).create().show();
			return true;
		}
		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference pref, Object newValue) {
		String key = pref.getKey();
		if (key.equals("broadcast")) {
			Sets.BY_IP = Boolean.parseBoolean(newValue.toString());
			return true;
		}
		if (key.equals("ipadr")) {
			try {
				String nv = newValue.toString();
				byte[] arr = Util.parceIP(nv);
				Sets.IP_ADR = InetAddress.getByAddress(arr);
			} catch (UnknownHostException e) {
				return false;
			}
			return true;
		}
		if (key.equals("port")) {
			Sets.PORT = Integer.parseInt(newValue.toString());
			return true;
		}
		return false;
	}
}
