

import android.content.Context;
import ru.megazlo.jbtchess.board.JCheck;
import ru.megazlo.jbtchess.board.pieces.Type;

public class JKnight extends JKing {
	private static final long serialVersionUID = 1L;

	public JKnight(Context c, Type TP, Col Cl, JCheck Chek) {
		super(c, TP, Cl, Chek);
		super.Pos = new int[][] { { -1, 1, -1, 1, 2, 2, -2, -2 }, { 2, 2, -2, -2, -1, 1, -1, 1 } };
	}

	@Override
	public void createMotion() {
		super.createMotion();
	}
}
