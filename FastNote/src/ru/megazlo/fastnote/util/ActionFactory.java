package ru.megazlo.fastnote.util;

import ru.megazlo.fastnote.fmMain;
import ru.megazlo.fastnote.component.NoteData;
import ru.megazlo.fastnote.R;
import ru.megazlo.quicker.ActionItem;
import ru.megazlo.quicker.QuickAction;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class ActionFactory {
	
	public static QuickAction create(View v, NoteData note) {
		QuickAction qa = new QuickAction(v);
		qa.addAction(send_email(v.getContext(), note));// отправить почтой
		qa.addAction(send_sms(v.getContext(), note));// отправить через смс
		qa.addAction(delete(v.getContext(), note));// удалить
		return qa;
	}

	private static ActionItem delete(Context context, final NoteData note) {
		final ActionItem action = new ActionItem();
		action.setIcon(context.getResources().getDrawable(R.drawable.qa_delete));
		action.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fmMain frm = (fmMain) v.getContext();
				frm.deleteNote(note);
				action.dismiss();
			}
		});
		return action;
	}

	private static ActionItem send_email(Context context, final NoteData note) {
		final ActionItem action = new ActionItem();
		action.setIcon(context.getResources().getDrawable(R.drawable.qa_mail));
		action.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setType("text/plain");
				emailIntent.putExtra(Intent.EXTRA_TEXT, SqlBase.getText(note.getID()));
				v.getContext().startActivity(Intent.createChooser(emailIntent, "Send your note in:"));
				action.dismiss();
			}
		});
		return action;
	}

	private static ActionItem send_sms(Context context, final NoteData note) {
		final ActionItem action = new ActionItem();
		action.setIcon(context.getResources().getDrawable(R.drawable.qa_sms));
		action.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", SqlBase.getText(note.getID()));
				sendIntent.setType("vnd.android-dir/mms-sms");
				v.getContext().startActivity(sendIntent);
				action.dismiss();
			}
		});
		return action;
	}

}
