package ru.megazlo.fastfile.util.ftp;

import ru.megazlo.fastfile.R;
import ru.megazlo.fastfile.fmMain;
import ru.megazlo.fastfile.engine.EngineFTP;
import ru.megazlo.ftplib.ftp.FTPClient;
import ru.megazlo.ftplib.ftp.FTPReply;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class FtpConnect extends AsyncTask<Object, Void, EngineFTP> {

	private static final String USER = "anonymous";

	private final ProgressDialog dlg = new ProgressDialog(fmMain.CONTEXT);

	@Override
	protected EngineFTP doInBackground(Object... params) {
		String host = (String) params[0];
		Boolean anom = Boolean.parseBoolean((String) params[1]);
		String user = anom ? USER : (String) params[2];
		String pass = anom ? "" : (String) params[3];
		EngineFTP eng = (EngineFTP) params[4];
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(host);// "ftp.ntua.gr"
			if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
				ftp.disconnect();
			} else {
				ftp.login(user, pass);
				ftp.changeWorkingDirectory("");
			}
			eng.getDat().FTP_CLIENT = ftp;
		} catch (Exception e) {
			Toast.makeText(fmMain.CONTEXT, R.string.err_conn, Toast.LENGTH_SHORT).show();
		}
		return eng;
	}

	@Override
	protected void onPostExecute(EngineFTP eng) {
		super.onPostExecute(eng);
		new FtpBrowse().execute("", eng);
		this.dlg.dismiss();
	}

	@Override
	protected void onPreExecute() {
		dlg.setMessage(dlg.getContext().getResources().getString(R.string.conng));
		dlg.setCancelable(false);
		dlg.show();
	}

}
