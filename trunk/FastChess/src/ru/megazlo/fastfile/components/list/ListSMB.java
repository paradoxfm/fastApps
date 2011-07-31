package ru.megazlo.fastfile.components.list;

import java.net.MalformedURLException;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import ru.megazlo.fastfile.R;
import ru.megazlo.fastfile.fmMain;
import ru.megazlo.fastfile.components.RowDataSMB;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.widget.Toast;

public class ListSMB extends ListBase {
	private RowDataSMB dat;

	public ListSMB(Context context, RowDataSMB dat) {
		super(context);
		this.dat = dat;
	}

	@Override
	public boolean browseUp() {
		// TODO Auto-generated method stub
		SmbFile fil = null;
		try {
			fil = new SmbFile(dat.PATH.getParent());
		} catch (MalformedURLException e) {
		}
		String from = dat.PATH.getName();
		if (fil != null) {
			browseCatalog(fil);
			int pos = 0;
			for (int i = 0; i < this.dirEntr.size(); i++) {
				SmbFile fl = this.dirEntr.get(i).getFile();
				if (from.equals(fl.getName())) {
					pos = i;
					break;
				}
			}
			this.setSelection(pos);
			return true;
		}
		return false;
	}

	@Override
	protected void browseCatalog(Object filed) {
		SmbFile dir = (SmbFile) filed;
		try {
			if (!dir.canRead()) {
				Toast.makeText(fmMain.CONTEXT, R.string.read_only, Toast.LENGTH_SHORT).show();
				return;
			}
		} catch (SmbException e1) {
		} catch (NotFoundException e1) {
		}
		dat.PATH = dir;
		mTitle = dat.PATH.getCanonicalPath();
		try {
			this.fill(dat.PATH.listFiles());
		} catch (SmbException e) {
		}
	}

	@Override
	public void update() {
		browseCatalog(dat.PATH);
	}

	@Override
	public Object[] getFiles() {
		int count = 0, size = dirEntr.size();
		for (int i = 0; i < size; i++)
			if (dirEntr.get(i).isChecked())
				count++;
		SmbFile[] fls = new SmbFile[count];
		count = 0;
		for (int i = 0; i < size; i++)
			if (dirEntr.get(i).isChecked()) {
				fls[count] = dirEntr.get(i).getFile();
				count++;
			}
		return fls;
	}

	@Override
	public void browseRoot() {
		// TODO Auto-generated method stub
	}

	@Override
	public void search(String search) {
		Toast.makeText(this.getContext(), "Поиск запрещен", Toast.LENGTH_SHORT).show();
		// TODO: текст сука
	}

	@Override
	public Object getCurrentDir() {
		return dat.PATH;
	}

	public RowDataSMB getDat() {
		return dat;
	}

}
