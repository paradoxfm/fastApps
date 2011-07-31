package Forms;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

public class fmSettings extends JDialog {
	private static final long serialVersionUID = 1L;
	private Container cont;
	private JButton btOk;

	public fmSettings() {
		this.initComponents();
	}

	private void initComponents() {
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
		this.setIconImage(Tools.mainImg);
		this.setTitle("Настройки");
		// ---------------------------------------------------------------------------------
		cont = new Container();
		this.setContentPane(cont);
		// ---------------------------------------------------------------------------------
		btOk = new JButton("OK");
		btOk.setSize(80, 30);
		btOk.setLocation(200, 200);
		btOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Tools.saveSettings();
			}
		});
		cont.add(btOk);
	}
}
