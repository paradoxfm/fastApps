package ru.megazlo.ffng.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.megazlo.ffng.fmMain;
import ru.megazlo.ffng.util.ActionFactory;
import ru.megazlo.ftplib.ftp.FTPClient;
import ru.megazlo.ftplib.ftp.FTPFile;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

public class Paste extends AsyncTask<Object, Void, Void> {
	private final ProgressDialog dialog;

	public Paste() {
		dialog = new ProgressDialog(fmMain.CONTEXT);
		dialog.setMessage("Вставка");
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				try {
					Paste.this.cancel(true);
					Paste.this.finalize();
				} catch (Throwable e) {
				}
			}
		});
	}

	@Override
	protected Void doInBackground(Object... params) {
		if (FileTools.FROM[0].getClass() == FTPFile.class && FileTools.TO.getClass() == File.class)
			for (Object obj : FileTools.FROM)
				copy((FTPFile) obj, (File) FileTools.TO, FileTools.CLIENT_FROM);

		if (FileTools.FROM[0].getClass() == File.class && FileTools.TO.getClass() == FTPFile.class)
			for (Object obj : FileTools.FROM)
				copy((File) obj, (FTPFile) FileTools.TO, FileTools.CLIENT_TO);

		if (FileTools.FROM[0].getClass() == File.class && FileTools.TO.getClass() == File.class)
			for (Object obj : FileTools.FROM)
				copy((File) obj, (File) FileTools.TO);

		FileTools.FROM = null;
		// TODO довести до ума хуйню с копированием

		// TODO замутить между фтп

		return null;
	}

	private void copy(FTPFile from, File to, FTPClient ftp) {
		if (from.isDirectory()) {
			File dir = new File(to, from.getName());
			dir.mkdir();
		} else if (from.isFile()) {
			try {
				OutputStream output = new FileOutputStream(new File(to, from.getName()));
				String nam = from.getName();
				ftp.retrieveFile(nam, output);
				output.close();
			} catch (IOException e) {
			}
		}
	}

	private void copy(File from, FTPFile to, FTPClient ftp) {
		String nm = to != fmMain.CONTEXT.getCurEng().getCurrentDir() ? to.getName() + '/' + from.getName() : from.getName();
		if (from.isDirectory()) {
			try {
				ftp.makeDirectory(nm);
			} catch (IOException e) {
			}
		} else if (from.isFile()) {
			try {
				InputStream input = new FileInputStream(from);
				ftp.storeFile(nm, input);
				input.close();
			} catch (IOException e) {
			}
		}
		if (FileTools.OPERATION == ActionFactory.MOVE)
			from.delete();
	}

	private void copy(File fileFrom, File fileTo) {
		if (!dialog.isShowing())
			return;
		File newf = new File(fileTo, fileFrom.getName());
		if (fileFrom.isDirectory()) {
			newf.mkdir();
			String[] children = fileFrom.list();
			for (int i = 0; i < children.length; i++)
				copy(new File(fileFrom, children[i]), newf);
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
			} catch (Exception e) {
			}
		}
		if (FileTools.OPERATION == ActionFactory.MOVE)
			fileFrom.delete();
	}

	@Override
	protected void onPostExecute(Void rez) {
		fmMain.CONTEXT.update();
		this.dialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		this.dialog.show();
	}

}
