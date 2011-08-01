

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

public class JCheck extends ImageView {
	private static final long serialVersionUID = 1L;
	public JChessBoard Brd;
	Boolean PassedPawn = false;
	public int Size = 70;
	public int ThisColor;

	public JCheck(Context c, Boolean Col, JChessBoard Brd) {
		super(c);
		this.Brd = Brd;
		ThisColor = Col ? Color.BLACK : Color.WHITE;
		// this.setBackground(ThisColor);
		// this.addMouseListener(new MouseListener() {
		// @Override
		// public void mouseReleased(MouseEvent e) {
		// }
		//
		// @Override
		// public void mousePressed(MouseEvent e) {
		// }
		//
		// @Override
		// public void mouseExited(MouseEvent e) {
		// }
		//
		// @Override
		// public void mouseEntered(MouseEvent e) {
		// }
		//
		// @Override
		// public void mouseClicked(MouseEvent e) {
		// JCheck CH = getThis();
		// if (CH.Brd.Draget == null)
		// return;
		// if (CH.getBorder() != null) {
		// Component Cmp = CH.Brd.Draget.getParent();
		// CH.add(CH.Brd.Draget);
		// Cmp.repaint();
		// CH.Brd.swapMove();
		// CH.Brd.stopMotion();
		// CH.Brd.endDragPiece();
		// }
		// }
		// });
	}
}
