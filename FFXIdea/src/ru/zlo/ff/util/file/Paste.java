package ru.zlo.ff.util.file;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import ru.zlo.ff.MAct;
import ru.zlo.ff.util.ActionFactory;

import java.io.*;

public class Paste extends AsyncTask<Object, Void, Void> {
	private final ProgressDialog dialog;

	public Paste() {
		dialog = new ProgressDialog(MAct.I);
		dialog.setMessage("Вставка");
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				try {
					Paste.this.cancel(true);
					Paste.this.finalize();
				} catch (Throwable ignored) {
				}
			}
		});
	}

	@Override
	protected Void doInBackground(Object... params) {
		if (FileTools.FROM[0].getClass() == File.class && FileTools.TO.getClass() == File.class)
			for (Object obj : FileTools.FROM)
				copy((File) obj, (File) FileTools.TO);

		FileTools.FROM = null;
		// TODO довести до ума хуйню с копированием

		// TODO замутить между фтп

		return null;
	}

	private void copy(File fileFrom, File fileTo) {
		if (!dialog.isShowing())
			return;
		File newf = new File(fileTo, fileFrom.getName());
		if (fileFrom.isDirectory()) {
			newf.mkdir();
			String[] children = fileFrom.list();
			for (String chld : children)
				copy(new File(fileFrom, chld), newf);
		} else {
			InputStream in;
			try {
				in = new FileInputStream(fileFrom);
				OutputStream out = new FileOutputStream(newf);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0)
					out.write(buf, 0, len);
				in.close();
				out.close();
			} catch (Exception ignored) {
			}
		}
		if (FileTools.OPERATION == ActionFactory.MOVE)
			fileFrom.delete();
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

}
