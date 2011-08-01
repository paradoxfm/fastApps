

import android.content.Context;
import android.graphics.Point;
import android.widget.ImageButton;
import ru.megazlo.jbtchess.board.JCheck;
import ru.megazlo.jbtchess.board.JChessBoard;
import ru.megazlo.jbtchess.board.pieces.Type;

//import Forms.Tools;

public abstract class JPiece extends ImageButton {
	private static final long serialVersionUID = 1L;
	public JChessBoard Brd;
	public Type Type;
	public Col Color;
	public Point Coo = new Point();

	public JPiece(Context c, Type Type, Col Color, JCheck Par) {
		super(c);
		this.Brd = Par.Brd;
		this.Type = Type;
		this.Color = Color;
		// Par.add(this);
		// this.setSize(Par.getSize());
		// this.setBackground(null);
		// this.addMouseListener(new PieceMouseListener());
		// this.setIcon(Tools.Imgs.get(Color.toString() + Type.toString()));
	}

	public void createMotion() {
		setCoordinate();
	}

	public void setCoordinate() {
		for (byte x = 0; x < 8; x++)
			for (byte y = 0; y < 8; y++)
				if (Brd.Checks[x][y].equals(this.getParent())) {
					Coo.set(y, x);
					return;
				}
	}
}
