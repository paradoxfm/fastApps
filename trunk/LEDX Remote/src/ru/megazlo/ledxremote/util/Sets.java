package ru.megazlo.ledxremote.util;

import java.net.InetAddress;

import android.content.SharedPreferences;

public abstract class Sets {
	public static int CL_1;
	public static int CL_2;
	public static int CL_3;
	public static int CL_4;
	public static int CL_5;
	public static boolean BY_IP;
	public static int PORT;
	public static InetAddress IP_ADR;

	public static void load(SharedPreferences prf) {
		CL_1 = prf.getInt("CL_1", -58624);
		CL_2 = prf.getInt("CL_2", -16400384);
		CL_3 = prf.getInt("CL_3", -13106962);
		CL_4 = prf.getInt("CL_4", -1536);
		CL_5 = prf.getInt("CL_5", -6752513);
		BY_IP = prf.getBoolean("BY_IP", false);
		PORT = prf.getInt("PORT", 5378);
		try {
			String nm = prf.getString("IP_ADR", "192.168.1.2");
			IP_ADR = InetAddress.getByAddress(Util.parceIP(nm));
		} catch (Exception e) {
		}
	}

	public static void save(SharedPreferences prf) {
		SharedPreferences.Editor edt = prf.edit();
		edt.putInt("CL_1", CL_1);
		edt.putInt("CL_2", CL_2);
		edt.putInt("CL_3", CL_3);
		edt.putInt("CL_4", CL_4);
		edt.putInt("CL_5", CL_5);

		edt.putBoolean("BY_IP", BY_IP);
		edt.putInt("PORT", PORT);
		String str = IP_ADR.toString();
		edt.putString("IP_ADR", str);

		edt.commit();
	}
}
