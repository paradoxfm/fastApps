package ru.megazlo.crazytest.components;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ru.megazlo.crazytest.classes.NoteData;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class dbLay {

	private final static String table = "tb_sclerosis";
	private static SQLiteDatabase db;

	public static Boolean connectBase(File ext) {
		File bd = new File(ext.getPath() + "/dbase.db");
		Boolean test = bd.exists();
		db = SQLiteDatabase.openOrCreateDatabase(bd.getPath(), null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		db.setLockingEnabled(true);
		if (!test)
			db.execSQL("create table " + table + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT, Title TEXT);");
		return true;
	}

	public static ArrayList<NoteData> getList(File ext, String srhQuery) {
		if (ext != null && connectBase(ext)) {
			Cursor cur = db.query(table, new String[] { "ID", "Date", "Title" }, null, null, null, null, null);
			return cursorReader(cur);
		}
		return new ArrayList<NoteData>();
	}

	private static ArrayList<NoteData> cursorReader(Cursor c) {
		ArrayList<NoteData> rez = new ArrayList<NoteData>();
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			NoteData dat = new NoteData(c.getInt(0), new Date(Date.parse(c.getString(1))), c.getString(2));
			rez.add(dat);
			c.moveToNext();
		}
		c.close();
		return rez;
	}

	public void insertNote(Date dat, String title) {
	}

	public static void insertNote(NoteData dat) {
		ContentValues val = new ContentValues();
		val.put("Date", dat.From.toString());
		val.put("Title", dat.Title);
		if (db != null) {
			db.insert(table, "", val);
			dat.ID = selectID();
		}// else {
			// Toast.makeText(fmMain.CONTEXT, "qwfasdf asdfasd",
			// Toast.LENGTH_SHORT).show();
		// }
	}

	private static int selectID() {
		Cursor cur = db.rawQuery("select last_insert_rowid()", null);
		int rez = 0;
		if (cur.moveToFirst())
			rez = cur.getInt(0);
		cur.close();
		return rez;
	}
}
