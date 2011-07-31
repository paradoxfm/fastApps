package ru.megazlo.fastfilehd.util;

import ru.megazlo.fastfilehd.R;
import ru.megazlo.fastfilehd.fmMain;
import ru.megazlo.fastfilehd.fmSettings;
import ru.megazlo.fastfilehd.components.RowDataFTP;
import ru.megazlo.fastfilehd.components.RowDataSD;
import ru.megazlo.fastfilehd.components.list.ListFTP;
import ru.megazlo.fastfilehd.components.list.ListSDC;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class MenuChecker {

	private static Intent INT_PRF;

	public static boolean itemClick(fmMain act, int id) {
		switch (id) {
		case R.id.appsett:
			startAct(act, INT_PRF, fmSettings.class);
			return true;
		case R.id.tutor:
			showHelp();
			return true;
		case R.id.ftp:
			if (NetChecker.isOnline(act))
				insertListFTP(act);
			else
				Toast.makeText(act, R.string.n_act_con, Toast.LENGTH_SHORT).show();
			return true;
		case R.id.sdcard:
			insertListSD(act);
			return true;
		case R.id.quit:
			exitApp(act);
			return true;

			// case R.id.getroot:
			// try {
			// Runtime.getRuntime().exec("su");
			// } catch (IOException e) {
			// }
			// return true;

		case R.id.samba:
			if (NetChecker.isOnline(act))
				Toast.makeText(act, R.string.future, Toast.LENGTH_SHORT).show();
			return true;

		default:
			return false;
		}
	}

	public static void showHelp() {
		LayoutInflater factory = LayoutInflater.from(fmMain.CONTEXT);
		final View v = factory.inflate(R.layout.help, null);
		new AlertDialog.Builder(fmMain.CONTEXT).setTitle(R.string.mn_tutor).setIcon(R.drawable.ic_menu_light).setView(v)
				.create().show();
	}

	public static void exitApp(fmMain act) {
		INT_PRF = null;
		Sets.dat.clear();
		act.finish();
		fmMain.CONTEXT = act = null;
		System.exit(0);
	}

	public static void insertListSD(fmMain act) {
		RowDataSD sd = new RowDataSD();
		if (Sets.dat.size() > 0)
			sd.PATH = ((RowDataSD) Sets.dat.get(0)).PATH;
		ListSDC lsd = new ListSDC(act, sd, false);
		Sets.dat.add(sd);
		act.setCurrentList(lsd);
	}

	public static void insertListFTP(fmMain act) {
		RowDataFTP sd = new RowDataFTP();
		ListFTP lsd = new ListFTP(act, sd, false);
		Sets.dat.add(sd);
		lsd.connect();
	}

	// private static void setNewList(fmMain act, ListBase lst) {
	// act.setCurrentList(lst);
	// // act.scrv.addView(lst);
	// // act.scrv.setDisplayedChild(act.scrv.getDisplayedChild());
	// }

	private static boolean startAct(Activity act, Intent intn, Class<?> cl) {
		if (intn == null) {
			intn = new Intent();
			intn.setClass(act, cl);
		}
		act.startActivity(intn);
		return true;
	}

}
