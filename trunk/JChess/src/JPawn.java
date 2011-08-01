

import android.content.Context;
import android.graphics.Point;

import ru.megazlo.jbtchess.board.JCheck;
import ru.megazlo.jbtchess.board.pieces.Type;

public class JPawn extends JPiece {
	private static final long serialVersionUID = 1L;
	int Hod = 1;
	Point Start;

	public JPawn(Context c, Type pawn, Col white, Point Start, JCheck Par) {
		super(c, pawn, white, Par);
		if (this.Color == Col.Black)
			this.Hod *= -1;
		this.Start = Start;
	}

	@Override
	public void createMotion() {
		super.createMotion();
		// if (Coo.y == 7 || Coo.y == 0)
		// JOptionPane.showMessageDialog(null, "������ �����!");
		if (!isTake(Coo.x, Coo.y + Hod)) {
			PwnMove(Coo.x, Coo.y + Hod);
			if (Start.y == Coo.y && !isTake(Coo.x, Coo.y + Hod * 2))
				PwnMove(Coo.x, Coo.y + Hod * 2);
		}
		Kill(Coo.x + 1, Coo.y + Hod);
		Kill(Coo.x - 1, Coo.y + Hod);
	}

	private void PwnMove(int x, int y) {
		// if (x > -1 && x < 8 && y > -1 && y < 8)
		// if (this.Brd.Checks[y][x].getComponentCount() == 0)
		// this.Brd.Checks[y][x].setDragDrop(true);
	}

	private void Kill(int x, int y) {
		// if (x > -1 && x < 8 && y > -1 && y < 8)
		// if (this.Brd.Checks[y][x].getComponentCount() > 0) {
		// JPiece Ph = (JPiece) Brd.Checks[y][x].getComponent(0);
		// if (Ph.Color != this.Color)
		// this.Brd.Checks[y][x].setDragDrop(true);
		// }
	}

	private boolean isTake(int y, int x) {
		// if (Brd.Checks[x][y].getComponentCount() > 0)
		// return true;
		return false;
	}
}
