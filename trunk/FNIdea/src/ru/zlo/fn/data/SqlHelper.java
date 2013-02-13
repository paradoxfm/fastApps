package ru.zlo.fn.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@EBean(scope = Scope.Singleton)
public class SqlHelper extends OrmLiteSqliteOpenHelper {

	@RootContext
	Context context;

	private static final String DATABASE_NAME = "fastnote.sqlite";
	private static final int DATABASE_VERSION = 1;
	private Dao<Note, Integer> noteDao = null;

	protected SqlHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Note.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		/*List<String> allSql = new ArrayList<String>();
		switch (oldVersion) {
			case 1:
				allSql.add("alter table Note add column `new_col` VARCHAR");
				//allSql.add("alter table AdData add column `new_col2` VARCHAR");
		}
		for (String sql : allSql)
			sqLiteDatabase.execSQL(sql);*/
	}

	public Dao<Note, Integer> getNoteDao() {
		if (noteDao == null) {
			try {
				noteDao = getDao(Note.class);
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return noteDao;
	}

	public List<Note> getAllNoteLists() {
		List<Note> noteLists;
		try {
			PreparedQuery<Note> query = getNoteDao().queryBuilder().selectColumns("id", "word_count", "note_date", "note_title").prepare();
			noteLists = getNoteDao().query(query);
		} catch (SQLException e) {
			e.printStackTrace();
			noteLists = new ArrayList<Note>(0);
		}
		return noteLists;
	}

	public String getNoteText(int id) {
		try {
			Note note = getNoteDao().queryForId(id);
			return note.getText();
		} catch (SQLException e) {
			return "Error";
		}
	}

	public boolean createNote(Note note) {
		try {
			getNoteDao().create(note);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public boolean updateNote(Note note) {
		try {
			getNoteDao().update(note);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
}
