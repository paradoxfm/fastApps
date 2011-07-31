package ru.megazlo.fastfile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Locale;

import ru.megazlo.fastfile.R;
import ru.megazlo.fastfile.fmMain;
import ru.megazlo.fastfile.components.RowData;
import ru.megazlo.fastfile.components.filerow.FileList;
import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

public class Sets {

	public static final String dbPath = "dbase.db";
	public static final String dbPathBak = "fastfileftp.bak";

	public static Boolean OPEN_THIS;// open ext
	public static Boolean SHOW_HIDDEN;// hidden
	public static Boolean SHOW_IMG;// show images preview
	public static Boolean SHOW_MP3;// show mp3 cover preview
	public static Boolean SHOW_APK;// show apk preview
	public static File HOME_PATH;// homedir

	public static Boolean FULL_SCR;// fullscr
	public static Boolean ANIMATE;// animate
	public static Integer ORIENT_TYPE;// orientype

	// -------------------------------------------
	public static LayoutAnimationController LIST_ANIM;
	public static Drawable I_FOLD, I_CHK, I_UNCHK;
	public static Drawable I_FILE_DOC, I_FILE_IMG, I_FILE_MOV, I_FILE_MUS, I_FILE_NON, I_FILE_BIN;
	public static String[] FILE_DOC, FILE_IMG, FILE_MOV, FILE_MUS, FILE_BIN;
	public static Drawable P_FTP, P_PDA, P_SMB, P_SRH;
	public static ArrayList<FtpRecort> FTPS = new ArrayList<FtpRecort>();
	public static SQLiteDatabase SQLITE;
	public static java.text.DateFormat F_TIME;
	public static java.text.DateFormat F_DATE;
	// ------------------------------------------- Модели данных
	public static ArrayList<RowData> dat = new ArrayList<RowData>();
	// -------------------------------------------
	public static float DIP_CONV;

	public static void load(SharedPreferences prf, Activity res) {
		DIP_CONV = res.getResources().getDisplayMetrics().densityDpi / (float) DisplayMetrics.DENSITY_DEFAULT;
		if (dat.size() != 0) {
			applySets(res);
			return;
		}
		F_DATE = DateFormat.getDateFormat(res);
		F_TIME = DateFormat.getTimeFormat(res);
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
		loadRes(res);
		applySets(res);
		File fl = res.getExternalFilesDir(null);
		File barFl = new File(fl, dbPath);
		if (!barFl.exists()) {
			restoreBackUpFTP(res);
			applySets(res);
		}
		if (fl != null)
			loadDB(fl + "/" + dbPath);
	}

	public static void applySets(Activity res) {
		if (Sets.FULL_SCR) {
			int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
			res.getWindow().setFlags(flg, flg);
		}
		res.setRequestedOrientation(Sets.ORIENT_TYPE);
	}

	public static void save(SharedPreferences prf) {
		SharedPreferences.Editor edit = prf.edit();
		edit.putBoolean("OPEN_THIS", OPEN_THIS);
		edit.putString("HOME_PATH", HOME_PATH.getPath());
		edit.putBoolean("SHOW_HIDDEN", SHOW_HIDDEN);
		edit.putBoolean("SHOW_IMG", SHOW_IMG);
		edit.putBoolean("SHOW_MP3", SHOW_MP3);
		edit.putBoolean("SHOW_APK", SHOW_APK);
		edit.putBoolean("FULL_SCR", FULL_SCR);
		edit.putBoolean("ANIMATE", ANIMATE);
		edit.putInt("ORIENT_TYPE", ORIENT_TYPE);
		edit.commit();
	}

	private static void loadRes(Activity act) {
		Resources res = act.getResources();
		LIST_ANIM = createAnim(act);
		I_FOLD = res.getDrawable(R.drawable.folder);

		I_FILE_NON = res.getDrawable(R.drawable.file_none);
		I_FILE_DOC = res.getDrawable(R.drawable.file_doc);
		I_FILE_IMG = res.getDrawable(R.drawable.file_img);
		I_FILE_MOV = res.getDrawable(R.drawable.file_mov);
		I_FILE_MUS = res.getDrawable(R.drawable.file_mus);
		I_FILE_BIN = res.getDrawable(R.drawable.file_bin);

		FILE_DOC = res.getStringArray(R.array.docs);
		FILE_IMG = res.getStringArray(R.array.imgs);
		FILE_MOV = res.getStringArray(R.array.vids);
		FILE_MUS = res.getStringArray(R.array.muss);
		FILE_BIN = res.getStringArray(R.array.bin);

		int i;
		for (i = 0; i < Sets.FILE_DOC.length; i++)
			Sets.FILE_DOC[i] = Sets.FILE_DOC[i].intern();
		for (i = 0; i < Sets.FILE_IMG.length; i++)
			Sets.FILE_IMG[i] = Sets.FILE_IMG[i].intern();
		for (i = 0; i < Sets.FILE_MUS.length; i++)
			Sets.FILE_MUS[i] = Sets.FILE_MUS[i].intern();
		for (i = 0; i < Sets.FILE_BIN.length; i++)
			Sets.FILE_BIN[i] = Sets.FILE_BIN[i].intern();
		for (i = 0; i < Sets.FILE_MOV.length; i++)
			Sets.FILE_MOV[i] = Sets.FILE_MOV[i].intern();

		I_CHK = res.getDrawable(R.drawable.check);
		I_UNCHK = res.getDrawable(R.drawable.empty);

		P_FTP = res.getDrawable(R.drawable.prot_ftp);
		P_PDA = res.getDrawable(R.drawable.prot_pda);
		P_SMB = res.getDrawable(R.drawable.prot_smb);
		P_SRH = res.getDrawable(R.drawable.prot_srh);
	}

	private static LayoutAnimationController createAnim(Activity act) {
		AnimationSet set = new AnimationSet(true);
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(50);
		set.addAnimation(animation);
		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(100);
		set.addAnimation(animation);
		// return AnimationUtils.loadLayoutAnimation(act, R.anim.layanim);
		return new LayoutAnimationController(set, 0.5f);
	}

	public static void restoreLists(fmMain act) {
		for (RowData dt : dat)
			act.scrv.addView(new FileList(act, dt, true));
	}

	public static void loadDB(String path) {
		Boolean test = new File(path).exists();
		SQLITE = SQLiteDatabase.openOrCreateDatabase(path, null);
		SQLITE.setVersion(1);
		SQLITE.setLocale(Locale.getDefault());
		SQLITE.setLockingEnabled(true);
		if (!test) {
			SQLITE.execSQL("create table ftp_con (id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "server TEXT, anonim TEXT, user TEXT);");
			SQLITE.execSQL("create table bksets (setnam TEXT, setval TEXT);");
		}
		Cursor cur = SQLITE.query("ftp_con", null, null, null, null, null, null);
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			FtpRecort rec = new FtpRecort();
			rec.ID = Integer.parseInt(cur.getString(0));
			rec.server = cur.getString(1);
			rec.anonim = Boolean.parseBoolean(cur.getString(2));
			rec.user = cur.getString(3);
			FTPS.add(rec);
			cur.moveToNext();
		}
		cur.close();
	}

	public static void deleteFtpRec(FtpRecort rec) {
		SQLITE.delete("ftp_con", "id=?", new String[] { Integer.toString(rec.ID) });
		FTPS.remove(rec);
	}

	public static void insertFtpRec(String hst, String usr, boolean checked) {
		FtpRecort rec = new FtpRecort();
		rec.server = hst;
		rec.user = usr;
		rec.anonim = checked;
		Boolean srh = true;
		for (int i = 0; i < FTPS.size(); i++)
			if (FTPS.get(i).server.equals(rec.server)) {
				srh = false;
				break;
			}
		if (srh) {
			ContentValues val = new ContentValues();
			val.put("server", rec.server);
			val.put("anonim", Boolean.toString(rec.anonim));
			val.put("user", rec.user);
			SQLITE.insert("ftp_con", "", val);
			FTPS.add(rec);
		}
	}

	private static void insSets() {
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
	}

	private static void selSets(String path, Activity act) {
		SQLiteDatabase rest = SQLiteDatabase.openOrCreateDatabase(path, null);
		Cursor cur = rest.query("bksets", null, null, null, null, null, null);
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
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
		save(act.getPreferences(0));
	}

	public static void backUpFTP(Activity ac) {
		try {
			insSets();
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

	public static void restoreBackUpFTP(Activity ac) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File bakDir = new File(sd, "backup");
			File backupDB = new File(bakDir, dbPathBak);
			File currentDB = new File(ac.getExternalFilesDir(null), dbPath);
			if (backupDB.exists()) {
				selSets(backupDB.getPath(), ac);
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
