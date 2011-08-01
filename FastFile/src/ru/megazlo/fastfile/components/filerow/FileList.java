package ru.megazlo.fastfile.components.filerow;

import java.io.File;

import ru.megazlo.fastfile.fmMain;
import ru.megazlo.fastfile.components.RowData;
import ru.megazlo.fastfile.components.RowDataFTP;
import ru.megazlo.fastfile.components.RowDataSMB;
import ru.megazlo.fastfile.engine.BaseEngine;
import ru.megazlo.fastfile.engine.EngineFTP;
import ru.megazlo.fastfile.engine.EngineSDC;
import ru.megazlo.fastfile.util.Sets;
import ru.megazlo.fastfile.util.file.FileTools;
import ru.megazlo.ftplib.ftp.FTPFile;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class FileList extends ListView {

	protected Drawable mIcoProtocol;
	private BaseEngine eng;
	FileRowAdapter itla;

	public FileList(fmMain context, RowData dat, boolean restore) {
		super(context);
		setScrollingCacheEnabled(false);
		this.setPadding(0, 10, 0, 0);
		itla = new FileRowAdapter(FileList.this.getContext());
		setAdapter(itla);
		eng = choiceEngine(dat, restore);

		eng.setOnScrollFinish(new BaseEngine.OnLoadFinish() {
			@Override
			public void onFinish() {
				itla.setListItems(FileList.this.getEngine().getDat().dir);
				itla.notifyDataSetChanged();
				if (Sets.ANIMATE) {
					if (FileList.this.getLayoutAnimation() == null)
						FileList.this.setLayoutAnimation(Sets.LIST_ANIM);
					else
						FileList.this.startLayoutAnimation();
				}
				FileList.this.getEngine().startLoadImage();
			}
		});

		eng.setOnDataChanger(new BaseEngine.OnDataChanged() {
			@Override
			public void onChange() {
				((BaseAdapter) FileList.this.getAdapter()).notifyDataSetChanged();
			}
		});

		eng.update();
	}

	@Override
	public boolean performItemClick(View view, int position, long id) {
		switch (eng.getType()) {
		case BaseEngine.SDC:
			localClick(position);
			break;
		case BaseEngine.FTP:
			ftpClick(position);
			break;
		}
		return super.performItemClick(view, position, id);
	}

	private void localClick(int pos) {
		File curFile = eng.getDat().dir.get(pos).getFile();
		if (curFile.isDirectory())
			eng.browseCatalog(curFile);
		else if (Sets.OPEN_THIS)
			FileTools.openFileThis(this.getContext(), curFile);
		else if (!Sets.OPEN_THIS)
			FileTools.openFileExt(this.getContext(), curFile);
	}

	private void ftpClick(int pos) {
		FTPFile curFile = eng.getDat().dir.get(pos).getFile();
		if (curFile.isDirectory() || curFile.isSymbolicLink())
			eng.browseCatalog(curFile);
	}

	private BaseEngine choiceEngine(RowData dat, boolean restore) {
		if (dat.getClass() == RowDataFTP.class)
			return new EngineFTP(dat, this, restore);
		else if (dat.getClass() == RowDataSMB.class)
			return null;
		return new EngineSDC(dat, this, restore);
	}

	public BaseEngine getEngine() {
		return eng;
	}

}
