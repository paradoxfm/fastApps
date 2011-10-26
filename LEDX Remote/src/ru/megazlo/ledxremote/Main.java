package ru.megazlo.ledxremote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import ru.megazlo.colorpicker.ColorCircle;
import ru.megazlo.colorpicker.ColorSlider;
import ru.megazlo.colorpicker.OnColorChangedListener;
import ru.megazlo.ledxremote.util.Util;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class Main extends Activity implements OnColorChangedListener, View.OnClickListener {
	private int clor = 0xfcca04;
	private ColorCircle mColorCircle;
	private ColorSlider mSaturation, mValue;
	private AlertDialog dial;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		initEvents();
	}

	private InetAddress getBroadcastAddress() throws IOException {
		WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	// DatagramSocket socket;

	private void sendPackage(byte[] data) throws IOException {
		// UDP порт: 5378
		// IP адрес 192.168.1.2
		int PORT = 5378;
		// InetAddress adr = InetAddress.getByAddress(new byte[] { (byte) 192,
		// (byte) 168, 1, 2 });
		InetAddress adr = getBroadcastAddress();

		DatagramSocket socket = new DatagramSocket(PORT);
		socket.setBroadcast(true);

		DatagramPacket packet = new DatagramPacket(data, data.length, adr, PORT);
		socket.send(packet);
		socket.close();
	}

	private void initializeColor(View v, int color) {
		mColorCircle = (ColorCircle) v.findViewById(R.id.colorcircle);
		mColorCircle.setOnColorChangedListener(this);
		mColorCircle.setColor(color);
		mSaturation = (ColorSlider) v.findViewById(R.id.saturation);
		mSaturation.setOnColorChangedListener(this);
		mSaturation.setColors(color, Color.BLACK);
		mValue = (ColorSlider) v.findViewById(R.id.value);
		mValue.setOnColorChangedListener(this);
		mValue.setColors(Color.WHITE, color);
	}

	private void initEvents() {
		findViewById(R.id.btProgOne).setOnClickListener(this);
		findViewById(R.id.btProgTwo).setOnClickListener(this);
		findViewById(R.id.btProgThree).setOnClickListener(this);
		findViewById(R.id.btProgFour).setOnClickListener(this);
		findViewById(R.id.btProgFive).setOnClickListener(this);

		findViewById(R.id.imgEnable).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView im = (ImageView) v;
				if (im.getTag().equals("on")) {
					im.setImageResource(R.drawable.shutdown);
					im.setTag("off");
				} else {
					im.setImageResource(R.drawable.standby);
					im.setTag("on");
				}

			}
		});
		findViewById(R.id.btCustomColor).setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				final View formcon = LayoutInflater.from(Main.this).inflate(R.layout.color_dial, null);
				initializeColor(formcon, clor);
				dial = new AlertDialog.Builder(Main.this).setView(formcon).create();
				dial.show();
				return true;
			}
		});
	}

	private void setProgram(byte id) {
		byte[] data = new byte[8];
		data[0] = (byte) 170;
		data[1] = 82;
		data[2] = 1;
		data[3] = 2;
		data[4] = 1;
		data[5] = id;
		data[6] = (byte) (86 + id);
		data[7] = 85;
		byte sum = Util.CheckSum(data);
		try {
			sendPackage(data);
		} catch (IOException e) {
		}
	}

	@Override
	public void onColorChanged(View view, int newColor) {
		if (view == mColorCircle) {
			mValue.setColors(0xFFFFFFFF, newColor);
			mSaturation.setColors(newColor, 0xff000000);
		} else if (view == mSaturation) {
			mColorCircle.setColor(newColor);
			mValue.setColors(0xFFFFFFFF, newColor);
		} else if (view == mValue) {
			mColorCircle.setColor(newColor);
		}
	}

	@Override
	public void onColorPicked(View view, int newColor) {
		findViewById(R.id.btCustomColor).setBackgroundColor(newColor);
		clor = newColor;
		dial.dismiss();
	}

	@Override
	public void onClick(View v) {
		byte prg = Byte.parseByte(((Button) v).getText().toString());
		setProgram(prg);
	}
}