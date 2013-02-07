package ru.zlo.ff.util.file;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import ru.zlo.ff.R;
import ru.zlo.ff.engine.EngPool;

import java.io.File;

public class Delete extends AsyncTask<Object, Void, Void> {
	private final ProgressDialog dialog;

	public Delete(Context context) {
		dialog = new ProgressDialog(context);
		dialog.setMessage(context.getString(R.string.deleting));
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				try {
					Delete.this.cancel(true);
					Delete.this.finalize();
				} catch (Throwable ignored) {
				}
			}
		});
	}

	@Override
	protected Void doInBackground(Object... arg) {
		for (Object obj : FileTools.FROM) {
			if (obj.getClass() == File.class)
				delete((File) obj);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void rez) {
		EngPool.Inst().getCurrent().update();
		this.dialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		this.dialog.show();
	}

	public static void delete(File fil) {
		if (fil.isFile()) {
			fil.delete();
		} else {
			File[] child = fil.listFiles();
			for (File aChild : child)
				delete(aChild);
			fil.delete();
		}
	}
}
