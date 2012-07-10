package ru.zlo.ff.util.net;

import java.net.MalformedURLException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import ru.megazlo.ftplib.ftp.FTPClient;
import ru.megazlo.ftplib.ftp.FTPReply;
import ru.zlo.ff.MAct;
import ru.zlo.ff.R;
import ru.zlo.ff.components.RowDataLAN;
import ru.zlo.ff.engine.BaseEngine;
import ru.zlo.ff.engine.EngineFTP;
import ru.zlo.ff.engine.EngineLAN;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class ConnectNet extends AsyncTask<Object, Void, BaseEngine> {

	private static final String USER = "anonymous";

	private final ProgressDialog dlg = new ProgressDialog(MAct.I);

	@Override
	protected BaseEngine doInBackground(Object... params) {
		String host = (String) params[0];
		Boolean anom = Boolean.parseBoolean((String) params[1]);
		String user = anom ? USER : (String) params[2];
		String pass = anom ? "" : (String) params[3];

		if (params[4].getClass() == EngineFTP.class)
			return connectFTP(host, anom, user, pass, (EngineFTP) params[4]);
		if (params[4].getClass() == EngineLAN.class)
			return connectLAN(host, anom, user, pass, (EngineLAN) params[4]);
		return null;
	}

	private BaseEngine connectFTP(String host, Boolean anom, String user, String pass, EngineFTP eng) {
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
			Toast.makeText(MAct.I, R.string.err_conn, Toast.LENGTH_SHORT).show();
		}
		return eng;
	}

	private BaseEngine connectLAN(String host, Boolean anom, String user, String pass, EngineLAN eng) {
		String[] spl = host.split(":");
		String dmn = spl.length > 1 ? spl[0] : null;
		NtlmPasswordAuthentication auth = anom ? NtlmPasswordAuthentication.ANONYMOUS : new NtlmPasswordAuthentication(dmn,
				user, pass);
		try {
			SmbFile dir = spl.length == 1 ? new SmbFile("smb://" + host + "/", auth) : new SmbFile("smb://" + spl[1] + "/",
					auth);
			eng.getDat().PATH = dir;
			return eng;
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(BaseEngine eng) {
		super.onPostExecute(eng);
		if (eng.getClass() == EngineFTP.class)
			new BrowseNet().execute("", eng);
		if (eng.getClass() == EngineLAN.class)
			new BrowseNet().execute(((RowDataLAN) eng.getDat()).PATH, eng);
		this.dlg.dismiss();
	}

	@Override
	protected void onPreExecute() {
		dlg.setMessage(dlg.getContext().getResources().getString(R.string.conng));
		dlg.setCancelable(false);
		dlg.show();
	}

}
