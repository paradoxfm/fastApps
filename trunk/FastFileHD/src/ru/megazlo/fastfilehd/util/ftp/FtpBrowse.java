package ru.megazlo.fastfilehd.util.ftp;

import java.io.IOException;

import ru.megazlo.fastfilehd.R;
import ru.megazlo.fastfilehd.fmMain;
import ru.megazlo.fastfilehd.components.list.ListFTP;
import ru.megazlo.ftplib.ftp.FTPClient;
import ru.megazlo.ftplib.ftp.FTPFile;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class FtpBrowse extends AsyncTask<Object, Void, Object[]> {

	private final ProgressDialog dialog = new ProgressDialog(fmMain.CONTEXT);

	@Override
	protected Object[] doInBackground(Object... params) {
		ListFTP ftp = (ListFTP) params[1];
		FTPClient client = ftp.getDat().FTP_CLIENT;
		try {
			if (params.length > 2)
				client.changeToParentDirectory();
			else
				client.changeWorkingDirectory((String) params[0]);
			return new Object[] { ftp, client.listFiles() };
		} catch (IOException e) {
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object[] fil) {
		super.onPostExecute(fil);
		if (fil != null) {
			ListFTP ftp = (ListFTP) fil[0];
			FTPFile[] files = (FTPFile[]) fil[1];
			ftp.fill(files);
		}
		this.dialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		dialog.setMessage(dialog.getContext().getResources().getString(R.string.browse));
		dialog.setCancelable(false);
		dialog.show();
	}

}
