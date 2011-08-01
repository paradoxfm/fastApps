package ru.megazlo.fastnote.util;

import ru.megazlo.fastnote.fmMain;
import ru.megazlo.fastnote.fmSettings;
import ru.megazlo.fastnote.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

public class MenuChecker {

	private static Intent INT_PRF;

	public static boolean itemClick(fmMain act, int id) {
		switch (id) {
		case R.id.appsett:
			startAct(act, INT_PRF, fmSettings.class);
			return true;
		case R.id.quit:
			exitApp(act);
			return true;
		case R.id.tutor:
			showHelp();
			return true;
		default:
			return false;
		}
	}

	private static void exitApp(fmMain act) {
		System.exit(0);
	}

	private static boolean startAct(Activity act, Intent intn, Class<?> cl) {
		if (intn == null) {
			intn = new Intent();
			intn.setClass(act, cl);
		}
		act.startActivity(intn);
		return true;
	}

	public static void showHelp() {
		LayoutInflater factory = LayoutInflater.from(fmMain.CONTEXT);
		final View v = factory.inflate(R.layout.help, null);
		new AlertDialog.Builder(fmMain.CONTEXT).setTitle(R.string.tutor).setIcon(R.drawable.ic_menu_light).setView(v)
				.create().show();
	}
}
