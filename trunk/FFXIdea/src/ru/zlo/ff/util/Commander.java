package ru.zlo.ff.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import ru.zlo.ff.MAct;
import ru.zlo.ff.R;
import ru.zlo.ff.SetAct;
import ru.zlo.ff.components.RowDataSD;
import ru.zlo.ff.engine.EngPool;

import java.io.File;

public class Commander {

	public static boolean itemClick(MAct act, int id) {
		switch (id) {
			case R.id.appsett:
				startAct(act, SetAct.class);
				return true;
			case R.id.tutor:
				showHelp(act);
				return true;
			case R.id.quit:
				exitApp();
				return true;
			default:
				return false;
		}
	}

	public static void showHelp(Context context) {
		LayoutInflater factory = LayoutInflater.from(context);
		final View v = factory.inflate(R.layout.help, null);
		new AlertDialog.Builder(context).setTitle(R.string.mn_tutor).setIcon(android.R.drawable.ic_menu_help).setView(v)
				.create().show();
	}

	public static void exitApp() {
		System.exit(0);
	}

	public static void createPanes(Context context, String path) {
		if (path != null) {
			RowDataSD dat = new RowDataSD();
			dat.PATH = new File(path);
			EngPool.Inst().addEngine(dat, context);
		} else {
			EngPool.Inst().addEngine(new RowDataSD(), context);
			EngPool.Inst().addEngine(new RowDataSD(), context);
		}
	}

	private static void startAct(Activity act, Class<?> cls) {
		Intent intent = new Intent(act, cls);
		act.startActivity(intent);
	}
}
