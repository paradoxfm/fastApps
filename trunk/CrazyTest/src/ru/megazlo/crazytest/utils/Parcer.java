package ru.megazlo.crazytest.utils;

import java.util.Date;

import ru.megazlo.crazytest.R;

import android.content.Context;

public final class Parcer {
	public static TimeSpan parce(Date f) {
		Date n = new Date();
		TimeSpan rez = new TimeSpan();
		rez.year = getYear(n, f);
		rez.month = getMonth(n, f);
		rez.day = getDay(n, f);
		rez.hour = getHour(n, f);
		rez.minutes = getMinutes(n, f);
		return rez;
	}

	private static int getMinutes(Date n, Date f) {
		int min = n.getMinutes() - f.getMinutes();
		if (n.getMinutes() < f.getMinutes())
			min += 60;
		else if (n.getSeconds() < f.getSeconds())
			min--;
		return min;
	}

	private static int getHour(Date n, Date f) {
		int hr = n.getHours() - f.getHours();
		if (n.getHours() < f.getHours())
			hr += 24;
		if (n.getMinutes() < f.getMinutes())
			hr--;
		else if (n.getMinutes() == f.getMinutes() && n.getSeconds() < f.getSeconds()) {
			hr--;
		}
		return hr;
	}

	private static int getDay(Date n, Date f) {
		int day = n.getDate() - f.getDate();
		if (n.getDate() < f.getDate())
			day += 30;
		if (n.getHours() < f.getHours()) {
			day--;
		} else if (n.getHours() == f.getHours()) {
			if (n.getMinutes() < f.getMinutes())
				day--;
			else if (n.getMinutes() == f.getMinutes() && n.getSeconds() < f.getSeconds()) {
				day--;
			}
		}
		return day;
	}

	private static int getMonth(Date n, Date f) {
		int mon = n.getMonth() - f.getMonth();
		if (n.getMonth() < f.getMonth())
			mon += 12;
		if (n.getDate() < f.getDate()) {
			mon--;
		} else if (n.getDate() == f.getDate()) {
			if (n.getHours() < f.getHours()) {
				mon--;
			} else if (n.getHours() == f.getHours()) {
				if (n.getMinutes() < f.getMinutes())
					mon--;
				else if (n.getMinutes() == f.getMinutes() && n.getSeconds() < f.getSeconds()) {
					mon--;
				}
			}
		}
		return mon;
	}

	private static int getYear(Date n, Date f) {
		int year = 0;
		if (n.getYear() > f.getYear()) {
			year = n.getYear() - f.getYear();
			if (n.getMonth() < f.getMonth())
				year--;
			else if (n.getMonth() == f.getMonth()) {
				if (n.getDate() < f.getDate()) {
					year--;
				} else if (n.getDate() == f.getDate()) {
					if (n.getHours() < f.getHours()) {
						year--;
					} else if (n.getHours() == f.getHours()) {
						if (n.getMinutes() < f.getMinutes()) {
							year--;
						} else if (n.getMinutes() == f.getMinutes() && n.getSeconds() < f.getSeconds()) {
							year--;
						}
					}
				}
			}
		}
		return year;
	}

	public static String createMessage(TimeSpan set, Context c) {
		String rez = c.getResources().getString(R.string.after) + "\n";
		if (set.year != 0)
			rez += set.year + getFormattedStr(c, R.string.lb_yr, getTypeLast(set.year)) + " ";
		if (set.month != 0)
			rez += set.month + getFormattedStr(c, R.string.lb_mn, getTypeLast(set.month)) + " ";
		if (set.day != 0)
			rez += set.day + getFormattedStr(c, R.string.lb_dy, getTypeLast(set.day)) + " ";
		if (set.hour != 0)
			rez += set.hour + getFormattedStr(c, R.string.lb_hr, getTypeLast(set.hour)) + " ";
		if (set.minutes != 0)
			rez += set.minutes + getFormattedStr(c, R.string.lb_mi, getTypeLast(set.minutes));
		return rez;
	}

	private static String getFormattedStr(Context c, int res, int typ) {
		String resstr = c.getResources().getString(res);
		return resstr.split(";")[typ];
	}

	private static int getTypeLast(int num) {
		String str = "" + num;
		Character chr = str.charAt(str.length() - 1);
		int ln = Integer.parseInt(chr + "");
		if (ln == 1)
			return 0;
		else if (ln > 1 && ln < 5)
			return 1;
		return 2;
	}
}
