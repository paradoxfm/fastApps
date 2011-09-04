package ru.megazlo.fastfile.engine;

import java.io.File;

import ru.megazlo.fastfile.R;
import ru.megazlo.fastfile.fmMain;
import ru.megazlo.fastfile.components.RowData;
import ru.megazlo.fastfile.components.RowDataSD;
import ru.megazlo.fastfile.components.filerow.FileList;
import ru.megazlo.fastfile.components.filerow.FileRowData;
import ru.megazlo.fastfile.util.Sets;
import ru.megazlo.fastfile.util.file.FileTools;
import ru.megazlo.fastfile.util.file.Search;
import android.widget.Toast;

public class EngineSDC extends BaseEngine {

	public EngineSDC(RowData data, FileList list) {
		super(list);
		isPreview = isAllowSearsh = true;
		dat = data;
	}

	@Override
	public boolean browseUp() {
		File fil = getDat().PATH.getParentFile();
		String from = getDat().PATH.getName();
		if (this.isSearsh) {
			browseCatalog(getDat().PATH);
			this.isSearsh = false;
			return true;
		} else if (fil != null) {
			browseCatalog(fil);
			for (int i = 0; i < dat.dir.size(); i++)
				if (from.equals(((File) dat.dir.get(i).getFile()).getName()))
					return true;
		}
		return false;
	}

	@Override
	public void browseCatalog(Object cat) {
		File dir = (File) cat;
		if (!dir.canRead()) {
			Toast.makeText(fmMain.CONTEXT, R.string.read_only, Toast.LENGTH_SHORT).show();
			return;
		}
		getDat().PATH = dir;
		mTitle = getDat().PATH.getAbsolutePath();
		this.fill(getDat().PATH.listFiles());
	}

	@Override
	public void fill(Object filar) {
		if (tmbl != null) {
			tmbl.cancel = true;
			tmbl = null;
		}
		dat.dir.clear();
		dat.fil.clear();
		File[] files = (File[]) filar;
		for (int i = 0; i < files.length; i++) {
			if (!Sets.SHOW_HIDDEN && files[i].isHidden())
				continue;
			if (files[i].isDirectory())
				dat.dir.add(new FileRowData(files[i], Sets.I_FOLD));
			else
				dat.fil.add(new FileRowData(files[i], getIconByFile(files[i].getName())));
		}
		super.fill(filar);
	}

	@Override
	public Object[] getFiles() {
		int count = 0, size = dat.dir.size();
		for (int i = 0; i < size; i++)
			if (dat.dir.get(i).isChecked())
				count++;
		File[] fls = new File[count];
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
		browseCatalog(new File("/"));
	}

	@Override
	public void search(String search) {
		new Search(search).execute(FileTools.FROM != null ? (File[]) FileTools.FROM : new File[] { getDat().PATH });
		isSearsh = true;
		mTitle = "/" + search;
		Toast.makeText(getList().getContext(), R.string.search_rez, Toast.LENGTH_LONG).show();
	}

	@Override
	public RowDataSD getDat() {
		return (RowDataSD) dat;
	}

	@Override
	public Object getCurrentDir() {
		return getDat().PATH;
	}

	@Override
	public Object exec(int cmd) {
		return null;
	}

}
