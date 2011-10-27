package ru.megazlo.ledxremote.util;

import ru.megazlo.ledxremote.Main;
import ru.megazlo.ledxremote.R;
import ru.megazlo.ledxremote.Settings;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

public abstract class MenuCheck {

	private static Intent INT_PRF;

	public static boolean itemClick(Main c, int id) {
		switch (id) {
		case R.id.appsett:
			if (INT_PRF == null) {
				INT_PRF = new Intent();
				INT_PRF.setClass(c, Settings.class);
			}
			c.startActivity(INT_PRF);
			break;
		case R.id.quit:
			c.saveSets();
			System.exit(0);

		case R.id.tutor:
			LayoutInflater factory = LayoutInflater.from(c);
			final View v = factory.inflate(R.layout.help, null);
			new AlertDialog.Builder(c).setTitle(R.string.tutor).setIcon(R.drawable.ic_menu_light).setView(v).create().show();
			break;
		default:
			break;
		}
		return true;
	}
}
