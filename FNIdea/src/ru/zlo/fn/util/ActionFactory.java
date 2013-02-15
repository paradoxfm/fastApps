package ru.zlo.fn.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import ru.megazlo.quicker.ActionItem;
import ru.megazlo.quicker.QuickAction;
import ru.zlo.fn.R;
import ru.zlo.fn.data.Note;
import ru.zlo.fn.data.SqlHelper;

@EBean(scope = Scope.Singleton)
public class ActionFactory {

	@Bean
	SqlHelper helper;

	public QuickAction create(View v, Note note) {
		QuickAction qa = new QuickAction(v);
		qa.addAction(send_email(v.getContext(), note));// отправить почтой
		qa.addAction(send_sms(v.getContext(), note));// отправить через смс
		qa.addAction(delete(v.getContext(), note));// удалить
		return qa;
	}

	private ActionItem delete(final Context context, final Note note) {
		final ActionItem action = new ActionItem();
		action.setIcon(context.getResources().getDrawable(R.drawable.qa_delete));
		action.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(context).setNegativeButton(R.string.cansel, null).setMessage(R.string.del_q)
						.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								helper.deleteNote(note);
								Toast.makeText(((AlertDialog) dialog).getContext(), R.string.del_ok, Toast.LENGTH_SHORT).show();
							}
						}).setIcon(R.drawable.qa_delete).setTitle(R.string.del_ing).show();
				action.dismiss();
			}
		});
		return action;
	}

	private ActionItem send_email(Context context, final Note note) {
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

	private ActionItem send_sms(Context context, final Note note) {
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
