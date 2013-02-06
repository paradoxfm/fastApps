package ru.zlo.ff.util;

import android.database.sqlite.SQLiteDatabase;
import ru.zlo.ff.MAct;
import ru.zlo.ff.components.RowData;
import ru.zlo.ff.engine.EngPool;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class Sets {

	// -------------------------------------------
	public static SQLiteDatabase SQLITE;
	// ------------------------------------------- Модели данных
	public static ArrayList<RowData> dat = new ArrayList<RowData>();

/*	public static void load(SharedPreferences prf, Activity res) {
		if (dat.size() != 0) {
			applySets(res);
			return;
		}
		ISGRID = prf.getBoolean("ISGRID", false);
		OPEN_THIS = prf.getBoolean("OPEN_THIS", false);
		HOME_PATH = new File(prf.getString("HOME_PATH", "/"));
		if (!HOME_PATH.exists() || !HOME_PATH.canRead())
			HOME_PATH = new File("/");

		SHOW_HIDDEN = prf.getBoolean("SHOW_HIDDEN", false);
		SHOW_IMG = prf.getBoolean("SHOW_IMG", true);
		SHOW_MP3 = prf.getBoolean("SHOW_MP3", true);
		SHOW_APK = prf.getBoolean("SHOW_APK", true);

		FULL_SCR = prf.getBoolean("FULL_SCR", false);
		ANIMATE = prf.getBoolean("ANIMATE", false);
		ORIENT_TYPE = prf.getInt("ORIENT_TYPE", ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		applySets(res);
		File fl = res.getExternalFilesDir(null);
		File barFl = new File(fl, dbPath);
		if (!barFl.exists())
			applySets(res);
		if (fl != null)
			loadDB(fl + "/" + dbPath);
	}*/

	public static void restoreLists(MAct act) {
		for (RowData dt : dat)
			EngPool.Inst().addEngine(dt);
	}

	public static void loadDB(String path) {
		Boolean test = new File(path).exists();
		SQLITE = SQLiteDatabase.openOrCreateDatabase(path, null);
		SQLITE.setVersion(1);
		SQLITE.setLocale(Locale.getDefault());
		SQLITE.setLockingEnabled(true);
		if (!test)
			SQLITE.execSQL("create table bksets (setnam TEXT, setval TEXT);");
	}

	/*private static void insSets() {
		SQLITE.delete("bksets", null, null);
		ContentValues val = new ContentValues();
		val.put("setnam", "OPEN_THIS");
		val.put("setval", Boolean.toString(OPEN_THIS));
		SQLITE.insert("bksets", "", val);
		val.clear();
		val.put("setnam", "HOME_PATH");
		val.put("setval", HOME_PATH.getPath());
		SQLITE.insert("bksets", "", val);
		val.clear();
		val.put("setnam", "SHOW_HIDDEN");
		val.put("setval", Boolean.toString(SHOW_HIDDEN));
		SQLITE.insert("bksets", "", val);
		val.clear();
		val.put("setnam", "FULL_SCR");
		val.put("setval", Boolean.toString(FULL_SCR));
		SQLITE.insert("bksets", "", val);
		val.clear();
		val.put("setnam", "ANIMATE");
		val.put("setval", Boolean.toString(ANIMATE));
		SQLITE.insert("bksets", "", val);
		val.clear();
		val.put("setnam", "ORIENT_TYPE");
		val.put("setval", Integer.toString(ORIENT_TYPE));
		SQLITE.insert("bksets", "", val);
	}*/

	/*private static void selSets(String path, Activity act) {
		SQLiteDatabase rest = SQLiteDatabase.openOrCreateDatabase(path, null);
		Cursor cur = rest.query("bksets", null, null, null, null, null, null);
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			String nm = cur.getString(0);
			String vl = cur.getString(1);
			if (nm.equals("OPEN_THIS"))
				OPEN_THIS = Boolean.parseBoolean(vl);
			if (nm.equals("HOME_PATH"))
				HOME_PATH = new File(vl);
			if (!HOME_PATH.exists() || !HOME_PATH.canRead())
				HOME_PATH = new File("/");
			if (nm.equals("SHOW_HIDDEN"))
				SHOW_HIDDEN = Boolean.parseBoolean(vl);
			if (nm.equals("FULL_SCR"))
				FULL_SCR = Boolean.parseBoolean(vl);
			if (nm.equals("ANIMATE"))
				ANIMATE = Boolean.parseBoolean(vl);
			if (nm.equals("ORIENT_TYPE"))
				ORIENT_TYPE = Integer.parseInt(vl);
			cur.moveToNext();
		}
		cur.close();
		rest.close();
		//save(act.getPreferences(0));
	}*/
}
