package ru.megazlo.fastfilehd.components.list;

import java.io.IOException;
import java.util.ArrayList;

import ru.megazlo.fastfilehd.R;
import ru.megazlo.fastfilehd.fmMain;
import ru.megazlo.fastfilehd.components.RowDataFTP;
import ru.megazlo.fastfilehd.components.filerow.FileRowData;
import ru.megazlo.fastfilehd.util.FtpRecort;
import ru.megazlo.fastfilehd.util.Sets;
import ru.megazlo.fastfilehd.util.ftp.FtpBrowse;
import ru.megazlo.fastfilehd.util.ftp.FtpConnect;
import ru.megazlo.ftplib.ftp.FTPFile;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class ListFTP extends ListBase {
	private RowDataFTP dat;

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
			new FtpConnect().execute(hst, Boolean.toString(cb.isChecked()), usr, pwd, ListFTP.this);
		}
	};

	public ListFTP(Context context, RowDataFTP dat, boolean rest) {
		super(context);
		this.mIcoProtocol = Sets.P_FTP;
		isAccessSearsh = false;
		isRestore = rest;
		this.dat = dat;
		this.filEntr = dat.fil;
		this.dirEntr = dat.dir;
		if (isRestore)
			fill(null);
		isRestore = false;
	}

	public void connect() {
		LayoutInflater factory = LayoutInflater.from(fmMain.CONTEXT);
		final View formcon = factory.inflate(R.layout.ftp_conn, null);
		final EditText host = (EditText) formcon.findViewById(R.id.ed_ftp_host);
		final CheckBox anom = (CheckBox) formcon.findViewById(R.id.ed_ftp_anm);
		final EditText user = (EditText) formcon.findViewById(R.id.ed_ftp_usr);
		final View usrinf = formcon.findViewById(R.id.ed_ftp_userinf);
		AlertDialog alr = new AlertDialog.Builder(fmMain.CONTEXT).setTitle(R.string.conng)
				.setIcon(R.drawable.ic_menu_globe).setView(formcon).setNegativeButton(R.string.cansel, null)
				.setPositiveButton(R.string.ok, editdom).create();

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
					usrinf.setVisibility(anom.isChecked() ? GONE : VISIBLE);
				}
			});

			spn.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					FtpRecort re = (FtpRecort) arg0.getSelectedItem();
					host.setText(re.server);
					anom.setChecked(re.anonim);
					user.setText(re.user);
					usrinf.setVisibility(anom.isChecked() ? GONE : VISIBLE);
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
	}

	@Override
	public boolean performItemClick(View view, int position, long id) {
		FTPFile curFile = dirEntr.get(position).getFile();
		if (curFile.isDirectory() || curFile.isSymbolicLink())
			browseCatalog(curFile);
		return super.performItemClick(view, position, id);
	}

	@Override
	public boolean browseUp() {
		dat.CUR_DIR = new FTPFile();
		new FtpBrowse().execute("up", this, "true");
		return true;
	}

	@Override
	protected void browseCatalog(Object filed) {
		dat.CUR_DIR = (FTPFile) filed;
		if (dat.CUR_DIR.isDirectory() || dat.CUR_DIR.isSymbolicLink()) {
			String dirb = dat.CUR_DIR.isDirectory() ? dat.CUR_DIR.getName() : dat.CUR_DIR.getLink();
			new FtpBrowse().execute(dirb, this);
		}
	}

	@Override
	public void fill(Object filar) {
		if (!isRestore) {
			this.dirEntr.clear();
			this.filEntr.clear();
			FTPFile[] files = (FTPFile[]) filar;
			try {
				this.mTitle = '/' + dat.FTP_CLIENT.getRemoteAddress().getHostName() + dat.FTP_CLIENT.printWorkingDirectory();
			} catch (IOException e) {
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory())
					this.dirEntr.add(new FileRowData(files[i], Sets.I_FOLD));
				else
					this.filEntr.add(new FileRowData(files[i], getIconByFile(files[i].getName())));
			}
		}
		super.fill(filar);
	}

	@Override
	public void update() {
		new FtpBrowse().execute("", this);
	}

	@Override
	public Object[] getFiles() {
		ArrayList<FTPFile> rez = new ArrayList<FTPFile>();
		for (int i = 0; i < dirEntr.size(); i++)
			if (dirEntr.get(i).isChecked()) {
				FTPFile fl = dirEntr.get(i).getFile();
				rez.add(fl);
			}
		FTPFile[] fls = new FTPFile[rez.size()];
		for (int i = 0; i < rez.size(); i++)
			fls[i] = rez.get(i);
		return fls;
	}

	@Override
	public void browseRoot() {
		new FtpBrowse().execute("/");
	}

	@Override
	public void search(String search) {
		Toast.makeText(this.getContext(), "Поиск запрещен", Toast.LENGTH_SHORT).show();
		// TODO: текст сука
	}

	public RowDataFTP getDat() {
		return dat;
	}

	@Override
	public Object getCurrentDir() {
		return dat.CUR_DIR;
	}

}
