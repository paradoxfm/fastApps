package ru.megazlo.fastfile.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import ru.megazlo.fastfile.R;
import ru.megazlo.fastfile.components.RowData;
import ru.megazlo.fastfile.components.RowDataFTP;
import ru.megazlo.fastfile.components.filerow.FileList;
import ru.megazlo.fastfile.components.filerow.FileRowData;
import ru.megazlo.fastfile.util.FtpRecort;
import ru.megazlo.fastfile.util.Sets;
import ru.megazlo.fastfile.util.ftp.FtpBrowse;
import ru.megazlo.fastfile.util.ftp.FtpConnect;
import ru.megazlo.ftplib.ftp.FTPFile;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class EngineFTP extends BaseEngine {

	private DialogInterface.OnClickListener editdom = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dlg, int which) {
			AlertDialog alr = (AlertDialog) dlg;
			EditText ed = (EditText) alr.findViewById(R.id.ed_ftp_host);
			String hst = ed.getEditableText().toString();
			CheckBox cb = (CheckBox) alr.findViewById(R.id.ed_ftp_anm);
			ed = (EditText) alr.findViewById(R.id.ed_ftp_usr);
			String usr = ed.getEditableText().toString();
			ed = (EditText) alr.findViewById(R.id.ed_ftp_pwd);
			String pwd = ed.getEditableText().toString();
			Sets.insertFtpRec(hst, usr, cb.isChecked());
			new FtpConnect().execute(hst, Boolean.toString(cb.isChecked()), usr, pwd, getList());
		}
	};

	public EngineFTP(RowData data, FileList list, boolean rest) {
		super(list);
		isRestore = rest;
		dat = data;
	}

	@Override
	public boolean browseUp() {
		getDat().CUR_DIR = new FTPFile();
		new FtpBrowse().execute("up", this, "true");
		return true;
	}

	@Override
	public void browseCatalog(Object cat) {
		RowDataFTP dt = getDat();
		dt.CUR_DIR = (FTPFile) cat;
		if (dt.CUR_DIR.isDirectory() || dt.CUR_DIR.isSymbolicLink()) {
			String dirb = dt.CUR_DIR.isDirectory() ? dt.CUR_DIR.getName() : dt.CUR_DIR.getLink();
			new FtpBrowse().execute(dirb, this);
		}
	}

	@Override
	public Object[] getFiles() {
		RowDataFTP dt = getDat();
		ArrayList<FTPFile> rez = new ArrayList<FTPFile>();
		for (int i = 0; i < dt.dir.size(); i++)
			if (dt.dir.get(i).isChecked()) {
				FTPFile fl = dt.dir.get(i).getFile();
				rez.add(fl);
			}
		FTPFile[] fls = new FTPFile[rez.size()];
		for (int i = 0; i < rez.size(); i++)
			fls[i] = rez.get(i);
		return fls;
	}

	@Override
	public void update() {
		new FtpBrowse().execute("", this);
	}

	@Override
	public void browseRoot() {
		new FtpBrowse().execute("/");
	}

	@Override
	public void search(String search) {
		Toast.makeText(getList().getContext(), "Поиск запрещен", Toast.LENGTH_SHORT).show();
	}

	@Override
	public RowDataFTP getDat() {
		return (RowDataFTP) dat;
	}

	@Override
	public Object getCurrentDir() {
		return getDat().CUR_DIR;
	}

	@Override
	public void fill(Object filar) {
		if (!isRestore) {
			dat.dir.clear();
			dat.fil.clear();
			FTPFile[] files = (FTPFile[]) filar;
			try {
				mTitle = '/' + getDat().FTP_CLIENT.getRemoteAddress().getHostName()
						+ getDat().FTP_CLIENT.printWorkingDirectory();
			} catch (IOException e) {
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory())
					dat.dir.add(new FileRowData(files[i], Sets.I_FOLD));
				else
					dat.fil.add(new FileRowData(files[i], getIconByFile(files[i].getName())));
			}
			Collections.sort(dat.dir);
			Collections.sort(dat.fil);
			dat.dir.addAll(dat.fil);
		}
		if (finisher != null)
			finisher.onFinish();
	}

	@Override
	public Object exec(int cmd) {
		switch (cmd) {
		case BaseEngine.CMD_CON:
			return execConnect(getList().getContext());
		default:
			return null;
		}
	}

	private Object execConnect(Context c) {
		LayoutInflater factory = LayoutInflater.from(c);
		final View formcon = factory.inflate(R.layout.ftp_conn, null);
		final EditText host = (EditText) formcon.findViewById(R.id.ed_ftp_host);
		final CheckBox anom = (CheckBox) formcon.findViewById(R.id.ed_ftp_anm);
		final EditText user = (EditText) formcon.findViewById(R.id.ed_ftp_usr);
		final View usrinf = formcon.findViewById(R.id.ed_ftp_userinf);
		AlertDialog alr = new AlertDialog.Builder(c).setTitle(R.string.conng).setIcon(R.drawable.ic_menu_globe)
				.setView(formcon).setNegativeButton(R.string.cansel, cansl).setPositiveButton(R.string.ok, editdom)
				.setOnCancelListener(back).create();

		if (Sets.FTPS.size() > 0) {
			final Spinner spn = (Spinner) formcon.findViewById(R.id.ed_ftp_spin);
			FtpRecort[] recs = new FtpRecort[Sets.FTPS.size()];
			for (int i = 0; i < Sets.FTPS.size(); i++)
				recs[i] = Sets.FTPS.get(i);
			ArrayAdapter<FtpRecort> adp = new ArrayAdapter<FtpRecort>(alr.getContext(), android.R.layout.simple_spinner_item,
					recs);
			adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn.setAdapter(adp);

			anom.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					usrinf.setVisibility(anom.isChecked() ? View.GONE : View.VISIBLE);
				}
			});

			spn.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					FtpRecort re = (FtpRecort) arg0.getSelectedItem();
					host.setText(re.server);
					anom.setChecked(re.anonim);
					user.setText(re.user);
					usrinf.setVisibility(anom.isChecked() ? View.GONE : View.VISIBLE);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});

			ImageView clos = (ImageView) formcon.findViewById(R.id.ed_ftp_del);
			clos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					FtpRecort rc = (FtpRecort) spn.getSelectedItem();
					spn.removeViewInLayout(spn.getSelectedView());
					Sets.deleteFtpRec(rc);
					FtpRecort[] recs = new FtpRecort[Sets.FTPS.size()];
					for (int i = 0; i < Sets.FTPS.size(); i++)
						recs[i] = Sets.FTPS.get(i);
					ArrayAdapter<FtpRecort> adp = new ArrayAdapter<FtpRecort>(v.getContext(),
							android.R.layout.simple_spinner_item, recs);
					adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spn.setAdapter(adp);
				}
			});
		}
		alr.show();
		return null;
	}

}
