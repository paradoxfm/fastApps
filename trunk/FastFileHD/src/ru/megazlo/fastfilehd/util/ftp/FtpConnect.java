package ru.megazlo.fastfilehd.util.ftp;

import ru.megazlo.fastfilehd.R;
import ru.megazlo.fastfilehd.fmMain;
import ru.megazlo.fastfilehd.components.list.ListFTP;
import ru.megazlo.ftplib.ftp.FTPClient;
import ru.megazlo.ftplib.ftp.FTPReply;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class FtpConnect extends AsyncTask<Object, Void, ListFTP> {

	private static String USER = "anonymous";

	private final ProgressDialog dlg = new ProgressDialog(fmMain.CONTEXT);

	@Override
	protected ListFTP doInBackground(Object... params) {
		String host = (String) params[0];
		Boolean anom = Boolean.parseBoolean((String) params[1]);
		String user = anom ? USER : (String) params[2];
		String pass = anom ? "" : (String) params[3];
		ListFTP lst = (ListFTP) params[4];
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(host);// "ftp.ntua.gr"
			if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
				ftp.disconnect();
			} else {
				ftp.login(user, pass);
				ftp.changeWorkingDirectory("");
			}
			lst.getDat().FTP_CLIENT = ftp;
		} catch (Exception e) {
			Toast.makeText(fmMain.CONTEXT, R.string.err_conn, Toast.LENGTH_SHORT).show();
		}
		return lst;
	}

	@Override
	protected void onPostExecute(ListFTP ftp) {
		super.onPostExecute(ftp);
		new FtpBrowse().execute("", ftp);
		this.dlg.dismiss();
	}

	@Override
	protected void onPreExecute() {
		dlg.setMessage(dlg.getContext().getResources().getString(R.string.conng));
		dlg.setCancelable(false);
		dlg.show();
	}

}
