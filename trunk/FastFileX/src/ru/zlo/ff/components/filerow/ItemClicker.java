package ru.zlo.ff.components.filerow;

import java.io.File;

import jcifs.smb.SmbFile;
import ru.megazlo.ftplib.ftp.FTPFile;
import ru.zlo.ff.engine.BaseEngine;
import ru.zlo.ff.util.Sets;
import ru.zlo.ff.util.file.FileTools;

public abstract class ItemClicker {

	public static void click(BaseEngine eng, int pos) {
		switch (eng.getType()) {
		case BaseEngine.SDC:
			localClick(eng, pos);
			break;
		case BaseEngine.FTP:
			ftpClick(eng, pos);
			break;
		case BaseEngine.LAN:
			lanClick(eng, pos);
			break;
		}
	}

	private static void localClick(BaseEngine eng, int pos) {
		File curFile = eng.getDat().dir.get(pos).getFile();
		if (curFile.isDirectory())
			eng.browseCatalog(curFile);
		else if (Sets.OPEN_THIS)
			FileTools.openFileThis(eng.getList().getContext(), curFile);
		else if (!Sets.OPEN_THIS)
			FileTools.openFileExt(eng.getList().getContext(), curFile);
	}

	private static void ftpClick(BaseEngine eng, int pos) {
		FTPFile curFile = eng.getDat().dir.get(pos).getFile();
		if (curFile.isDirectory() || curFile.isSymbolicLink())
			eng.browseCatalog(curFile);
	}

	private static void lanClick(BaseEngine eng, int pos) {
		try {
			SmbFile curFile = eng.getDat().dir.get(pos).getFile();
			if (curFile.isDirectory())
				eng.browseCatalog(eng.getDat().dir.get(pos).getFile());
		} catch (Exception e) {
		}
	}

}
