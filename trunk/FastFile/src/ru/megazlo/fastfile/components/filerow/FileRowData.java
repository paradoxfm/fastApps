package ru.megazlo.fastfile.components.filerow;

import java.io.File;
import java.util.Date;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import ru.megazlo.fastfile.util.Sets;
import ru.megazlo.ftplib.ftp.FTPFile;
import android.graphics.drawable.Drawable;

public class FileRowData implements Comparable<FileRowData> {

	private File m_file_sdc;
	private FTPFile m_file_ftp;
	private SmbFile m_file_smb;
	private String m_itm_txt, m_dat_txt, m_dir_txt;
	private Drawable m_icon;
	// private FileRow m_file_row;
	private boolean m_Selectable = true;
	private boolean m_Checked = false;

	public FileRowData(File fil, Drawable curIcon) {
		m_file_sdc = fil;
		m_icon = curIcon;
		initData(fil.isDirectory(), fil.getName());
	}

	public FileRowData(FTPFile fil, Drawable curIcon) {
		m_file_ftp = fil;
		m_icon = curIcon;
		initData(fil.isDirectory(), fil.getName());
	}

	public FileRowData(SmbFile fil, Drawable curIcon) throws SmbException {
		m_file_smb = fil;
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
		} else if (m_file_ftp != null) {
			if (m_file_ftp.isSymbolicLink())
				return m_file_ftp.getLink();
			else if (m_file_ftp.isFile())
				return getSize(m_file_ftp.getSize());
			else
				return "dir";
		} else if (m_file_smb != null) {
			try {
				if (m_file_smb.isDirectory())
					return m_file_smb.canRead() ? Integer.toString(m_file_smb.listFiles().length) + " items" : "n/a";
				else
					return getSize(m_file_smb.length());
			} catch (SmbException e) {
			}
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
		if (m_file_sdc != null || m_file_ftp != null/* || m_file_smb != null */)
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
		if (m_file_ftp != null)
			return m_file_ftp.getName();
		if (m_file_smb != null)
			return m_file_smb.getName();
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
		if (m_file_ftp != null)
			dat = m_file_ftp.getTimestamp().getTime();
		if (m_file_smb != null)
			try {
				dat = new Date(m_file_smb.lastModified());
			} catch (SmbException e) {
			}
		return getDateFormat(dat);
	}

	private String getDateFormat(Date dt) {
		String rez = Sets.F_DATE.format(dt) + " " + Sets.F_TIME.format(dt) + " | ";
		if (m_file_sdc != null) {
			rez += m_file_sdc.isDirectory() ? "d" : "-";
			rez += m_file_sdc.canRead() ? "r" : "-";
			rez += m_file_sdc.canWrite() ? "w" : "-";
			rez += m_file_sdc.isHidden() ? "h" : "-";
		}
		if (m_file_ftp != null) {
			rez += m_file_ftp.isDirectory() ? "d" : "-";
			rez += m_file_ftp.hasPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION) ? "r" : "-";
			rez += m_file_ftp.hasPermission(FTPFile.USER_ACCESS, FTPFile.WRITE_PERMISSION) ? "w" : "-";
			rez += m_file_ftp.isSymbolicLink() ? "l" : "-";
		}
		// if (m_file_smb != null) {
		// try {
		// rez += m_file_smb.isDirectory() ? "d" : "-";
		// rez += m_file_smb.canRead() ? "r" : "-";
		// rez += m_file_smb.canWrite() ? "w" : "-";
		// rez += m_file_smb.isHidden() ? "h" : "-";
		// } catch (SmbException e) {
		// }
		// }

		return rez;
	}

	public void setIcon(Drawable ico) {
		this.m_icon = ico;
	}

	@SuppressWarnings("unchecked")
	public <T> T getFile() {
		if (m_file_sdc != null)
			return (T) m_file_sdc;
		if (m_file_ftp != null)
			return (T) m_file_ftp;
		if (m_file_smb != null)
			return (T) m_file_smb;
		return null;
	}

	public void setChecked(boolean chck) {
		this.m_Checked = chck;
	}

	public boolean isChecked() {
		return m_Checked;
	}

	public Drawable getIconCheck() {
		return m_Checked ? Sets.I_CHK : Sets.I_UNCHK;
	}

	// public void setFileRow(FileRow fileRow) {
	// m_file_row = fileRow;
	// }

}
