package ru.megazlo.jbtchess.board.events;

public interface BoardListener {
	public void movePiece(BoardEvent ev);

	public void startDragPiece(BoardEvent ev);

	public void endDragPiece(BoardEvent ev);
}
