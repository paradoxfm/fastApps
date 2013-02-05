package ru.zlo.ff.util;

import ru.zlo.ff.R;
import ru.zlo.ff.MAct;
import ru.zlo.ff.SetAct;
import ru.zlo.ff.components.RowData;
import ru.zlo.ff.components.RowDataSD;
import ru.zlo.ff.components.filerow.FileList;
import ru.zlo.ff.engine.BaseEngine;
import ru.zlo.ff.engine.EngPool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

public class MenuChecker {

	private static Intent INT_PRF;

	public static boolean itemClick(MAct act, int id) {
		switch (id) {
		case R.id.appsett2:
		case R.id.appsett:
			startAct(act, INT_PRF, SetAct.class);
			return true;
		case R.id.tutor2:
		case R.id.tutor:
			showHelp();
			return true;
		case R.id.sdcard:
			insertList(new RowDataSD());
			return true;
		case R.id.quit2:
		case R.id.quit:
			exitApp(act);
			return true;
		case R.id.closetab:
			remList(act.getCurEng().getList());
			return true;
		case R.id.menu_set_widget_path:
			MAct.I.configWidget();
			return true;
		default:
			return false;
		}
	}

	public static void showHelp() {
		LayoutInflater factory = LayoutInflater.from(MAct.I);
		final View v = factory.inflate(R.layout.help, null);
		new AlertDialog.Builder(MAct.I).setTitle(R.string.mn_tutor).setIcon(android.R.drawable.ic_menu_help).setView(v)
				.create().show();
	}

	public static void exitApp(MAct act) {
		INT_PRF = null;
		Sets.dat.clear();
		act.finish();
		MAct.I = act = null;
		System.exit(0);
	}

	public static void insertList(RowData dat) {
		EngPool.Inst().addEngine(dat);
		Sets.dat.add(dat);
		EngPool.Inst().getEngine(EngPool.Inst().count() - 1).exec(BaseEngine.CMD_CON);
		MAct.I.scrollToNew();
	}

	public static void remList(FileList lst) {
		if (EngPool.Inst().count() == 1)
			exitApp(MAct.I);
		EngPool.Inst().removeCurrent(MAct.I.getCurEng());
		MAct.I.removeFragment();
		Sets.dat.remove(lst.getEngine().getDat());
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
