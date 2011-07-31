package ru.megazlo.jbtchess.board.events;

import java.util.EventObject;

public class BoardEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	public BoardEvent(Object source) {
		super(source);
	}
}