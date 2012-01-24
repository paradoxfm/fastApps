package ru.megazlo.ffng.engine;

import jcifs.smb.SmbFile;
import ru.megazlo.ffng.components.RowData;
import ru.megazlo.ffng.components.RowDataLAN;
import ru.megazlo.ffng.components.filerow.FileList;
import ru.megazlo.ffng.components.filerow.FileRowData;
import ru.megazlo.ffng.util.Sets;
import ru.megazlo.ffng.util.net.BrowseNet;

public class EngineLAN extends BaseEngine {

	public EngineLAN(RowData data, FileList list) {
		super(list);
		engType = BaseEngine.LAN;
		dat = data;
	}

	@Override
	public boolean browseUp() {

		// getDat().CUR_DIR = new FTPFile();
		// new BrowseNet().execute("up", this, "true");
		// return true;

		SmbFile fil = null;
		try {
			fil = new SmbFile(getDat().PATH.getParent());
		} catch (Exception e) {
		}
		// String from = getDat().PATH.getName();
		if (fil != null) {
			browseCatalog(fil);
			// int pos = 0;
			// for (int i = 0; i < dat.dir.size(); i++) {
			// SmbFile fl = dat.dir.get(i).getFile();
			// if (from.equals(fl.getName())) {
			// pos = i;
			// break;
			// }
			// }
			// this.setSelection(pos);
			return true;
		}
		return false;
	}

	@Override
	public void browseCatalog(Object cat) {
		try {
			RowDataLAN dt = getDat();
			dt.PATH = (SmbFile) cat;
			if (dt.PATH.isDirectory())
				new BrowseNet().execute(dt.PATH, this);
		} catch (Exception e) {
		}
		// try {
		//
		// String url = "smb://172.30.9.17/";
		// NtlmPasswordAuthentication auth = new
		// NtlmPasswordAuthentication("regions.alfaintra.net", "u_06sqa",
		// "Qwer1234");
		// SmbFile dir = new SmbFile(url, auth);
		//
		// // (SmbFile) cat;
		// getDat().PATH = dir;
		// if (!dir.canRead()) {
		// Toast.makeText(getList().getContext(), R.string.read_only,
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		// getDat().PATH = dir;
		// mTitle = getDat().PATH.getCanonicalPath();
		// SmbFile[] arr = dir.listFiles();
		// this.fill(arr);
		// } catch (Exception e) {
		// }
	}

	@Override
	public Object[] getFiles() {
		int count = 0, size = dat.dir.size();
		for (int i = 0; i < size; i++)
			if (dat.dir.get(i).isChecked())
				count++;
		SmbFile[] fls = new SmbFile[count];
		count = 0;
		for (int i = 0; i < size; i++)
			if (dat.dir.get(i).isChecked()) {
				fls[count] = dat.dir.get(i).getFile();
				count++;
			}
		return fls;
	}

	@Override
	public void update() {
		if (getDat().PATH != null)
			browseCatalog(getDat().PATH);
	}

	@Override
	public void browseRoot() {
		// TODO Auto-generated method stub
	}

	@Override
	public void search(String search) {
		// TODO Auto-generated method stub
	}

	@Override
	public RowDataLAN getDat() {
		return (RowDataLAN) dat;
	}

	@Override
	public Object getCurrentDir() {
		return getDat().PATH;
	}

	@Override
	public void fill(Object filar) {
		dat.dir.clear();
		dat.fil.clear();
		SmbFile[] files = (SmbFile[]) filar;
		mTitle = '/' + getDat().PATH.getCanonicalPath();
		for (int i = 0; i < files.length; i++) {
			try {
				if (!Sets.SHOW_HIDDEN && files[i].isHidden())
					continue;
				if (files[i].isDirectory())
					dat.dir.add(new FileRowData(files[i], Sets.I_FOLD));
				else
					dat.fil.add(new FileRowData(files[i], getIconByFile(files[i].getName())));
			} catch (Exception e) {
			}
		}
		super.fill(filar);

		// dat.dir.clear();
		// dat.fil.clear();
		// FTPFile[] files = (FTPFile[]) filar;
		// try {
		// mTitle = '/' + getDat().FTP_CLIENT.getRemoteAddress().getHostName() +
		// getDat().FTP_CLIENT.printWorkingDirectory();
		// } catch (IOException e) {
		// }
		// for (int i = 0; i < files.length; i++) {
		// if (files[i].isDirectory())
		// dat.dir.add(new FileRowData(files[i], Sets.I_FOLD));
		// else
		// dat.fil.add(new FileRowData(files[i],
		// getIconByFile(files[i].getName())));
		// }
		// super.fill(filar);

	}

	@Override
	public Object exec(int cmd) {
		switch (cmd) {
		case BaseEngine.CMD_CON:
			return execConnect(getList().getContext(), false);
		default:
			return null;
		}
	}

}
