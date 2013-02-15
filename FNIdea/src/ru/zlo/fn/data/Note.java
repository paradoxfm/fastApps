package ru.zlo.fn.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Note {

	boolean checked = false;
	@DatabaseField(columnName = "id", generatedId = true)
	private int id;
	@DatabaseField(columnName = "word_count", canBeNull = false)
	private int wordCount = 0;
	@DatabaseField(columnName = "note_date", canBeNull = false)
	private Date date = new Date();
	@DatabaseField(columnName = "note_title", canBeNull = false)
	private String title = "New note";
	@DatabaseField(columnName = "note_text", canBeNull = false)
	private String text = "";

	public Note() {
	}

	public int getId() {
		return id;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getWordCount() {
		return wordCount;
	}

	public Date getDate() {
		return date;
	}

	@Deprecated
	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public void setText(String textString) {
		text = textString;
		title = text.split("\n")[0];
		date = new Date();
		wordCount = getWorgCount(text);
	}

	protected int getWorgCount(String text) {
		return text.split("\\s+").length;
	}
}
