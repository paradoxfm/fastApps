package ru.zlo.ff.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import ru.zlo.ff.R;
import ru.zlo.ff.components.RowData;
import ru.zlo.ff.components.filerow.FileRowData;
import ru.zlo.ff.util.Options;
import ru.zlo.ff.util.ThumbnailLoader;
import ru.zlo.ff.util.file.MimeTypes;

import java.util.Collections;
import java.util.List;

public abstract class BaseEngine implements IEngine {

	public static final int SDC = 0;
	public static final int CMD_CON = 100;
	public int scrollPoz = 0;

	protected boolean isSearsh = false;
	protected boolean isAllowSearsh = false;
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
	protected Context context;

	public interface OnLoadFinish {
		void onLoadFinish(List<FileRowData> dataRows);
	}

	public interface OnDataChanged {
		void onChange();
	}

	public int getType() {
		return engType;
	}

	public BaseEngine(Context context) {
		this.context = context;
		if (currentHandler == null)
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
		if (Options.SHOW_APK || Options.SHOW_IMG || Options.SHOW_MP3) {
			tmbl = new ThumbnailLoader(dat.dir, currentHandler, context, mimetypes);
			tmbl.start();
		}
	}

	@SuppressWarnings("StringEquality")
	protected Drawable getIconByFile(String fil) {
		String tmp = getExtension(fil);
		if (tmp == null)
			return Options.i_file_non;
		tmp = tmp.toLowerCase().intern();
		short i;
		for (i = 0; i < Options.docs.length; i++)
			if (tmp == Options.docs[i])
				return Options.i_file_doc;
		for (i = 0; i < Options.imgs.length; i++)
			if (tmp == Options.imgs[i])
				return Options.i_file_img;
		for (i = 0; i < Options.muss.length; i++)
			if (tmp == Options.muss[i])
				return Options.i_file_mus;
		for (i = 0; i < Options.bin.length; i++)
			if (tmp == Options.bin[i])
				return Options.i_file_bin;
		for (i = 0; i < Options.vids.length; i++)
			if (tmp == Options.vids[i])
				return Options.i_file_mov;
		return Options.i_file_non;
	}

	@SuppressWarnings("StringEquality")
	public static int getType(String lowerCase) {
		String tmp = getExtension(lowerCase).toLowerCase().intern();
		short i;
		for (i = 0; i < Options.muss.length; i++)
			if (tmp == Options.muss[i])
				return FileRowData.TP_MUSIC;
		for (i = 0; i < Options.imgs.length; i++)
			if (tmp == Options.imgs[i])
				return FileRowData.TP_BITMAP;
		return FileRowData.TP_OTHER;
	}

	private static String getExtension(String uri) {
		if (uri == null)
			return null;
		int dot = uri.lastIndexOf(".");
		return dot >= 0 ? uri.substring(dot + 1) : "";
	}

	@Override
	public String getTitle() {
		return "\\" + mTitle;
	}

	public void setOnLoadFinish(OnLoadFinish onLoadFinish) {
		finisher = onLoadFinish;
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

	public Integer getIcoProtocol() {
		switch (engType) {
			case SDC:
				return R.drawable.prot_pda;
			default:
				return R.drawable.prot_pda;
		}
	}

	@Override
	public boolean isAllowSearsh() {
		return isAllowSearsh;
	}

	@Override
	public void fill(Object filar) {
		Collections.sort(dat.dir);
		Collections.sort(dat.fil);
		dat.dir.addAll(dat.fil);
		setOffset();
		if (finisher != null) {
			finisher.onLoadFinish(dat.dir);
			startLoadImage();
		}
	}

	public void setOffset() {
	}

	@Override
	public void stopThreads() {
		if (tmbl != null) {
			tmbl.cancel = true;
			tmbl = null;
		}
	}

}
