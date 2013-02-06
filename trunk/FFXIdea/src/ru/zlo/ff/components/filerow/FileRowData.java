package ru.zlo.ff.components.filerow;

import android.graphics.drawable.Drawable;
import ru.zlo.ff.util.Options;
import ru.zlo.ff.util.Sets;

import java.io.File;
import java.util.Date;

public class FileRowData implements Comparable<FileRowData> {

	public final static byte TP_BITMAP = 0;
	public final static byte TP_MUSIC = 1;
	public final static byte TP_VIDEO = 2;
	public final static byte TP_TEXT = 3;
	public final static byte TP_OTHER = 4;

	private File m_file_sdc;
	private String m_itm_txt, m_dat_txt, m_dir_txt;
	private Drawable m_icon;
	private boolean m_Selectable = true;
	private boolean m_Checked = false;

	public FileRowData(File fil, Drawable curIcon) {
		m_file_sdc = fil;
		m_icon = curIcon;
		initData(fil.isDirectory(), fil.getName());
	}

	private void initData(Boolean isDir, String name) {
		// m_icon = isDir ? Sets.I_FOLD : Sets.I_FILE_NON;
		m_itm_txt = setItmText();
		m_dat_txt = setDatText();
		m_dir_txt = setDirText();
	}

	private String setItmText() {
		if (m_file_sdc != null) {
			if (m_file_sdc.isDirectory())
				return m_file_sdc.canRead() ? Integer.toString(m_file_sdc.listFiles().length) + " items" : "n/a";
			else
				return getSize(m_file_sdc.length());
		}
		return "";
	}

	private String getSize(float len) {
		String perf = "B";
		if (len >= 1024) {
			len = len / 1024;
			perf = "KB";
		}
		if (len >= 1024) {
			len = len / 1024;
			perf = "MB";
		}
		if (len >= 1024) {
			len = len / 1024;
			perf = "GB";
		}
		String str = Float.toString(len);
		str = str.substring(0, str.indexOf(".") + 2);
		return str + ' ' + perf;
	}

	@Override
	public int compareTo(FileRowData row) {
		if (m_file_sdc != null)
			return this.getName().toLowerCase().compareTo(row.getName().toLowerCase());
		else
			throw new IllegalArgumentException();
	}

	public String getName() {
		return m_dir_txt;
	}

	public boolean isSelectable() {
		return m_Selectable;
	}

	public void setSelectable(boolean selectable) {
		this.m_Selectable = selectable;
	}

	public Drawable getIcon() {
		return this.m_icon;
	}

	private String setDirText() {
		if (m_file_sdc != null)
			return m_file_sdc.getName();
		return "";
	}

	public String getItmText() {
		return m_itm_txt;
	}

	public String getDateText() {
		return this.m_dat_txt;
	}

	private String setDatText() {
		Date dat = null;
		if (m_file_sdc != null)
			dat = new Date(m_file_sdc.lastModified());
		return getDateFormat(dat);
	}

	private String getDateFormat(Date dt) {
		String rez = Options.F_DATE.format(dt) + " " + Options.F_TIME.format(dt) + " | ";
		if (m_file_sdc != null) {
			rez += m_file_sdc.isDirectory() ? "d" : "-";
			rez += m_file_sdc.canRead() ? "r" : "-";
			rez += m_file_sdc.canWrite() ? "w" : "-";
			rez += m_file_sdc.isHidden() ? "h" : "-";
		}

		return rez;
	}

	public void setIcon(Drawable ico) {
		this.m_icon = ico;
	}

	@SuppressWarnings("unchecked")
	public <T> T getFile() {
		if (m_file_sdc != null)
			return (T) m_file_sdc;
		return null;
	}

	public void setChecked(boolean chck) {
		this.m_Checked = chck;
	}

	public boolean isChecked() {
		return m_Checked;
	}

	public Drawable getIconCheck() {
		return m_Checked ? Options.i_chk : Options.i_unchk;
	}

}
