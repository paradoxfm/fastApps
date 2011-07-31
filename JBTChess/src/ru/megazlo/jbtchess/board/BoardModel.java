package ru.megazlo.jbtchess.board;

import android.graphics.Color;
import ru.megazlo.jbtchess.board.pieces.Type;

public class BoardModel {
	/** количество столбцов на доске */
	private static int mCols = 8;
	/** количество строк на доске */
	private static int mRows = 8;
	/** расположение фигур на шахматной доске */
	private CheckModel[][] mCells = new CheckModel[mRows][mCols];

	/** Конструктор */
	public BoardModel() {
		initChecks();
		initPieces();
	}

	private void initChecks() {
		Boolean Col = false;
		for (int xx = mCols - 1; xx >= 0; xx--, Col = !Col)
			for (int yy = 0; yy < mRows; yy++, Col = !Col)
				mCells[xx][yy] = new CheckModel(Col);
	}

	private void initPieces() {
		for (int ii = 7; ii >= 0; ii--) {
			mCells[1][ii].setItem(Type.Pawn, Color.WHITE);
			mCells[6][ii].setItem(Type.Pawn, Color.BLACK);
		}
		initPiecesDop(7, Color.BLACK);
		initPiecesDop(0, Color.WHITE);
		mCells[7][4].setItem(Type.King, Color.BLACK);
		mCells[0][4].setItem(Type.King, Color.WHITE);
		mCells[7][3].setItem(Type.Queen, Color.BLACK);
		mCells[0][3].setItem(Type.Queen, Color.WHITE);
	}

	private void initPiecesDop(int NLine, int Col) {
		mCells[NLine][2].setItem(Type.Bishop, Col);
		mCells[NLine][5].setItem(Type.Bishop, Col);
		mCells[NLine][0].setItem(Type.Rook, Col);
		mCells[NLine][7].setItem(Type.Rook, Col);
		mCells[NLine][1].setItem(Type.Knight, Col);
		mCells[NLine][6].setItem(Type.Knight, Col);
	}

	/**
	 * @return Размер поля
	 */
	public int getCount() {
		return mCols * mRows;
	}

	/**
	 * @param row
	 *          Номер строки
	 * @param col
	 *          Номер столбца
	 * @return Значение ячейки, находящейся в указанной строке и указанном столбце
	 */
	private CheckModel itemAt(int row, int col) {
		if (row < 0 || row >= mRows || col < 0 || col >= mCols)
			return null;
		return mCells[row][col];
	}

	public CheckModel itemAt(int position) {
		int r = position / mCols;
		int c = position % mCols;
		return itemAt(r, c);
	}

}

class CheckModel {
	private Boolean cColor;
	private int pType;
	private int pColor;

	/** Инструктор для инициализации полей доски */
	public CheckModel(Boolean white) {
		setNomeItem();
		cColor = white;
	}

	/** Инструктор для инициализации фигур на доске */
	public CheckModel(int type, int color) {
		pType = type;
		pColor = color;
	}

	public int getType() {
		return pType;
	}

	public int getColor() {
		return pColor;
	}

	public Boolean getCheckColor() {
		return cColor;
	}

	public void setItem(int type, int color) {
		pType = type;
		pColor = color;
	}

	public void setNomeItem() {
		pType = Type.None;
		pColor = -1;
	}
}