package ru.megazlo.fastfile.engine;

import java.io.IOException;
import java.util.ArrayList;

import ru.megazlo.fastfile.components.RowData;
import ru.megazlo.fastfile.components.RowDataFTP;
import ru.megazlo.fastfile.components.filerow.FileList;
import ru.megazlo.fastfile.components.filerow.FileRowData;
import ru.megazlo.fastfile.util.Sets;
import ru.megazlo.fastfile.util.net.BrowseNet;
import ru.megazlo.ftplib.ftp.FTPFile;
import android.widget.Toast;

public class EngineFTP extends BaseEngine {

	public EngineFTP(RowData data, FileList list) {
		super(list);
		engType = BaseEngine.FTP;
		dat = data;
	}

	@Override
	public boolean browseUp() {
		getDat().CUR_DIR = new FTPFile();
		new BrowseNet().execute("up", this, "true");
		return true;
	}

	@Override
	public void browseCatalog(Object cat) {
		RowDataFTP dt = getDat();
		dt.CUR_DIR = (FTPFile) cat;
		if (dt.CUR_DIR.isDirectory() || dt.CUR_DIR.isSymbolicLink()) {
			String dirb = dt.CUR_DIR.isDirectory() ? dt.CUR_DIR.getName() : dt.CUR_DIR.getLink();
			new BrowseNet().execute(dirb, this);
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
		if (getDat().CUR_DIR != null)
			new BrowseNet().execute("", this);
	}

	@Override
	public void browseRoot() {
		new BrowseNet().execute("/");
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
		dat.dir.clear();
		dat.fil.clear();
		FTPFile[] files = (FTPFile[]) filar;
		try {
			mTitle = '/' + getDat().FTP_CLIENT.getRemoteAddress().getHostName() + getDat().FTP_CLIENT.printWorkingDirectory();
		} catch (IOException e) {
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
				dat.dir.add(new FileRowData(files[i], Sets.I_FOLD));
			else
				dat.fil.add(new FileRowData(files[i], getIconByFile(files[i].getName())));
		}
		super.fill(filar);
	}

	@Override
	public Object exec(int cmd) {
		switch (cmd) {
		case BaseEngine.CMD_CON:
			return execConnect(getList().getContext(), true);
		default:
			return null;
		}
	}

}
