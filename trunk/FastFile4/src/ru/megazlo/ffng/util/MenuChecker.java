package ru.megazlo.ffng.util;

import ru.megazlo.ffng.R;
import ru.megazlo.ffng.fmMain;
import ru.megazlo.ffng.fmSettings;
import ru.megazlo.ffng.components.RowData;
import ru.megazlo.ffng.components.RowDataFTP;
import ru.megazlo.ffng.components.RowDataLAN;
import ru.megazlo.ffng.components.RowDataSD;
import ru.megazlo.ffng.components.filerow.FileList;
import ru.megazlo.ffng.engine.BaseEngine;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
				insertList(act, new RowDataFTP());
			else
				Toast.makeText(act, R.string.n_act_con, Toast.LENGTH_SHORT).show();
			return true;
		case R.id.sdcard:
			insertList(act, new RowDataSD());
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
			// Toast.makeText(act, R.string.future, Toast.LENGTH_SHORT).show();
			if (NetChecker.isOnline(act)) {
				insertList(act, new RowDataLAN());
				Toast.makeText(act, R.string.a_opt, Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(act, R.string.n_act_con, Toast.LENGTH_SHORT).show();
			return true;
		case R.id.closetab:
			remList(act.getCurEng().getList());
			return true;
		default:
			return false;
		}
	}

	public static void showHelp() {
		LayoutInflater factory = LayoutInflater.from(fmMain.CONTEXT);
		final View v = factory.inflate(R.layout.help, null);
		new AlertDialog.Builder(fmMain.CONTEXT).setTitle(R.string.mn_tutor).setIcon(android.R.drawable.ic_menu_help).setView(v)
				.create().show();
	}

	private static void exitApp(fmMain act) {
		INT_PRF = null;
		Sets.dat.clear();
		act.finish();
		fmMain.CONTEXT = act = null;
		System.exit(0);
	}

	public static void insertList(Context c, RowData sd) {
		FileList lsd = new FileList(c, sd, false);
		Sets.dat.add(sd);
		setNewList((fmMain) c, lsd);
		lsd.getEngine().exec(BaseEngine.CMD_CON);
	}

	private static void setNewList(fmMain act, View lst) {
		act.scrv.addView(lst);
		act.scrv.scrollRight();
	}

	public static void remList(FileList lst) {
		if (fmMain.CONTEXT.scrv.getChildCount() == 1)
			exitApp(fmMain.CONTEXT);
		Sets.dat.remove(lst.getEngine().getDat());
		removeList(fmMain.CONTEXT, lst);
	}

	private static void removeList(fmMain act, FileList lst) {
		int dsp = act.scrv.getDisplayedChild();
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
