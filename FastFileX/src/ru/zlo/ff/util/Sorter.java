package ru.zlo.ff.util;

import java.io.File;
import java.util.Comparator;

public final class Sorter {
	public static final boolean REVERCE = false;

	public static final String NAME = "NAME";
	public static final String DATE = "DATE";
	public static final String SIZE = "SIZE";

	public static Comparator<?> get(Object obj, String type) {
		Comparator<?> rez = null;
		if (obj.getClass() == File.class) {
			if (type == NAME)
				rez = new CompFileName();
			if (type == DATE)
				rez = new CompFileDate();
			if (type == SIZE)
				rez = new CompFileSize();
		}
		return rez;
	}
}

class CompFileName implements Comparator<File> {
	@Override
	public int compare(File obj1, File obj2) {
		int rez = obj1.getName().toLowerCase().compareTo(obj2.getName().toLowerCase());
		if (Sorter.REVERCE)
			rez *= -1;
		return rez;
	}
}

abstract class CompareNum implements Comparator<File> {
	protected int calc(long dat1, long dat2) {
		int rez = 0;
		if (dat1 < dat2)
			rez = -1;
		if (dat1 > dat2)
			rez = 1;
		if (Sorter.REVERCE)
			rez *= -1;
		return rez;
	}
}

class CompFileDate extends CompareNum {
	@Override
	public int compare(File obj1, File obj2) {
		return calc(obj1.lastModified(), obj2.lastModified());
	}
}

class CompFileSize extends CompareNum {
	@Override
	public int compare(File obj1, File obj2) {
		return calc(obj1.length(), obj2.length());
	}
}
