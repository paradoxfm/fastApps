

import android.content.Context;
import ru.megazlo.jbtchess.board.JCheck;
import ru.megazlo.jbtchess.board.pieces.Type;

public class JBishop extends JQueen {
	private static final long serialVersionUID = 1L;

	public JBishop(Context c, Type TP, Col Cl, JCheck Chek) {
		super(c, TP, Cl, Chek);
	}

	@Override
	public void MParallel() {
		return;
	}
}
