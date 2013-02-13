package ru.zlo.fn.component;

/*
public class NoteEditor extends LinearLayout {
	private boolean mEdited = false;
	private NoteEdit nedit;
	private NoteData curDat;

	public NoteEditor(Context context) {
		super(context);
		setOrientation(VERTICAL);
		initChild(context);
		setBackgroundColor(Color.WHITE);
	}

	private void initChild(Context context) {
		nedit = new NoteEdit(context);
	}

	public void setText(NoteData dat) {
		curDat = dat;
		//nedit.setText(SqlBase.getText(dat.getID()));
		setText();
	}

	public void setText(String text) {
		curDat = null;
		nedit.setText(text);
		setText();
	}

	private void setText() {
		nedit.setSelection(0);
		//scrv.scrollTo(0, nedit.getTop());
		setLocked(true);
		mEdited = false;
	}

	*/
/** Текстовый редактор заблокирован *//*


	public boolean isLocked() {
		return nedit.isEdit();
	}

	public void setLocked(boolean locked) {
		if (locked)
			nedit.disableEdit();
	}

	public String getText() {
		return nedit.getText().toString();
	}

	public String getTitle() {
		if (this.isFromBase()) {
			if (nedit.getText().toString().length() == 0)
				return curDat.getTitle();
			String text = nedit.getText().toString();
			int ind = text.indexOf("\n");
			return ind == -1 ? text : text.substring(0, ind);
		} else
			return FileUtil.getFileName();
	}

	*/
/** Заметка была загружена из базы, если нет то из файла *//*

	public boolean isFromBase() {
		return curDat != null;
	}


	*/
/** Заметка была отредактирована *//*


	public boolean isEdited() {
		return mEdited;
	}

	public int getWordCount() {
		return nedit.getText().toString().split(" ").length;
	}

	public void saveText() {
		if (this.isFromBase()) {
			curDat.setDate(new Date());
			curDat.setTitle(this.getTitle());
			curDat.setWordCount(this.getWordCount());
			SqlBase.updateData(this.getText(), curDat);
		}
		this.setLocked(true);
		mEdited = false;
	}

	public void saveTextFile() {
		if (!this.isFromBase())
			FileUtil.saveToFile(nedit.getText().toString());
		this.setLocked(true);
		mEdited = false;
	}

	public void search(String query) {
		String str = nedit.getEditableText().toString().toLowerCase();
		if (str.contains(query)) {
			int ind = str.indexOf(query);
			nedit.requestFocus();
			nedit.setSelection(ind, ind + query.length());
		}
	}
}

*/
