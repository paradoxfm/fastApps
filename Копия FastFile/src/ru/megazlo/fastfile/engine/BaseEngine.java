package ru.megazlo.fastfile.engine;

import ru.megazlo.fastfile.components.RowData;
import ru.megazlo.fastfile.util.Sets;
import ru.megazlo.fastfile.util.ThumbnailLoader;
import ru.megazlo.fastfile.util.file.MimeTypes;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;

public abstract class BaseEngine implements IEngine {

	protected boolean isSearsh = false;
	protected boolean isPreview = false;
	protected boolean isRestore = false;
	protected String mTitle;
	protected OnScrollFinish finisher;
	private RowData dat;
	protected ThumbnailLoader tmbl;
	private Handler currentHandler;
	protected Context cont;
	private static MimeTypes mimetypes = new MimeTypes();

	public interface OnScrollFinish {
		void onFinish();
	}

	public BaseEngine(Context context, final BaseAdapter adpt) {
		cont = context;
		currentHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (adpt != null)
					adpt.notifyDataSetChanged();
			}
		};
	}

	public void startLoadImage() {
		if (!isPreview)
			return;
		if (Sets.SHOW_APK || Sets.SHOW_IMG || Sets.SHOW_MP3) {
			tmbl = new ThumbnailLoader(dat.fil, currentHandler, cont, mimetypes);
			// TODO: dat.fil или все таки dat.dir
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

	public void setOnScrollFinish(OnScrollFinish onScrollFinish) {
		finisher = onScrollFinish;
	}
}
