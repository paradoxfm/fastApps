package ru.zlo.ff.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import ru.zlo.ff.MAct;
import ru.zlo.ff.R;
import ru.zlo.ff.SAct_;

public class Commander {

	public static boolean itemClick(MAct act, int id) {
		switch (id) {
			case R.id.appsett:
				act.startActivity(new Intent(act, SAct_.class));
				return true;
			case R.id.tutor:
				showHelp(act);
				return true;
			case R.id.quit:
				System.exit(0);
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
}
