


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.widget.GridView;

import ru.megazlo.jbtchess.board.JCheck;
import ru.megazlo.jbtchess.board.events.*;
import ru.megazlo.jbtchess.board.pieces.*;

public class JChessBoard extends GridView {

	private static final long serialVersionUID = 1L;
	private boolean isPaused = false;
	private ArrayList<BoardListener> listeners = new ArrayList<BoardListener>();
	private Context cont;
	public JPiece Draget;
	public JCheck[][] Checks = new JCheck[8][8];
	public Col WhoMove = Col.White;

	public JChessBoard(Context c) {
		super(c);
		cont = c;
		this.initComponents();
	}

	private void initComponents() {
		// JCheck Ck = new JCheck(cont, false, this);
		this.setNumColumns(8);
		this.initChecks();
		this.initPieces();
	}

	private void initChecks() {
		Boolean Col = false;
		for (int xx = 7; xx >= 0; xx--) {
			Col = !Col;
			for (int yy = 0; yy < 8; yy++) {
				Col = !Col;
				JCheck Sq = new JCheck(cont, Col, this);
				Checks[xx][yy] = Sq;
				// this.add(Sq);
			}
		}
	}

	private void initPieces() {
		for (int ii = 7; ii >= 0; ii--) {
			new JPawn(cont, Type.Pawn, Col.White, new Point(ii, 1), Checks[1][ii]);
			new JPawn(cont, Type.Pawn, Col.Black, new Point(ii, 6), Checks[6][ii]);
		}
		initPiecesDop(7, Col.Black);
		initPiecesDop(0, Col.White);
		new JKing(cont, Type.King, Col.Black, Checks[7][4]);
		new JKing(cont, Type.King, Col.White, Checks[0][4]);
		new JQueen(cont, Type.Queen, Col.Black, Checks[7][3]);
		new JQueen(cont, Type.Queen, Col.White, Checks[0][3]);
	}

	private void initPiecesDop(int NLine, Col Col) {
		new JBishop(cont, Type.Bishop, Col, Checks[NLine][2]);
		new JBishop(cont, Type.Bishop, Col, Checks[NLine][5]);
		new JRook(cont, Type.Rook, Col, Checks[NLine][0]);
		new JRook(cont, Type.Rook, Col, Checks[NLine][7]);
		new JKnight(cont, Type.Knight, Col, Checks[NLine][1]);
		new JKnight(cont, Type.Knight, Col, Checks[NLine][6]);
	}

	public void disableDrop() {
		for (byte x = 0; x < 8; x++)
			for (byte y = 0; y < 8; y++)
				this.Checks[x][y].setDragDrop(false);
	}

	public void swapMove() {
		doMove();
		if (WhoMove == Col.White)
			WhoMove = Col.Black;
		else
			WhoMove = Col.White;
		// OnMovePiece(this, EventArgs.Empty);
		// Old = Ph.Coo;
		checkOnShah();
		// New = Ph.Coo;
		checkOnMat();
		// if (GameTp == GameType.VsLan && Ph.Color == YourColor)
		// {
		// SendHod();
		// BackW.RunWorkerAsync();
		// }
	}

	private void checkOnMat() {
	}

	private void checkOnShah() {
		disableDrop();
		Draget.createMotion();
		for (int x = 0; x < 8; x++)
			for (int y = 0; y < 8; y++)
				if (this.Checks[y][x].getComponentCount() > 0 && this.Checks[y][x].getDropTarget() != null) {
					JPiece King = (JPiece) this.Checks[y][x].getComponent(0);
					if (King.Type == Type.King && King.Color != Draget.Color)
						JOptionPane.showMessageDialog(null,
								"Король в опасности!\r\nПрикройте или уберите его на неатакуемое поле!", "Шах!",
								JOptionPane.WARNING_MESSAGE);
				}
	}

	public void resetRound() {
		for (int ii = 0; ii < 8; ii++)
			for (int yy = 0; yy < 8; yy++)
				if (Checks[ii][yy].getComponentCount() > 0) {
					Checks[ii][yy].remove(0);
					Checks[ii][yy].repaint();
				}
		initPieces();
		WhoMove = Col.White;
	}

	public void stopMotion() {
		for (int ii = 0; ii < 8; ii++)
			for (int yy = 0; yy < 8; yy++)
				if (Checks[ii][yy].getDropTarget() != null)
					Checks[ii][yy].setDragDrop(false);
	}

	public void addBoardListener(BoardListener listener) {
		if (listener == null) {
			listeners.clear();
			return;
		}
		listeners.add(listener);
	}

	public BoardListener[] getMyListeners() {
		return listeners.toArray(new BoardListener[listeners.size()]);
	}

	public void removeBoardListener(BoardListener listener) {
		listeners.remove(listener);
	}

	public int getWhiteCount() {
		return getPieceCount(Col.White);
	}

	public int getBlackCount() {
		return getPieceCount(Col.Black);
	}

	private int getPieceCount(Col Colo) {
		int CountWhite = 0;
		for (byte x = 0; x < 8; x++)
			for (byte y = 0; y < 8; y++)
				if (this.Checks[x][y].getComponentCount() > 0) {
					JPiece Ph = (JPiece) this.Checks[x][y].getComponent(0);
					if (Ph.Color == Colo)
						CountWhite++;
				}
		return CountWhite;
	}

	public void startDragPiece() {
		doBeginDrag();
	}

	public void endDragPiece() {
		doEndDrag();
	}

	private void doMove() {
		BoardEvent ev = new BoardEvent(this);
		for (BoardListener listener : listeners)
			listener.movePiece(ev);
	}

	private void doBeginDrag() {
		BoardEvent ev = new BoardEvent(this);
		for (BoardListener listener : listeners)
			listener.startDragPiece(ev);
	}

	private void doEndDrag() {
		BoardEvent ev = new BoardEvent(this);
		for (BoardListener listener : listeners)
			listener.endDragPiece(ev);
	}

	public void setPause(boolean Pause) {
		this.isPaused = Pause;
		for (int ii = 0; ii < Checks.length; ii++)
			for (int yy = 0; yy < Checks[ii].length; yy++) {
				if (Checks[ii][yy].getComponentCount() > 0)
					Checks[ii][yy].getComponent(0).setVisible(!isPaused);
			}
	}

	public boolean getPauseState() {
		return isPaused;
	}
}
