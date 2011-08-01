

import android.content.Context;
import ru.megazlo.jbtchess.board.JCheck;
import ru.megazlo.jbtchess.board.pieces.Type;

public class JRook extends JQueen {

	public JRook(Context c, Type TP, Col Cl, JCheck Chek) {
		super(c, TP, Cl, Chek);
	}

	@Override
	public void MDiagonal() {
		return;
	}
}
