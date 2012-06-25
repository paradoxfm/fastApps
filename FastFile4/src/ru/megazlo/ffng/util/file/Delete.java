package ru.megazlo.ffng.util.file;

import java.io.File;
import java.io.IOException;

import ru.megazlo.ffng.R;
import ru.megazlo.ffng.fmMain;
import ru.megazlo.ftplib.ftp.FTPFile;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

public class Delete extends AsyncTask<Object, Void, Void> {
	private final ProgressDialog dialog;

	public Delete() {
		dialog = new ProgressDialog(fmMain.I);
		dialog.setMessage(fmMain.I.getResources().getString(R.string.deleting));
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
			if (obj.getClass() == FTPFile.class)
				delete((FTPFile) obj);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void rez) {
		fmMain.I.update();
		this.dialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		this.dialog.show();
	}

	public static void delete(FTPFile fil) {
		try {
			if (fil.isDirectory())
				FileTools.CLIENT_FROM.removeDirectory(fil.getName());
			else if (fil.isFile())
				FileTools.CLIENT_FROM.deleteFile(fil.getName());
			else
				return;
		} catch (IOException e) {
		}
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
