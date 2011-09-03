package ru.megazlo.fastnote.component;

import java.util.Date;

import ru.megazlo.fastnote.R;
import ru.megazlo.fastnote.util.FileUtil;
import ru.megazlo.fastnote.util.Sets;
import ru.megazlo.fastnote.util.SqlBase;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class NoteEditor extends LinearLayout {
	private boolean mLocked = true;
	private boolean mEdited = false;
	private NoteEdit nedit;
	private TextWatcher wach;
	private NoteData curDat;
	private ScrollView scrv;
	private LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	public NoteEditor(Context context) {
		super(context);
		setOrientation(VERTICAL);
		initChild(context);
		setLayoutParams(lp1);
		setBackgroundColor(Color.WHITE);
	}

	private void initChild(Context context) {
		wach = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!mEdited)
					mEdited = true;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};

		ImageView ribb = new ImageView(context);
		ribb.setBackgroundResource(R.drawable.note_top);
		this.addView(ribb, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		scrv = new ScrollView(context);
		addView(scrv, lp1);

		nedit = new NoteEdit(context);
		nedit.addTextChangedListener(wach);
		applyEditorSet();
		scrv.addView(nedit);
	}

	public void applyEditorSet() {
		nedit.setPaintColor(Sets.LINE_COLOR);
		nedit.setTextColor(Sets.FONT_COLOR);
		nedit.setTextSize(Sets.FONT_SIZE);
	}

	public void setText(NoteData dat) {
		curDat = dat;
		nedit.setText(SqlBase.getText(dat.getID()));
		setText();
	}

	public void setText(String text) {
		curDat = null;
		nedit.setText(text);
		setText();
	}

	private void setText() {
		nedit.setSelection(0);
		scrv.scrollTo(0, nedit.getTop());
		setLocked(true);
		mEdited = false;
	}

	/** Текстовый редактор заблокирован */
	public boolean isLocked() {
		return mLocked;
	}

	public void setLocked(boolean locked) {
		mLocked = locked;
		nedit.setFocusable(!locked);
		if (locked)
			((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					nedit.getWindowToken(), 0);
		else
			nedit.setFocusableInTouchMode(true);
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

	/** Заметка была загружена из базы, если нет то из файла */
	public boolean isFromBase() {
		return curDat != null;
	}

	/** Заметка была отредактирована */
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
}
