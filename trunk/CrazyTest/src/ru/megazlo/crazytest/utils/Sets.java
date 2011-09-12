package ru.megazlo.crazytest.utils;

import android.content.Context;
import android.text.format.DateFormat;

public class Sets {

	public static java.text.DateFormat F_TIME;
	public static java.text.DateFormat F_DATE;

	public static void load(Context c) {
		F_DATE = DateFormat.getDateFormat(c);
		F_TIME = DateFormat.getTimeFormat(c);
	}

	public static void save(Context c) {

	}

}
