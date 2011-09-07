package ru.megazlo.fastnote.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.mozilla.universalchardet.UniversalDetector;

import android.content.Intent;

public class FileUtil {
	public static String file_text;
	private static File file;
	private static String encoding;

	public static String loadFromFile() {
		String rez = "";
		try {
			rez = read(file);
		} catch (IOException e) {
		}
		return rez;
	}

	private static String read(File file) throws IOException {
		encoding = detectEncode(file);
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, encoding);
		BufferedReader rd = new BufferedReader(isr);
		String s = new String();
		StringBuffer buf = new StringBuffer();
		while ((s = rd.readLine()) != null) {
			buf.append(s + "\r\n");
		}
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
		while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, nread);
		}
		detector.dataEnd();
		String encoding = detector.getDetectedCharset();
		detector.reset();
		return encoding != null ? encoding : "UTF-8";
	}

	public static void saveToFile(String text) {
		try {
			Writer out = new OutputStreamWriter(new FileOutputStream(file), encoding);
			out.write(text.toString());
			out.close();
		} catch (Exception e) {
		}
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
