package ru.zlo.fn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import ru.zlo.fn.MAct;
import ru.zlo.fn.component.NoteData;
import ru.zlo.fn.R;
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
	/*public static final String dbPath = "dbase.db";
	public static final String dbPathBak = "fastnotes.bak";

	// ----------------------- Стастическая хрень -----------------------
	public static double DIP_CONV = 0;

	public static java.text.DateFormat F_TIME;
	public static java.text.DateFormat F_DATE;
	// ----------------------------------------------------------------------
	public static ArrayList<NoteData> DAT;

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
		} catch (Exception ignored) {
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
		} catch (Exception ignored) {
		}
	}*/
}
