package ru.megazlo.fastnote.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import ru.megazlo.fastnote.Mfm;
import ru.megazlo.fastnote.component.NoteData;
import ru.megazlo.fastnote.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public final class Sets {
	public static final String dbPath = "dbase.db";
	public static final String dbPathBak = "fastnotes.bak";

	// ----------------------- Настройки -----------------------
	public static Boolean FULL_SCR;// fullscr
	public static Integer ORIENT_TYPE;// orientype
	public static Integer FONT_SIZE;// font size
	public static Integer FONT_COLOR;// color
	public static Integer LINE_COLOR;// color

	// ----------------------- Стастическая хрень -----------------------
	public static double DIP_CONV = 0;
	public static Drawable I_NOTE, I_RIGTH, I_RIGTH_MARK, I_PLUS;

	public static java.text.DateFormat F_TIME;
	public static java.text.DateFormat F_DATE;
	// ----------------------------------------------------------------------
	public static ArrayList<NoteData> DAT;

	public static void load(SharedPreferences prf, Mfm frm) {
		if (DAT != null) {
			applySets(frm);
			return;
		} else
			DAT = new ArrayList<NoteData>();
		F_DATE = DateFormat.getDateFormat(frm);
		F_TIME = DateFormat.getTimeFormat(frm);
		DIP_CONV = frm.getResources().getDisplayMetrics().densityDpi / (float) DisplayMetrics.DENSITY_DEFAULT;

		FULL_SCR = prf.getBoolean("FULL_SCR", false);
		ORIENT_TYPE = prf.getInt("ORIENT_TYPE", ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		FONT_SIZE = prf.getInt("FONT_SIZE", 22);
		FONT_COLOR = prf.getInt("FONT_COLOR", Color.BLACK);
		LINE_COLOR = prf.getInt("LINE_COLOR", 0x800000FF);

		loadRes(frm);
		File fl = frm.getExternalFilesDir(null);
		File barFl = new File(fl, dbPath);
		if (!barFl.exists())
			restore(frm);
		applySets(frm);
	}

	public static void save(SharedPreferences prf) {
		SharedPreferences.Editor edit = prf.edit();
		edit.putBoolean("FULL_SCR", FULL_SCR);
		edit.putInt("ORIENT_TYPE", ORIENT_TYPE);
		edit.putInt("FONT_SIZE", FONT_SIZE);
		edit.putInt("FONT_COLOR", FONT_COLOR);
		edit.putInt("LINE_COLOR", LINE_COLOR);
		edit.commit();
	}

	private static void loadRes(Mfm frm) {
		Resources res = frm.getResources();
		I_NOTE = res.getDrawable(R.drawable.inote);
		I_RIGTH = res.getDrawable(R.drawable.rigth);
		I_RIGTH_MARK = res.getDrawable(R.drawable.rigth_mark);
		I_PLUS = res.getDrawable(R.drawable.plus_64);
	}

	public static void applySets(Activity act) {
		if (Sets.FULL_SCR) {
			int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
			act.getWindow().setFlags(flg, flg);
		}
		act.setRequestedOrientation(Sets.ORIENT_TYPE);
	}

	public static void backup(Activity ac) {
		try {
			SqlBase.insertSets();
			File sd = Environment.getExternalStorageDirectory();
			File data = ac.getExternalFilesDir(null);
			if (sd.canWrite()) {
				File currentDB = new File(data, dbPath);
				File bakDir = new File(sd, "backup");
				if (!bakDir.exists())
					bakDir.mkdir();
				File backupDB = new File(bakDir, dbPathBak);
				if (currentDB.exists()) {
					if (backupDB.exists())
						backupDB.delete();
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {
		}
	}

	public static void restore(Activity act) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File bakDir = new File(sd, "backup");
			File backupDB = new File(bakDir, dbPathBak);
			File currentDB = new File(act.getExternalFilesDir(null), dbPath);
			if (backupDB.exists()) {
				SqlBase.restoreSets(backupDB.getPath(), act);
				FileChannel src = new FileInputStream(backupDB).getChannel();
				FileChannel dst = new FileOutputStream(currentDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
			}
		} catch (Exception e) {
		}
	}
}
