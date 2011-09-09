package ru.megazlo.fastnote.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ru.megazlo.fastnote.R;
import ru.megazlo.fastnote.fmMain;
import ru.megazlo.fastnote.component.NoteData;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class SqlBase {
	private final static String table = "tb_notes";
	private final static String settab = "bksets";
	private static SQLiteDatabase SQLITE;

	public static ArrayList<NoteData> getList(File ext, String srhQuery) {
		String req = srhQuery == null ? null : "Title LIKE '%" + srhQuery + "%' or Txt LIKE '%" + srhQuery + "%'";
		if (ext != null && connectBase(ext)) {
			Cursor cur = SQLITE.query(table, new String[] { "ID", "Ordr", "WCount", "Date", "Title" }, req, null, null, null,
					null);
			return cursorReader(cur);
		}
		Toast.makeText(fmMain.CONTEXT, R.string.db_broken, Toast.LENGTH_SHORT).show();	
		return new ArrayList<NoteData>();
	}

	private static ArrayList<NoteData> cursorReader(Cursor c) {
		ArrayList<NoteData> rez = new ArrayList<NoteData>();
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			int id = c.getInt(0);
			int order = c.getInt(1);
			int wcnt = c.getInt(2);
			Date date = new Date(Date.parse(c.getString(3)));
			String title = c.getString(4);
			NoteData dat = new NoteData(id, order, wcnt, date, title);
			rez.add(dat);
			c.moveToNext();
		}
		c.close();
		return rez;
	}

	public static Boolean connectBase(File ext) {
		File bd = new File(ext.getPath() + "/dbase.db");
		Boolean test = bd.exists();
		SQLITE = SQLiteDatabase.openOrCreateDatabase(bd.getPath(), null);
		SQLITE.setVersion(1);
		SQLITE.setLocale(Locale.getDefault());
		SQLITE.setLockingEnabled(true);
		if (!test) {
			SQLITE.execSQL("create table " + table + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "Ordr INTEGER, WCount INTEGER, Date TEXT, Title TEXT, Txt TEXT);");
			SQLITE.execSQL("create table " + settab + " (setnam TEXT, setval TEXT);");
		}
		return true;
	}

	public static void deleteNote(int id) {
		SQLITE.delete(table, "id=?", new String[] { Integer.toString(id) });
	}

	public static void insertNote(NoteData dat) {
		if (SQLITE != null) {
			ContentValues val = new ContentValues();
			val.put("Ordr", dat.getOrder());
			val.put("WCount", dat.getWordCount());
			val.put("Date", dat.getDateNorm().toString());
			val.put("Title", dat.getTitle());
			val.put("Txt", "");
			SQLITE.insert(table, "", val);
			dat.setID(selectID());
		} else
			Toast.makeText(fmMain.CONTEXT, R.string.db_broken, Toast.LENGTH_SHORT).show();		
	}

	private static int selectID() {
		Cursor cur = SQLITE.rawQuery("select last_insert_rowid()", null);
		int rez = 0;
		if (cur.moveToFirst())
			rez = cur.getInt(0);
		cur.close();
		return rez;
	}

	public static String getText(int id) {
		Cursor cur = SQLITE.rawQuery("select Txt from " + table + " where id=?", new String[] { Integer.toString(id) });
		String rez = "";
		if (cur.moveToFirst())
			rez = cur.getString(0);
		cur.close();
		return rez;
	}

	public static void updateData(String text, NoteData dat) {
		ContentValues val = new ContentValues();
		val.put("Ordr", dat.getOrder());
		val.put("WCount", dat.getWordCount());
		val.put("Date", dat.getDateNorm().toString());
		val.put("Title", dat.getTitle());
		val.put("Txt", text);
		SQLITE.update(table, val, "id=?", new String[] { Integer.toString(dat.getID()) });
	}

	public static void insertSets() {
		SQLITE.delete(settab, null, null);
		String nm = "setnam", vl = "setval";
		ContentValues val = new ContentValues();

		val.put(nm, "FULL_SCR");
		val.put(vl, Boolean.toString(Sets.FULL_SCR));
		SQLITE.insert(settab, "", val);
		val.clear();

		val.put(nm, "ORIENT_TYPE");
		val.put(vl, Integer.toString(Sets.ORIENT_TYPE));
		SQLITE.insert(settab, "", val);
		val.clear();

		val.put(nm, "FONT_SIZE");
		val.put(vl, Integer.toString(Sets.FONT_SIZE));
		SQLITE.insert(settab, "", val);
		val.clear();

		val.put(nm, "FONT_COLOR");
		val.put(vl, Integer.toString(Sets.FONT_COLOR));
		SQLITE.insert(settab, "", val);
		val.clear();

		val.put(nm, "LINE_COLOR");
		val.put(vl, Integer.toString(Sets.LINE_COLOR));
		SQLITE.insert(settab, "", val);
		val.clear();
	}

	public static void restoreSets(String path, Activity act) {
		SQLiteDatabase rest = SQLiteDatabase.openOrCreateDatabase(path, null);
		Cursor cur = rest.query("bksets", null, null, null, null, null, null);
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			String nm = cur.getString(0);
			String vl = cur.getString(1);
			if (nm.equals("FULL_SCR"))
				Sets.FULL_SCR = Boolean.parseBoolean(vl);
			if (nm.equals("ORIENT_TYPE"))
				Sets.ORIENT_TYPE = Integer.parseInt(vl);
			if (nm.equals("FONT_SIZE"))
				Sets.FONT_SIZE = Integer.parseInt(vl);
			if (nm.equals("FONT_COLOR"))
				Sets.FONT_COLOR = Integer.parseInt(vl);
			if (nm.equals("LINE_COLOR"))
				Sets.LINE_COLOR = Integer.parseInt(vl);
			cur.moveToNext();
		}
		cur.close();
		rest.close();
		Sets.save(act.getPreferences(0));
	}

	public static boolean isConnected() {
		return SQLITE != null;
	}
}
