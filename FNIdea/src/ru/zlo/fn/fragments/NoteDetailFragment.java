package ru.zlo.fn.fragments;

import android.app.Fragment;
import android.widget.ScrollView;
import com.googlecode.androidannotations.annotations.*;
import ru.zlo.fn.R;
import ru.zlo.fn.component.NoteEdit;
import ru.zlo.fn.data.Note;
import ru.zlo.fn.data.SqlHelper;
import ru.zlo.fn.util.Options;

@EFragment(R.layout.note_details)
public class NoteDetailFragment extends Fragment {

	private boolean mEdited = false;
	@ViewById(R.id.scroll_detail)
	ScrollView scroll;
	@ViewById(R.id.text_detail)
	NoteEdit editor;
	@Bean
	SqlHelper helper;
	Note currentNote;
	OnSaveChanges saveChanges;

	public interface OnSaveChanges {
		void saveChanges();
	}

	@AfterViews
	void afterInit() {
		editor.setPaintColor(Options.LINE_COLOR);
		editor.setTextColor(Options.FONT_COLOR);
		editor.setTextSize(Options.FONT_SIZE);
	}

	public void setCurrentNote(Note note) {
		currentNote = note;
		String text = helper.getNoteText(note.getId());
		currentNote.setText(text);
		editor.setText(text);
		scroll.scrollTo(0, editor.getTop());
	}

	@TextChange(R.id.text_detail)
	void textCahnge() {
		if (!mEdited)
			mEdited = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		save();
	}

	public void save() {
		if (!mEdited)
			return;
		mEdited = false;
		helper.updateNote(currentNote);
		if (saveChanges != null)
			saveChanges.saveChanges();
	}
}
