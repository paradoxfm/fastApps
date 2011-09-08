package ru.megazlo.crazytest.classes;

import java.util.Date;

public class NoteData {
	public int ID;
	public Date From;
	public String Title;

	public NoteData(int _ID, Date _From, String _Title) {
		ID = _ID;
		From = _From;
		Title = _Title;
	}
}
