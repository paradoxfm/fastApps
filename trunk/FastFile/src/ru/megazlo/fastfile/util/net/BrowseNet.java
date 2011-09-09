package ru.megazlo.fastfile.util.net;

import java.io.IOException;

import jcifs.smb.SmbFile;

import ru.megazlo.fastfile.R;
import ru.megazlo.fastfile.fmMain;
import ru.megazlo.fastfile.engine.BaseEngine;
import ru.megazlo.fastfile.engine.EngineFTP;
import ru.megazlo.fastfile.engine.EngineLAN;
import ru.megazlo.ftplib.ftp.FTPClient;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class BrowseNet extends AsyncTask<Object, Void, Object[]> {

	private final ProgressDialog dialog = new ProgressDialog(fmMain.CONTEXT);

	@Override
	protected Object[] doInBackground(Object... params) {
		if (params[1].getClass() == EngineFTP.class)
			return blowseFTP(params);
		else if (params[1].getClass() == EngineLAN.class)
			return blowseLAN(params);
		return null;
	}

	private Object[] blowseFTP(Object[] prm) {
		EngineFTP eng = (EngineFTP) prm[1];
		FTPClient client = eng.getDat().FTP_CLIENT;
		try {
			if (prm.length > 2)
				client.changeToParentDirectory();
			else
				client.changeWorkingDirectory((String) prm[0]);
			return new Object[] { eng, client.listFiles() };
		} catch (IOException e) {
			return null;
		}
	}

	private Object[] blowseLAN(Object[] prm) {
		EngineLAN eng = (EngineLAN) prm[1];
		try {
			if (prm.length > 2) {
				// eng.getDat().PATH.
				//TODO: переход выше
			}
			eng.getDat().PATH = (SmbFile) prm[0];
			return new Object[] { eng, eng.getDat().PATH.listFiles() };
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(Object[] fil) {
		super.onPostExecute(fil);
		if (fil != null)
			((BaseEngine) fil[0]).fill(fil[1]);
		this.dialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		dialog.setMessage(dialog.getContext().getResources().getString(R.string.browse));
		dialog.setCancelable(false);
		dialog.show();
	}

}
