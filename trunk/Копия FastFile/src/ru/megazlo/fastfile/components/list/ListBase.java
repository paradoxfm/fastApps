package ru.megazlo.fastfile.components.list;

import java.util.Collections;
import java.util.List;

import ru.megazlo.fastfile.components.filerow.FileRow;
import ru.megazlo.fastfile.components.filerow.FileRowAdapter;
import ru.megazlo.fastfile.components.filerow.FileRowData;
import ru.megazlo.fastfile.util.MenuChecker;
import ru.megazlo.fastfile.util.Sets;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.widget.ListView;

public abstract class ListBase extends ListView {

	protected boolean isSearsh = false;
	protected boolean isRestore = false;
	protected boolean isAccessSearsh = true;
	protected String mTitle = "";
	protected List<FileRowData> dirEntr;
	protected List<FileRowData> filEntr;
	protected Drawable mIcoProtocol;
	protected DialogInterface.OnClickListener cansl = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dlg, int which) {
			MenuChecker.remList(ListBase.this);
		}
	};
	protected DialogInterface.OnCancelListener back = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			MenuChecker.remList(ListBase.this);
		}
	};

	public ListBase(Context context) {
		super(context);
		this.setPadding(0, 10, 0, 0);
	}

	// -------------- Управление чз команды начало
	public abstract boolean browseUp();

	public abstract void browseRoot();

	protected abstract void browseCatalog(Object dir);

	public abstract void update();

	// -------------- Управление чз команды конец

	public void fill(Object files) {
		if (!isRestore) {
			Collections.sort(this.dirEntr);
			Collections.sort(this.filEntr);
			this.dirEntr.addAll(filEntr);
		}
		FileRowAdapter itla = new FileRowAdapter(this.getContext());
		itla.setListItems(this.dirEntr);
		this.setAdapter(itla);
		if (Sets.ANIMATE) {
			if (this.getLayoutAnimation() == null)
				this.setLayoutAnimation(Sets.LIST_ANIM);
			else
				this.startLayoutAnimation();
		}
	}

	public abstract Object[] getFiles();

	public void selectAll() {
		for (int i = 0; i < dirEntr.size(); i++)
			dirEntr.get(i).setChecked(!dirEntr.get(i).isChecked());
		// for (int i = 0; i < filEntr.size(); i++)
		// filEntr.get(i).setChecked(!filEntr.get(i).isChecked());
		for (int i = 0; i < this.getChildCount(); i++) {
			FileRow rw = (FileRow) this.getChildAt(i);
			rw.updateChecked();
		}
	}

	protected Drawable getIconByFile(String fil) {
		String tmp = getExtension(fil).toLowerCase().intern();
		if (tmp == null)
			return Sets.I_FILE_NON;
		int i;
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

	public String getTitle() {
		return mTitle;
	}

	public Drawable getIcoProtocol() {
		return !isSearsh ? mIcoProtocol : Sets.P_SRH;
	}

	public boolean isAccessSearsh() {
		return isAccessSearsh;
	}

	public abstract void search(String search);

	public abstract Object getCurrentDir();

}
