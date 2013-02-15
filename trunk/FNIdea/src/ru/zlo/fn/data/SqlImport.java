package ru.zlo.fn.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlImport extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "fastnotes.bak";
	private static final int DATABASE_VERSION = 1;
	private Dao<OldNote, Integer> noteDao = null;

	public SqlImport(Context context) {
		super(context, getDbPath(context), null, DATABASE_VERSION);
	}

	public static String getDbPath(Context context) {
		return Environment.getExternalStorageDirectory().getPath() + "/backup/" + DATABASE_NAME;
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, OldNote.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
	}

	protected Dao<OldNote, Integer> getNoteDao() {
		if (noteDao == null) {
			try {
				noteDao = getDao(OldNote.class);
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return noteDao;
	}

	public List<OldNote> getAllNoteLists() {
		try {
			return getNoteDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<OldNote>(0);
		}
	}
}
