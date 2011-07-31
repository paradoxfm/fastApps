package ru.megazlo.fastfilehd.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetChecker {
	public static boolean isOnline(Activity act) {
		ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo inf = cm.getActiveNetworkInfo();
		try {
			return inf != null;
		} catch (Exception e) {
			return false;
		}
	}
}