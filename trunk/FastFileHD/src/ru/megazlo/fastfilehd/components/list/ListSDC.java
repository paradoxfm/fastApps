package ru.megazlo.fastfilehd.components.list;

import java.io.File;

import ru.megazlo.fastfilehd.R;
import ru.megazlo.fastfilehd.fmMain;
import ru.megazlo.fastfilehd.components.RowDataSD;
import ru.megazlo.fastfilehd.components.filerow.FileRowData;
import ru.megazlo.fastfilehd.util.Sets;
import ru.megazlo.fastfilehd.util.ThumbnailLoader;
import ru.megazlo.fastfilehd.util.file.FileTools;
import ru.megazlo.fastfilehd.util.file.MimeTypes;
import ru.megazlo.fastfilehd.util.file.Search;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class ListSDC extends ListBase {
	private RowDataSD dat;
	private static MimeTypes mimetypes = new MimeTypes();
	ThumbnailLoader tmbl;
	Handler currentHandler;

	public ListSDC(Context context, RowDataSD dat, boolean rest) {
		super(context);
		this.mIcoProtocol = Sets.P_PDA;
		isRestore = rest;
		this.dat = dat;
		this.filEntr = dat.fil;
		this.dirEntr = dat.dir;
		browseCatalog(dat.PATH);
		isRestore = false;
		currentHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (ListSDC.this.getAdapter() != null) {
					((BaseAdapter) ListSDC.this.getAdapter()).notifyDataSetChanged();
				}
			}
		};
	}

	@Override
	protected void browseCatalog(Object filed) {
		File dir = (File) filed;
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
			this.dirEntr.clear();
			this.filEntr.clear();
			File[] files = (File[]) filar;
			for (int i = 0; i < files.length; i++) {
				if (!Sets.SHOW_HIDDEN && files[i].isHidden())
					continue;
				if (files[i].isDirectory())
					this.dirEntr.add(new FileRowData(files[i], Sets.I_FOLD));
				else
					this.filEntr.add(new FileRowData(files[i], getIconByFile(files[i].getName())));
			}
		}
		super.fill(filar);
		if (Sets.SHOW_APK || Sets.SHOW_IMG || Sets.SHOW_MP3) {
			tmbl = new ThumbnailLoader(dirEntr, currentHandler, this.getContext(), mimetypes);
			tmbl.start();
		}
	}

	@Override
	public boolean performItemClick(View view, int position, long id) {
		super.performItemClick(view, position, id);
		File curFile = dirEntr.get(position).getFile();
		if (curFile.isDirectory()) {
			browseCatalog(curFile);
		} else if (Sets.OPEN_THIS) {
			FileTools.openFileThis(this.getContext(), curFile);
		} else if (!Sets.OPEN_THIS) {
			FileTools.openFileExt(this.getContext(), curFile);
		}
		return false;
	}

	@Override
	public boolean browseUp() {
		File fil = dat.PATH.getParentFile();
		String from = dat.PATH.getName();
		if (this.isSearsh) {
			browseCatalog(dat.PATH);
			this.isSearsh = false;
			return true;
		} else if (fil != null) {
			browseCatalog(fil);
			int pos = 0;
			for (int i = 0; i < this.dirEntr.size(); i++) {
				File fl = this.dirEntr.get(i).getFile();
				if (from.equals(fl.getName())) {
					pos = i;
					break;
				}
			}
			this.setSelection(pos);
			return true;
		} else {
			return false;
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
		File[] fls = new File[count];
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
		browseCatalog(new File("/"));
	}

	@Override
	public void search(String search) {
		new Search(search).execute(FileTools.FROM != null ? (File[]) FileTools.FROM : new File[] { dat.PATH });
		isSearsh = true;
		mTitle = "/" + search;
		Toast.makeText(this.getContext(), R.string.search_rez, Toast.LENGTH_LONG).show();
	}

	public RowDataSD getDat() {
		return dat;
	}

	@Override
	public Object getCurrentDir() {
		return dat.PATH;
	}
}
