package ru.megazlo.fastfile.util;

import ru.megazlo.fastfile.R;
import ru.megazlo.fastfile.fmMain;
import ru.megazlo.fastfile.fmSettings;
import ru.megazlo.fastfile.components.RowDataFTP;
import ru.megazlo.fastfile.components.RowDataLAN;
import ru.megazlo.fastfile.components.RowDataSD;
import ru.megazlo.fastfile.components.filerow.FileList;
import ru.megazlo.fastfile.engine.BaseEngine;
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
				insertListLAN(act);
			else
				Toast.makeText(act, R.string.n_act_con, Toast.LENGTH_SHORT).show();
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

	private static void exitApp(fmMain act) {
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
		FileList lsd = new FileList(act, sd, false);
		Sets.dat.add(sd);
		setNewList(act, lsd);
	}

	public static void insertListFTP(fmMain act) {
		RowDataFTP sd = new RowDataFTP();
		FileList lsd = new FileList(act, sd, false);
		Sets.dat.add(sd);
		setNewList(act, lsd);
		lsd.getEngine().exec(BaseEngine.CMD_CON);
	}

	public static void insertListLAN(fmMain act) {
		RowDataLAN sd = new RowDataLAN();
		FileList lsd = new FileList(act, sd, false);
		Sets.dat.add(sd);
		setNewList(act, lsd);
		// lsd.getEngine().exec(BaseEngine.CMD_CON);
	}

	private static void setNewList(fmMain act, View lst) {
		act.scrv.addView(lst);
		act.scrv.scrollToScreen(act.scrv.getChildCount() - 1);
	}

	public static void remList(FileList lst) {
		if (fmMain.CONTEXT.scrv.getChildCount() == 1)
			exitApp(fmMain.CONTEXT);
		Sets.dat.remove(lst.getEngine().getDat());
		removeList(fmMain.CONTEXT, lst);
	}

	private static void removeList(fmMain act, FileList lst) {
		int dsp = fmMain.CONTEXT.scrv.getDisplayedChild();
		if (dsp != 0)
			act.scrv.setCurrentScreen(dsp - 1);
		act.scrv.removeView(lst);
	}

	private static boolean startAct(Activity act, Intent intn, Class<?> cl) {
		if (intn == null) {
			intn = new Intent();
			intn.setClass(act, cl);
		}
		act.startActivity(intn);
		return true;
	}

}
