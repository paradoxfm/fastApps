package ru.megazlo.ledxremote.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import ru.megazlo.ledxremote.Main;
import ru.megazlo.ledxremote.R;
import ru.megazlo.ledxremote.enums.Cmd;
import ru.megazlo.ledxremote.enums.FType;
import ru.megazlo.ledxremote.enums.Sys;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

/** Вспомогательные утилиты */
public abstract class Util {

	public static byte CONTROLLER = 1;

	public static byte CheckSum(ArrayList<Byte> arr) {
		byte rez = 0;
		for (int i = 1; i < arr.size(); i++)
			rez += arr.get(i) & 0xFF;
		return rez;
	}

	private static InetAddress getBroadcastAddress(Context c) throws IOException {
		WifiManager wifi = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow
		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	public static void sendPackage(byte[] data) {
		// UDP порт: 5378 IP адрес 192.168.1.2
		if (checkErrors())
			return;
		int PORT = 5378;
		InetAddress adr;
		try {
			if (Sets.BY_IP)
				adr = InetAddress.getByAddress(new byte[] { (byte) 192, (byte) 168, 1, 2 });
			else
				adr = getBroadcastAddress(Main.getInst());
			DatagramSocket socket = new DatagramSocket(PORT);
			socket.setBroadcast(true);
			DatagramPacket packet = new DatagramPacket(data, data.length, adr, PORT);
			socket.send(packet);
			socket.close();
		} catch (IOException e) {
		}
	}

	public static boolean checkErrors() {
		if (CONTROLLER == 0)
			return true;
		ConnectivityManager manager = (ConnectivityManager) Main.getInst().getSystemService(Main.CONNECTIVITY_SERVICE);
		Boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		if (!isWifi) {
			Main.getInst().showAlert(R.string.err_wifi);
			return true;
		}

		return false;
	}

	private static ArrayList<Byte> setup(byte cmd, int len) {
		ArrayList<Byte> lst = new ArrayList<Byte>();
		lst.add(Sys.Start);// стартовый байт
		lst.add(FType.Request);// тип фрейма
		lst.add(CONTROLLER);// номер контроллера
		lst.add(cmd);// команда
		lst.add((byte) len);// количество данных
		return lst;
	}

	public static byte[] parceIP(String adr) {
		byte[] rez = new byte[4];
		String test = adr.replace("/", "").replace('.', ';');
		String[] arr = test.split(";");
		for (int i = 0; i < arr.length; i++)
			rez[i] = (byte) Integer.parseInt(arr[i]);
		return rez;
	}

	private static void setupEnd(ArrayList<Byte> lst) {
		lst.add(CheckSum(lst)); // контрльная сумма
		lst.add(Sys.Stop);// стоповый байт
		byte[] rez = new byte[lst.size()];
		for (int i = 0; i < rez.length; i++)
			rez[i] = lst.get(i);
		sendPackage(rez);
	}

	public static void sendProgram(int val) {
		sendSPBRPR(Cmd.RUN_PROG, val);
	}

	public static void sendSpeed(int val) {
		sendSPBRPR(Cmd.CHANGE_SP, val);
	}

	public static void sendBrightness(int val) {
		sendSPBRPR(Cmd.CHANGE_BR, val);
	}

	private static void sendSPBRPR(byte cmd, int val) {
		ArrayList<Byte> lst = setup(cmd, 1);
		lst.add((byte) val);
		setupEnd(lst);
	}

	public static void sendColor(int cl) {
		ArrayList<Byte> lst = setup(Cmd.SET_COLOR, 3);
		lst.add((byte) Color.red(cl));
		lst.add((byte) Color.green(cl));
		lst.add((byte) Color.blue(cl));
		setupEnd(lst);
	}

	public static void swapEnable() {
		ArrayList<Byte> lst = setup(Cmd.ONOFFCOMM, 0);
		setupEnd(lst);
	}

	public static void swapPlayPause() {
		ArrayList<Byte> lst = setup(Cmd.PAUSE_TOGGLE, 0);
		setupEnd(lst);
	}

}
