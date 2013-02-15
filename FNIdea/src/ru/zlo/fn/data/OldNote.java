package ru.zlo.fn.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_notes")
public class OldNote {

	@DatabaseField(columnName = "ID")
	private Integer id;
	@DatabaseField(columnName = "Ordr")
	private Integer order;
	@DatabaseField(columnName = "WCount")
	private Integer wordCount;
	@DatabaseField(columnName = "Date")
	private String date;
	@DatabaseField(columnName = "Title")
	private String title;
	@DatabaseField(columnName = "Txt")
	private String text;

	public String getDate() {
		return date;
	}

	public String getText() {
		return text;
	}
}
