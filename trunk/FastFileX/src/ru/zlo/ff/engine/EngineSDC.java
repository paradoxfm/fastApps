package ru.zlo.ff.engine;

import java.io.File;

import ru.zlo.ff.R;
import ru.zlo.ff.MAct;
import ru.zlo.ff.components.RowData;
import ru.zlo.ff.components.RowDataSD;
import ru.zlo.ff.components.filerow.FileList;
import ru.zlo.ff.components.filerow.FileRowData;
import ru.zlo.ff.util.Sets;
import ru.zlo.ff.util.file.FileTools;
import ru.zlo.ff.util.file.Search;
import android.widget.Toast;

public class EngineSDC extends BaseEngine {

	private String from;

	public EngineSDC(RowData data, FileList list) {
		super(list);
		isPreview = isAllowSearsh = true;
		dat = data;
	}

	@Override
	public boolean browseUp() {
		File fil = getDat().PATH.getParentFile();
		from = getDat().PATH.getName();
		if (this.isSearsh) {
			browseCatalog(getDat().PATH);
			this.isSearsh = false;
			return true;
		}
		if (fil != null) {
			browseCatalog(fil);
			return true;
		}
		return false;
	}

	@Override
	public void browseCatalog(Object cat) {
		File dir = (File) cat;
		if (!dir.canRead()) {
			Toast.makeText(MAct.I, R.string.read_only, Toast.LENGTH_SHORT).show();
			return;
		}
		getDat().PATH = dir;
		mTitle = getDat().PATH.getName();
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
			if (files[i].isDirectory()) {
				FileRowData dt = new FileRowData(files[i], Sets.I_FOLD); 
				dat.dir.add(dt);
			}
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

	@Override
	public void setOffset() {
		if (from == null)
			return;
		scrollPoz = 0;
		for (int i = 0; i < dat.dir.size(); i++)
			if (from.equals(((File) dat.dir.get(i).getFile()).getName())) {
				scrollPoz = i;
				break;
			}
	}

}