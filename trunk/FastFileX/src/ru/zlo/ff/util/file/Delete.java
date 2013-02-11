package ru.zlo.ff.util.file;

import java.io.File;

import ru.zlo.ff.R;
import ru.zlo.ff.MAct;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

public class Delete extends AsyncTask<Object, Void, Void> {
	private final ProgressDialog dialog;

	public Delete() {
		dialog = new ProgressDialog(MAct.I);
		dialog.setMessage(MAct.I.getResources().getString(R.string.deleting));
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				try {
					Delete.this.cancel(true);
					Delete.this.finalize();
				} catch (Throwable e) {
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
		MAct.I.update();
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
			for (int i = 0; i < child.length; i++)
				delete(child[i]);
			fil.delete();
		}
	}
}