package Forms;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

import Board.Events.BoardEvent;
import Board.Events.BoardListener;

public class fmMain extends JFrame {
	private static final long serialVersionUID = 1L;
	private String Title = "JChess";
	private Container cont;
	private Board.JChessBoard Brd;
	private JLabel lbtm;
	private JProgressBar PrgW, PrgB;
	private SystemTray systemTray;
	private TrayIcon trayIcon;
	private Timer tmMove;
	private int TotalSec = 0;
	private Date Dat = new Date();
	private String formatString = "mm:ss";
	private SimpleDateFormat sdf = new SimpleDateFormat(formatString);

	public fmMain() {
		this.initComponents();
	}

	private void initComponents() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(Tools.mainImg);
		// this.setResizable(false);
		this.setTitle(Title);
		this.setSize(Tools.Set.getWidth(), Tools.Set.getHeight());
		Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((sSize.width - Tools.Set.getWidth()) / 2, (sSize.height - Tools.Set.getHeight()) / 2);

		this.addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
				if (e.getNewState() == JFrame.ICONIFIED)
					showTrayIcon();
			}
		});
		// ---------------------------------------------------------------------------------
		this.cont = new Container();
		this.setContentPane(cont);
		// ---------------------------------------------------------------------------------
		this.loadMenu();
		// ---------------------------------------------------------------------------------
		this.add(initStaus());
		// ---------------------------------------------------------------------------------
		this.Brd = new Board.JChessBoard();
		this.Brd.addBoardListener(new BoardListener() {
			@Override
			public void movePiece(BoardEvent ev) {
				PrgW.setValue(Brd.getWhiteCount());
				PrgB.setValue(Brd.getBlackCount());
				TotalSec = 0;
			}

			@Override
			public void endDragPiece(BoardEvent ev) {
			}

			@Override
			public void startDragPiece(BoardEvent ev) {
			}
		});
		this.Brd.setLocation(30, 10);
		this.cont.add(Brd);
		// ---------------------------------------------------------------------------------
		this.initTrayIcon();
		// ---------------------------------------------------------------------------------
		this.setLabelsNum();
		this.setLabelsLet();
		// ---------------------------------------------------------------------------------
		tmMove = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dat.setTime(TotalSec * 1000);
				TotalSec++;
				lbtm.setText(sdf.format(Dat));
			}
		});
		tmMove.start();
	}

	private JFrame getThis() {
		return this;
	}

	private void initTrayIcon() {
		this.systemTray = SystemTray.getSystemTray();
		this.trayIcon = new TrayIcon(Tools.mainImg, this.Title);
		trayIcon.setImageAutoSize(true);
		this.trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideTrayIcon();
			}
		});
	}

	private void showTrayIcon() {
		this.setVisible(false);
		try {
			systemTray.add(trayIcon);
		} catch (Exception ex) {}
	}

	private void hideTrayIcon() {
		this.setVisible(true);
		this.setState(JFrame.NORMAL);
		this.systemTray.remove(trayIcon);
	}

	private JPanel initStaus() {
		JPanel sts = new JPanel();
		sts.setBorder(new BevelBorder(0));
		sts.setLayout(new FlowLayout());
		sts.setSize(610, 35);
		sts.setLocation(0, 605);
		lbtm = new JLabel(formatString);
		JLabel lb0 = new JLabel("Время хода: ");
		JLabel lbmv = new JLabel("Ходят: ");
		PrgW = getProgress("Белых");
		PrgB = getProgress("Черных");
		sts.add(PrgW);
		sts.add(PrgB);
		sts.add(lbmv);
		sts.add(lb0);
		sts.add(lbtm);
		return sts;
	}

	private JProgressBar getProgress(String Colo) {
		JProgressBar Prg = new JProgressBar(0, 16);
		Prg.setValue(16);
		Prg.setString("Количество " + Colo);
		Prg.setStringPainted(true);
		return Prg;
	}

	private void setLabelsNum() {
		for (int ii = 0; ii < 8; ii++) {
			JLabel Lb = new JLabel(Integer.toString(Math.abs(ii - 8)));
			Lb.setLocation(10, ii * 69 + 40);
			Lb.setSize(20, 20);
			Lb.setFont(new Font("Courier New", Font.PLAIN, 20));
			cont.add(Lb);
		}
	}

	private void setLabelsLet() {
		for (int ii = 0; ii < 8; ii++) {
			JLabel Lb = new JLabel((char) (ii + 65) + "");
			Lb.setLocation(ii * 69 + 60, 575);
			Lb.setSize(20, 20);
			Lb.setFont(new Font("Courier New", Font.PLAIN, 20));
			cont.add(Lb);
		}
	}

	private void loadMenu() {
		this.setJMenuBar(Tools.loader.getMenuBar("mainMenu"));
		Tools.loader.addActionListener("exit", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		Tools.loader.addActionListener("new", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Brd.resetRound();
				TotalSec = 0;
			}
		});
		Tools.loader.addActionListener("set", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fmSettings Set = new fmSettings();
				Set.setVisible(true);
			}
		});
		Tools.loader.addActionListener("abt", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fmAbout abt = new fmAbout(getThis());
				abt.setVisible(true);
			}
		});
		Tools.loader.addActionListener("pause", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Brd.setPause(!Brd.getPauseState());
				if (tmMove.isRunning())
					tmMove.stop();
				else
					tmMove.start();
			}
		});
	}
}
