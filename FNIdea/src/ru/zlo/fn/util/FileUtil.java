package ru.zlo.fn.util;

import android.content.Intent;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;

public class FileUtil {
	public static String file_text;
	private static File file;

	public static String loadFromFile() {
		String rez = "";
		try {
			rez = read(file);
		} catch (IOException ignored) {
		}
		return rez;
	}

	public static boolean backupBase(String path) {

		return false;
	}

	private static String read(File file) throws IOException {
		String encoding = detectEncode(file);
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, encoding);
		BufferedReader rd = new BufferedReader(isr);
		String s;
		StringBuilder buf = new StringBuilder();
		while ((s = rd.readLine()) != null)
			buf.append(s).append("\r\n");
		rd.close();
		isr.close();
		fis.close();
		return buf.toString();
	}

	private static String detectEncode(File file) throws IOException {
		byte[] buf = new byte[4096];
		FileInputStream fis = new FileInputStream(file);
		UniversalDetector detector = new UniversalDetector(null);
		int nread;
		while ((nread = fis.read(buf)) > 0 && !detector.isDone())
			detector.handleData(buf, 0, nread);
		fis.close();
		detector.dataEnd();
		String encoding = detector.getDetectedCharset();
		detector.reset();
		return encoding != null ? encoding : "UTF-8";
	}

	public static boolean openText(Intent intent) {
		String path = intent.getData() != null ? intent.getData().getPath() : null;
		intent.setData(null);
		if (path != null) {
			file = new File(path);
			file_text = FileUtil.loadFromFile();
		}
		return path != null;
	}

	public static String getFileName() {
		return file.getName();
	}
}
