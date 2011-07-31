package ru.megazlo.fastfile.util.file;

import java.io.File;
import java.util.ArrayList;

import ru.megazlo.fastfile.R;
import ru.megazlo.fastfile.fmMain;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

public class Search extends AsyncTask<File, Void, File[]> {

	private final ProgressDialog dialog;
	private String srh;

	public Search(String srhe) {
		srh = srhe.toLowerCase();
		dialog = new ProgressDialog(fmMain.CONTEXT);
		dialog.setMessage(fmMain.CONTEXT.getString(R.string.serach) + ": " + srhe);
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				try {
					Search.this.cancel(true);
					Search.this.finalize();
				} catch (Throwable e) {
				}
			}
		});
	}

	@Override
	protected File[] doInBackground(File... files) {
		// ArrayList<File> dirs = new ArrayList<File>();
		ArrayList<File> fils = new ArrayList<File>();
		for (int i = 0; i < files.length; i++)
			search(files[i], srh, /* files[i].isDirectory() ? dirs : */fils);
		// for (int i = 0; i < fils.size(); i++)
		// dirs.add(fils.get(i));
		File[] fls = new File[fils.size()];
		fils.toArray(fls);
		FileTools.FROM = null;
		return fls;
	}

	private void search(File fil, String txt, ArrayList<File> rez) {
		if (!dialog.isShowing())
			return;
		if (fil.getName().toLowerCase().contains(txt))
			rez.add(fil);
		if (fil.isDirectory() && fil.canRead()) {
			File[] lst = fil.listFiles();
			for (int i = 0; i < lst.length; i++)
				search(lst[i], txt, rez);
		}
	}

	@Override
	protected void onPostExecute(File[] rez) {
		super.onPostExecute(rez);
		fmMain.CONTEXT.getCurrentList().fill(rez);
		this.dialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		this.dialog.show();
	}

}
