import Forms.Tools;
import Forms.fmMain;

public class Program {
	public static void main(String[] args) {
		Tools.loadHelpers();
		final fmMain Frm = new fmMain();
		Tools.setStyle(Frm);
		Frm.setVisible(true);
	}
}
