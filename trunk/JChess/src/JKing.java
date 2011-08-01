

import android.content.Context;
import ru.megazlo.jbtchess.board.JCheck;
import ru.megazlo.jbtchess.board.pieces.Type;

public class JKing extends JPiece {
	private static final long serialVersionUID = 1L;
	public int[][] Pos;

	public JKing(Context c, Type TP, Col Cl, JCheck Chek) {
		super(c, TP, Cl, Chek);
		Pos = new int[][] { { 1, 0, 1, -1, -1, 0, -1, 1 }, { 0, 1, 1, 1, 0, -1, -1, -1 } };
	}

	@Override
	public void createMotion() {
		super.createMotion();
		for (int ii = 0; ii < 8; ii++)
			MoveKill(Coo.x + Pos[0][ii], Coo.y + Pos[1][ii]);
	}

	private void MoveKill(int y, int x) {
		if (x > -1 && x < 8 && y > -1 && y < 8) {
			// if (Brd.Checks[x][y].getComponentCount() == 0)
			// Brd.Checks[x][y].setDragDrop(true);
			// if (Brd.Checks[x][y].getComponentCount() > 0) {
			// JPiece Ph = (JPiece) Brd.Checks[x][y].getComponent(0);
			// if (Ph.Color != this.Color)
			// Brd.Checks[x][y].setDragDrop(true);
			// }
		}
	}
}
