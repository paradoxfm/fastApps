package ru.megazlo.ledxremote.util;

import java.net.InetAddress;

import android.content.SharedPreferences;

public abstract class Sets {

	public static final int SIZE = 5;
	public static boolean BY_IP;
	public static int PORT;
	public static int[] CLS = new int[SIZE];
	private static int[] CLD = new int[] { -58624, -16400384, -13106962, -1536, -6752513 };
	public static String CONTROL;
	public static InetAddress IP_ADR;

	public static void load(SharedPreferences prf) {
		for (int i = 0; i < SIZE; i++)
			CLS[i] = prf.getInt("CL_" + i, CLD[i]);
		BY_IP = prf.getBoolean("BY_IP", false);
		PORT = prf.getInt("PORT", 5378);
		try {
			String nm = prf.getString("IP_ADR", "192.168.1.2");
			IP_ADR = InetAddress.getByAddress(Util.parceIP(nm));
		} catch (Exception e) {
		}
		CONTROL = prf.getString("CONTROL", "Бар,Стойка,Кафе,Танцпол");
	}

	public static void save(SharedPreferences prf) {
		SharedPreferences.Editor edt = prf.edit();
		for (int i = 0; i < SIZE; i++)
			edt.putInt("CL_" + i, CLS[i]);
		edt.putBoolean("BY_IP", BY_IP);
		edt.putInt("PORT", PORT);
		String str = IP_ADR.toString();
		edt.putString("IP_ADR", str);
		edt.putString("CONTROL", CONTROL);
		edt.commit();
	}
}
