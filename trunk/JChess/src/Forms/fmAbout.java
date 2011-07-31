package Forms;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class fmAbout extends JDialog {
	private static final long serialVersionUID = 1L;
	private Container cont;
	private Image img;
	private JFrame Parent;
	private JScrollPane Scrol;
	private JTextArea tbLog;
	private JButton btOk;

	public fmAbout(JFrame Prn) {
		this.Parent = Prn;
		initComponents();
	}

	private void initComponents() {
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setTitle("About " + Parent.getTitle());
		this.setResizable(false);
		this.setModal(true);
		this.setSize(450, 300);
		this.setLocation(getLocat());
		this.setIconImage(Tools.mainImg);
		// ---------------------------------------------------------------------------------
		cont = new Container();
		this.setContentPane(cont);
		// ---------------------------------------------------------------------------------
		img = Tools.welcomeImg;
		// ---------------------------------------------------------------------------------
		tbLog = new JTextArea();
		tbLog.setFont(new java.awt.Font("Courier New", 0, 12));
		tbLog.setLineWrap(true);
		tbLog.setWrapStyleWord(true);
		tbLog.setEditable(false);
		tbLog.setText(this.getText());
		tbLog.setCaretPosition(1);
		Scrol = new JScrollPane();
		Scrol.setSize(280, 150);
		Scrol.setLocation(150, 90);
		Scrol.setViewportView(tbLog);
		cont.add(Scrol);
		// ---------------------------------------------------------------------------------
		btOk = new JButton("OK");
		btOk.setSize(80, 30);
		btOk.setLocation(350, 240);
		btOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getThis().dispose();
			}
		});
		cont.add(btOk);
	}

	private String getText() {
		String Str = "Программа шахматы\r\n";
		Str += "  Все вопросы и пожелания направлять по адресу: paradoxfm@mail.ru\r\n";
		Str += "  Написана для ознакомления с возможностями ООП на Java, а так же визуальными возможностями\r\n";
		Str += "  Интерфейс полностью построен на компонентов Swing\r\n";
		Str += "  Среда разработки IDE Eclipse - http://www.eclipse.org/\r\n";
		return Str;
	}

	private Point getLocat() {
		int x = Parent.getLocation().x + (Parent.getSize().width - this.getSize().width) / 2;
		int y = Parent.getLocation().y + (Parent.getSize().height - this.getSize().height) / 2;
		return new Point(x, y);
	}

	private fmAbout getThis() {
		return this;
	}

	@Override
	public void paint(Graphics gr) {
		super.paint(gr);
		gr.drawImage(img, 10, 30, this);
	}

	@Override
	public void repaint() {
		super.repaint();
		Graphics gr = this.getGraphics();
		gr.drawImage(img, 10, 30, this);
	}
}
