package Forms;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.ImageIcon;

import Board.Pieces.Col;
import Board.Pieces.Tip;

public class Tools {
	public static Hashtable<String, ImageIcon> Imgs = new Hashtable<String, ImageIcon>();
	public static Image mainImg;
	public static Image welcomeImg;
	public static XmlMenuLoader loader;
	public static Settings Set;

	public static void setStyle(fmMain mForm) {
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			javax.swing.SwingUtilities.updateComponentTreeUI(mForm);
		} catch (Exception e) {}
	}

	public static void loadHelpers() {
		loadIcon();
		loadPiecess();
		loadMenu();
		loadSettings();
	}

	private static void loadMenu() {
		Object obj = new Object();
		try {
			InputStream stream = obj.getClass().getResourceAsStream("/Resours/Xml/menu.xml");
			loader = new XmlMenuLoader(stream);
			loader.parse();
		} catch (Exception e) {}
	}

	private static void loadIcon() {
		Object obj = new Object();
		URL resource = obj.getClass().getResource("/Resours/Other/JChess.png");
		mainImg = Toolkit.getDefaultToolkit().getImage(resource);
		resource = obj.getClass().getResource("/Resours/Other/welcome.jpg");
		welcomeImg = Toolkit.getDefaultToolkit().getImage(resource);
	}

	private static void loadPiecess() {
		Object Res = new Object();
		Tip[] TP = Tip.values();
		Col[] CL = Col.values();
		String[] Names = new String[TP.length];
		String[] Clrs = new String[CL.length];
		for (int ii = 0; ii < TP.length; ii++)
			Names[ii] = TP[ii].toString();
		for (int ii = 0; ii < CL.length; ii++)
			Clrs[ii] = CL[ii].toString();
		for (int ii = 0; ii < Clrs.length; ii++)
			for (int yy = 0; yy < TP.length; yy++) {
				String Path = "/Resours/Pieces/" + Clrs[ii] + Names[yy] + ".png";
				ImageIcon img = new ImageIcon(Res.getClass().getResource(Path));
				Imgs.put(Clrs[ii] + Names[yy], img);
			}
	}

	private static void loadSettings() {
		Set = new Settings();
	}

	public static void saveSettings() {

	}
}
