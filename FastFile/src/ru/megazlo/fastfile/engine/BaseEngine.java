package ru.megazlo.fastfile.engine;

import java.util.Collections;

import ru.megazlo.fastfile.components.RowData;
import ru.megazlo.fastfile.components.filerow.FileList;
import ru.megazlo.fastfile.util.MenuChecker;
import ru.megazlo.fastfile.util.Sets;
import ru.megazlo.fastfile.util.ThumbnailLoader;
import ru.megazlo.fastfile.util.file.MimeTypes;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public abstract class BaseEngine implements IEngine {

	public static final int SDC = 0;
	public static final int FTP = 1;
	public static final int LAN = 2;

	public static final int CMD_CON = 100;

	protected boolean isSearsh = false;
	protected boolean isPreview = false;
	// protected boolean isRestore = false;
	protected int engType = SDC;
	protected String mTitle;
	protected OnLoadFinish finisher;
	protected OnDataChanged changer;
	protected RowData dat;
	protected ThumbnailLoader tmbl;
	private Handler currentHandler;
	private static MimeTypes mimetypes = new MimeTypes();
	private FileList parent;

	protected DialogInterface.OnClickListener cansl = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dlg, int which) {
			MenuChecker.remList(getList());
		}
	};
	protected DialogInterface.OnCancelListener back = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			MenuChecker.remList(getList());
		}
	};

	public interface OnLoadFinish {
		void onFinish();
	}

	public interface OnDataChanged {
		void onChange();
	}

	public int getType() {
		return engType;
	}

	public BaseEngine(FileList list) {
		parent = list;
		currentHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (changer != null)
					changer.onChange();
			}
		};
	}

	public void startLoadImage() {
		if (!isPreview)
			return;
		if (Sets.SHOW_APK || Sets.SHOW_IMG || Sets.SHOW_MP3) {
			tmbl = new ThumbnailLoader(dat.dir, currentHandler, parent.getContext(), mimetypes);
			tmbl.start();
		}
	}

	protected Drawable getIconByFile(String fil) {
		String tmp = getExtension(fil).toLowerCase().intern();
		if (tmp == null)
			return Sets.I_FILE_NON;
		short i;
		for (i = 0; i < Sets.FILE_DOC.length; i++)
			if (tmp == Sets.FILE_DOC[i])
				return Sets.I_FILE_DOC;
		for (i = 0; i < Sets.FILE_IMG.length; i++)
			if (tmp == Sets.FILE_IMG[i])
				return Sets.I_FILE_IMG;
		for (i = 0; i < Sets.FILE_MUS.length; i++)
			if (tmp == Sets.FILE_MUS[i])
				return Sets.I_FILE_MUS;
		for (i = 0; i < Sets.FILE_BIN.length; i++)
			if (tmp == Sets.FILE_BIN[i])
				return Sets.I_FILE_BIN;
		for (i = 0; i < Sets.FILE_MOV.length; i++)
			if (tmp == Sets.FILE_MOV[i])
				return Sets.I_FILE_MOV;
		return Sets.I_FILE_NON;
	}

	private static String getExtension(String uri) {
		if (uri == null)
			return null;
		int dot = uri.lastIndexOf(".");
		return dot >= 0 ? uri.substring(dot + 1) : "";
	}

	@Override
	public String getTitle() {
		return mTitle;
	}

	public void setOnScrollFinish(OnLoadFinish onScrollFinish) {
		finisher = onScrollFinish;
	}

	public void setOnDataChanger(OnDataChanged onDataChanged) {
		changer = onDataChanged;
	}

	@Override
	public void selectAll() {
		for (int i = 0; i < dat.dir.size(); i++)
			dat.dir.get(i).setChecked(!dat.dir.get(i).isChecked());
		if (changer != null)
			changer.onChange();
	}

	public Drawable getIcoProtocol() {
		switch (engType) {
		case SDC:
			return Sets.P_PDA;
		case FTP:
			return Sets.P_FTP;
		case LAN:
			return Sets.P_SMB;
		default:
			return Sets.P_PDA;
		}
	}

	public FileList getList() {
		return parent;
	}

	@Override
	public boolean isAllowSearsh() {
		return isSearsh;
	}

	@Override
	public void fill(Object filar) {
		Collections.sort(dat.dir);
		Collections.sort(dat.fil);
		dat.dir.addAll(dat.fil);
		if (finisher != null)
			finisher.onFinish();
	}

	@Override
	public void stopThreads() {
		if (tmbl != null) {
			tmbl.cancel = true;
			tmbl = null;
		}
	}
}
