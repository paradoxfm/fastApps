package ru.megazlo.fastfile.engine;

import java.net.MalformedURLException;

import jcifs.smb.SmbFile;
import ru.megazlo.fastfile.R;
import ru.megazlo.fastfile.components.RowData;
import ru.megazlo.fastfile.components.RowDataLAN;
import ru.megazlo.fastfile.components.filerow.FileList;
import ru.megazlo.fastfile.components.filerow.FileRowData;
import ru.megazlo.fastfile.util.Sets;
import android.widget.Toast;

public class EngineLAN extends BaseEngine {

	public EngineLAN(RowData data, FileList list) {
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
	}

	@Override
	public Object exec(int cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
