package ru.zlo.fn.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Note {

	boolean checked = false;
	@DatabaseField(columnName = "id", id = true)
	private int id;
	@DatabaseField(columnName = "word_count", canBeNull = false)
	private int wordCount = 0;
	@DatabaseField(columnName = "note_date", canBeNull = false)
	private Date date = new Date();
	@DatabaseField(columnName = "note_title", canBeNull = false)
	private String title = "";
	@DatabaseField(columnName = "note_text", canBeNull = false)
	private String text = "";

	public Note() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
