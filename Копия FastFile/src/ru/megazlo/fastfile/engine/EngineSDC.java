package ru.megazlo.fastfile.engine;

import java.io.File;
import java.util.Collections;

import ru.megazlo.fastfile.R;
import ru.megazlo.fastfile.fmMain;
import ru.megazlo.fastfile.components.RowData;
import ru.megazlo.fastfile.components.RowDataSD;
import ru.megazlo.fastfile.components.filerow.FileRowData;
import ru.megazlo.fastfile.util.Sets;
import ru.megazlo.fastfile.util.file.FileTools;
import ru.megazlo.fastfile.util.file.Search;
import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class EngineSDC extends BaseEngine {

	private RowDataSD dat;

	public EngineSDC(RowData data, Context context, BaseAdapter adpter, boolean rest) {
		super(context, adpter);
		isPreview = isSearsh = true;
		isRestore = rest;
		dat = (RowDataSD) data;
		update();
		isRestore = false;
	}

	@Override
	public int browseUp() {
		File fil = dat.PATH.getParentFile();
		String from = dat.PATH.getName();
		if (this.isSearsh) {
			browseCatalog(dat.PATH);
			this.isSearsh = false;
			return 0;
		} else if (fil != null) {
			browseCatalog(fil);
			for (int i = 0; i < dat.dir.size(); i++)
				if (from.equals(((File) dat.dir.get(i).getFile()).getName()))
					return i;
		} else
			return -1;
		return -1;
	}

	@Override
	public void browseCatalog(Object cat) {
		File dir = (File) cat;
		if (!dir.canRead()) {
			Toast.makeText(fmMain.CONTEXT, R.string.read_only, Toast.LENGTH_SHORT).show();
			return;
		}
		dat.PATH = dir;
		mTitle = dat.PATH.getAbsolutePath();
		this.fill(dat.PATH.listFiles());
	}

	@Override
	public void fill(Object filar) {
		if (tmbl != null) {
			tmbl.cancel = true;
			tmbl = null;
		}
		if (!isRestore) {
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
		}
		if (!isRestore) {
			Collections.sort(dat.dir);
			Collections.sort(dat.fil);
			dat.dir.addAll(dat.fil);
		}
		finisher.onFinish();
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
		browseCatalog(dat.PATH);
	}

	@Override
	public void browseRoot() {
		browseCatalog(new File("/"));
	}

	@Override
	public void search(String search) {
		new Search(search).execute(FileTools.FROM != null ? (File[]) FileTools.FROM : new File[] { dat.PATH });
		isSearsh = true;
		mTitle = "/" + search;
		Toast.makeText(cont, R.string.search_rez, Toast.LENGTH_LONG).show();
	}

	@Override
	public RowData getDat() {
		return dat;
	}

	@Override
	public Object getCurrentDir() {
		return dat.PATH;
	}

	@Override
	public boolean isAllowSearsh() {
		return isSearsh;
	}

}
