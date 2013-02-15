package ru.zlo.fn.fragments;

import android.app.Fragment;
import android.widget.ScrollView;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import ru.zlo.fn.R;
import ru.zlo.fn.component.NoteEdit;
import ru.zlo.fn.data.Note;
import ru.zlo.fn.data.SqlHelper;
import ru.zlo.fn.util.Options_;

@EFragment(R.layout.note_details)
public class NoteDetailFragment extends Fragment implements SqlHelper.OnDeleteItem {

	private boolean mEdited = false;
	@ViewById(R.id.scroll_detail)
	ScrollView scroll;
	@ViewById(R.id.text_detail)
	NoteEdit editor;
	@Bean
	SqlHelper helper;
	@Pref
	Options_ opt;
	Note currentNote;
	OnSaveChanges saveChanges;

	public interface OnSaveChanges {
		void saveChanges();
	}

	@Override
	public void onResume() {
		super.onResume();
		editor.setPaintColor(opt.lineColor().get());
		editor.setTextColor(opt.fontColor().get());
		editor.setTextSize(opt.fontSize().get());
	}

	@AfterViews
	void afterInit() {
		helper.addDeleteItemListener(this);
	}

	public void setOnSaveChanges(OnSaveChanges listener) {
		saveChanges = listener;
	}

	@Override
	public void deleteItem(Note item) {
		currentNote = null;
		editor.setText("");
	}

	public void setCurrentNote(Note note) {
		currentNote = note;
		String text = helper.getNoteText(note.getId());
		currentNote.setText(text);
		editor.setText(text);
		scroll.scrollTo(0, editor.getTop());
	}

	@TextChange(R.id.text_detail)
	void textCahnge(CharSequence text) {
		if (!mEdited && text.length() > 0)
			mEdited = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		save();
	}

	public void save() {
		if (!mEdited || currentNote == null)
			return;
		mEdited = false;
		currentNote.setText(editor.getText().toString());
		helper.updateNote(currentNote);
		if (saveChanges != null)
			saveChanges.saveChanges();
	}
}
