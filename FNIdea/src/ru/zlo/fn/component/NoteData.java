package ru.zlo.fn.component;

import ru.zlo.fn.util.Options;

import java.util.Date;

public class NoteData {
	private boolean mChecked;
	private int mID;
	private int mOrder;
	private int mWordCount;
	private Date mDate;
	private String mTitle;

	public NoteData(int id, int order, int wordCount, Date date, String title) {
		this.mID = id;
		this.mOrder = order;
		mWordCount = wordCount;
		this.mDate = date;
		this.mTitle = title;
	}

	public NoteData() {
		mDate = new Date();
		mTitle = "New note";
		mWordCount = 0;
		mChecked = true;
		mOrder = 0;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setDate(Date date) {
		this.mDate = date;
	}

	public String getDate() {
		return Options.F_DATE.format(mDate) + " " + Options.F_TIME.format(mDate) + " ";
	}

	public void setOrder(int order) {
		this.mOrder = order;
	}

	public int getOrder() {
		return mOrder;
	}

	public void setID(int id) {
		this.mID = id;
	}

	public int getID() {
		return mID;
	}

	public boolean isChecked() {
		return mChecked;
	}

	public void setChecked(boolean check) {
		mChecked = check;
	}

	public Date getDateNorm() {
		return mDate;
	}

	public void setWordCount(int wordCount) {
		mWordCount = wordCount;
	}

	public int getWordCount() {
		return mWordCount;
	}
}
