package ru.megazlo.fastfile.engine;

import java.net.MalformedURLException;

import android.widget.Toast;

import jcifs.smb.SmbFile;
import ru.megazlo.fastfile.R;
import ru.megazlo.fastfile.components.RowData;
import ru.megazlo.fastfile.components.RowDataLAN;
import ru.megazlo.fastfile.components.filerow.FileList;

public class EngineLAN extends BaseEngine {

	public EngineLAN(RowData data, FileList list, boolean rest) {
		super(list);
		engType = BaseEngine.LAN;
		dat = data;
	}

	@Override
	public boolean browseUp() {
		SmbFile fil = null;
		try {
			fil = new SmbFile(getDat().PATH.getParent());
		} catch (MalformedURLException e) {
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
		SmbFile dir = (SmbFile) cat;
		try {
			if (!dir.canRead()) {
				Toast.makeText(getList().getContext(), R.string.read_only, Toast.LENGTH_SHORT).show();
				return;
			}
			getDat().PATH = dir;
			mTitle = getDat().PATH.getCanonicalPath();
			this.fill(getDat().PATH.listFiles());
		} catch (Exception e) {
		}
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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCurrentDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fill(Object filar) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object exec(int cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
