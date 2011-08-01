

import android.content.Context;
import ru.megazlo.jbtchess.board.JCheck;
import ru.megazlo.jbtchess.board.pieces.Type;

public class JQueen extends JPiece {
	private static final long serialVersionUID = 1L;

	public JQueen(Context c, Type TP, Col Cl, JCheck Chek) {
		super(c, TP, Cl, Chek);
	}

	@Override
	public void createMotion() {
		super.createMotion();
		MParallel();
		MDiagonal();
	}

	private boolean MoveKill(int y, int x) {
		if (x < 0 || x > 7 || y < 0 || y > 7)
			return true;
		// if (Brd.Checks[x][y].getComponentCount() == 0) {
		// //Brd.Checks[x][y].setDragDrop(true);
		// return false;
		// } else if (Brd.Checks[x][y].getComponentCount() > 0) {
		// JPiece Ph = (JPiece) Brd.Checks[x][y].getComponent(0);
		// if (Ph.Color == this.Color)
		// return true;
		// //Brd.Checks[x][y].setDragDrop(true);
		// return true;
		// } else
		return true;
	}

	public void MDiagonal() {
		int x, y;
		for (x = 1, y = 1;; x++, y++)
			if (MoveKill(Coo.x + x, Coo.y + y))
				break;
		for (x = 1, y = 1;; x++, y++)
			if (MoveKill(Coo.x - x, Coo.y - y))
				break;
		for (x = 1, y = 1;; x++, y++)
			if (MoveKill(Coo.x - x, Coo.y + y))
				break;
		for (x = 1, y = 1;; x++, y++)
			if (MoveKill(Coo.x + x, Coo.y - y))
				break;
	}

	public void MParallel() {
		int xy;
		for (xy = 1;; xy++)
			if (MoveKill(Coo.x + xy, Coo.y))
				break;
		for (xy = 1;; xy++)
			if (MoveKill(Coo.x - xy, Coo.y))
				break;
		for (xy = 1;; xy++)
			if (MoveKill(Coo.x, Coo.y + xy))
				break;
		for (xy = 1;; xy++)
			if (MoveKill(Coo.x, Coo.y - xy))
				break;
	}

}
