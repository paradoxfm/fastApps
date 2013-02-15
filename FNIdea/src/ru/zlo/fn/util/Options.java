package ru.zlo.fn.util;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultInt;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Options {
	@DefaultBoolean(false)
	boolean fullScreen();

	@DefaultInt(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
	int orientation();

	@DefaultInt(22)
	int fontSize();

	@DefaultInt(Color.BLACK)
	int fontColor();

	@DefaultInt(0xFF08a2ce)
	int lineColor();
}

/*
@EBean
public class Options {
	private final static String name = "fastnote";
	private final static String dbPathBak = "fastnotes.bak";

	// ----------------------- Настройки -----------------------
	public static Boolean FULL_SCR;// fullscr
	public static Integer ORIENT_TYPE;// orientype
	public static Integer FONT_SIZE;// font size
	public static Integer FONT_COLOR;// color
	public static Integer LINE_COLOR;// color
	// ----------------------- Стастическая хрень -----------------------


	@RootContext
	Context context;

	@AfterInject
	void loadOptions() {
		SharedPreferences p = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		FULL_SCR = p.getBoolean("FULL_SCR", false);
		ORIENT_TYPE = p.getInt("ORIENT_TYPE", ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		FONT_SIZE = p.getInt("FONT_SIZE", 22);
		FONT_COLOR = p.getInt("FONT_COLOR", Color.BLACK);
		LINE_COLOR = p.getInt("LINE_COLOR", 0xFF08a2ce);
	}

	public static void save(Context context) {
		SharedPreferences.Editor edit = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
		edit.putBoolean("FULL_SCR", FULL_SCR);
		edit.putInt("ORIENT_TYPE", ORIENT_TYPE);
		edit.putInt("FONT_SIZE", FONT_SIZE);
		edit.putInt("FONT_COLOR", FONT_COLOR);
		edit.putInt("LINE_COLOR", LINE_COLOR);
		edit.commit();
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
	}
}
*/
