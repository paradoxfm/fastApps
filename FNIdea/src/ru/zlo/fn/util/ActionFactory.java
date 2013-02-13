package ru.zlo.fn.util;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import ru.megazlo.quicker.ActionItem;
import ru.megazlo.quicker.QuickAction;
import ru.zlo.fn.MAct;
import ru.zlo.fn.R;
import ru.zlo.fn.data.Note;

public class ActionFactory {

	public static QuickAction create(View v, Note note) {
		QuickAction qa = new QuickAction(v);
		qa.addAction(send_email(v.getContext(), note));// отправить почтой
		qa.addAction(send_sms(v.getContext(), note));// отправить через смс
		qa.addAction(delete(v.getContext(), note));// удалить
		return qa;
	}

	private static ActionItem delete(Context context, final Note note) {
		final ActionItem action = new ActionItem();
		action.setIcon(context.getResources().getDrawable(R.drawable.qa_delete));
		action.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MAct frm = (MAct) v.getContext();
				frm.deleteNote(note);
				action.dismiss();
			}
		});
		return action;
	}

	private static ActionItem send_email(Context context, final Note note) {
		final ActionItem action = new ActionItem();
		action.setIcon(context.getResources().getDrawable(R.drawable.qa_mail));
		action.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setType("text/plain");
				emailIntent.putExtra(Intent.EXTRA_TEXT, note.getText());
				v.getContext().startActivity(Intent.createChooser(emailIntent, "Send your note in:"));
				action.dismiss();
			}
		});
		return action;
	}

	private static ActionItem send_sms(Context context, final Note note) {
		final ActionItem action = new ActionItem();
		action.setIcon(context.getResources().getDrawable(R.drawable.qa_sms));
		action.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", note.getText());
				sendIntent.setType("vnd.android-dir/mms-sms");
				v.getContext().startActivity(sendIntent);
				action.dismiss();
			}
		});
		return action;
	}

}
