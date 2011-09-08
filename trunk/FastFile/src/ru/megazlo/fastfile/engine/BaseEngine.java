package ru.megazlo.fastfile.engine;

import java.util.Collections;

import ru.megazlo.fastfile.R;
import ru.megazlo.fastfile.components.RowData;
import ru.megazlo.fastfile.components.filerow.FileList;
import ru.megazlo.fastfile.components.filerow.FileRowData;
import ru.megazlo.fastfile.util.ConnectionRecort;
import ru.megazlo.fastfile.util.MenuChecker;
import ru.megazlo.fastfile.util.Sets;
import ru.megazlo.fastfile.util.ThumbnailLoader;
import ru.megazlo.fastfile.util.file.MimeTypes;
import ru.megazlo.fastfile.util.net.ConnectNet;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public abstract class BaseEngine implements IEngine {

	public static final int SDC = 0;
	public static final int FTP = 1;
	public static final int LAN = 2;

	public static final int CMD_CON = 100;

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

	protected DialogInterface.OnClickListener editdom = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dlg, int which) {
			AlertDialog alr = (AlertDialog) dlg;
			EditText ed = (EditText) alr.findViewById(R.id.ed_ftp_host);
			String hst = ed.getEditableText().toString();
			CheckBox cb = (CheckBox) alr.findViewById(R.id.ed_ftp_anm);
			ed = (EditText) alr.findViewById(R.id.ed_ftp_usr);
			String usr = ed.getEditableText().toString();
			ed = (EditText) alr.findViewById(R.id.ed_ftp_pwd);
			String pwd = ed.getEditableText().toString();
			Sets.insertFtpRec(hst, usr, cb.isChecked());
			new ConnectNet().execute(hst, Boolean.toString(cb.isChecked()), usr, pwd, BaseEngine.this);
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

	public static int getType(String lowerCase) {
		String tmp = getExtension(lowerCase).toLowerCase().intern();
		short i;
		for (i = 0; i < Sets.FILE_MUS.length; i++)
			if (tmp == Sets.FILE_MUS[i])
				return FileRowData.TP_MUSIC;
		for (i = 0; i < Sets.FILE_IMG.length; i++)
			if (tmp == Sets.FILE_IMG[i])
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
		return isAllowSearsh;
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

	protected Object execConnect(Context c, boolean isFTP) {
		LayoutInflater factory = LayoutInflater.from(c);
		final View formcon = factory.inflate(R.layout.connect, null);
		final EditText host = (EditText) formcon.findViewById(R.id.ed_ftp_host);
		final CheckBox anom = (CheckBox) formcon.findViewById(R.id.ed_ftp_anm);
		final EditText user = (EditText) formcon.findViewById(R.id.ed_ftp_usr);
		final View usrinf = formcon.findViewById(R.id.ed_ftp_userinf);
		AlertDialog alr = new AlertDialog.Builder(c).setTitle(R.string.conng).setIcon(R.drawable.ic_menu_globe)
				.setView(formcon).setNegativeButton(R.string.cansel, cansl).setPositiveButton(R.string.ok, editdom)
				.setOnCancelListener(back).create();
		anom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				usrinf.setVisibility(anom.isChecked() ? View.GONE : View.VISIBLE);
			}
		});

		if (Sets.FTPS.size() > 0) {
			final Spinner spn = (Spinner) formcon.findViewById(R.id.ed_ftp_spin);
			final int cnt = Sets.FTPS.size();
			int siz = 0;
			for (int i = 0; i < cnt; i++)
				if (Sets.FTPS.get(i).isFTP == isFTP)
					siz++;
			ConnectionRecort[] recs = new ConnectionRecort[siz];
			for (int i = 0, y = 0; i < cnt; i++)
				if (Sets.FTPS.get(i).isFTP == isFTP) {
					recs[y] = Sets.FTPS.get(i);
					y++;
				}
			ArrayAdapter<ConnectionRecort> adp = new ArrayAdapter<ConnectionRecort>(alr.getContext(),
					android.R.layout.simple_spinner_item, recs);
			adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn.setAdapter(adp);

			spn.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					ConnectionRecort re = (ConnectionRecort) arg0.getSelectedItem();
					host.setText(re.server);
					anom.setChecked(re.anonim);
					user.setText(re.user);
					usrinf.setVisibility(anom.isChecked() ? View.GONE : View.VISIBLE);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});

			ImageView clos = (ImageView) formcon.findViewById(R.id.ed_ftp_del);
			clos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ConnectionRecort rc = (ConnectionRecort) spn.getSelectedItem();
					spn.removeViewInLayout(spn.getSelectedView());
					Sets.deleteFtpRec(rc);
					ConnectionRecort[] recs = new ConnectionRecort[Sets.FTPS.size()];
					for (int i = 0; i < Sets.FTPS.size(); i++)
						recs[i] = Sets.FTPS.get(i);
					ArrayAdapter<ConnectionRecort> adp = new ArrayAdapter<ConnectionRecort>(v.getContext(),
							android.R.layout.simple_spinner_item, recs);
					adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spn.setAdapter(adp);
				}
			});
		}
		alr.show();
		return null;
	}
}
