package ru.zlo.fn.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import ru.zlo.fn.R;
import ru.zlo.fn.SAct_;

public class MenuChecker {

	public static boolean itemClick(Context context, int id) {
		switch (id) {
			case R.id.appsett:
				context.startActivity(new Intent(context, SAct_.class));
				return true;
			case R.id.tutor:
				showHelp(context);
				return true;
			default:
				return false;
		}
	}

	public static void showHelp(Context context) {
		LayoutInflater factory = LayoutInflater.from(context);
		final View v = factory.inflate(R.layout.help, null);
		new AlertDialog.Builder(context).setTitle(R.string.tutor).setIcon(android.R.drawable.ic_menu_help).setView(v)
				.create().show();
	}
}
