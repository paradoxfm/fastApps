package ru.megazlo.fastnote.util;

import java.io.File;
import java.util.ArrayList;

import ru.megazlo.fastnote.component.NoteData;
import android.os.Handler;
import android.os.Message;

public class NotesLoader extends Thread {

	private Handler handler;
	private File path;
	private String query;

	public NotesLoader(Handler handler, File path, String query) {
		this.handler = handler;
		this.path = path;
		this.query = query;
	}

	public void run() {
		try {
			ArrayList<NoteData> dat = SqlBase.getList(path, query);
			for (int i = 0; i < dat.size(); i++) {
				Thread.sleep(50);
				Message msg = handler.obtainMessage(1);
				msg.obj = dat.get(i);
				msg.sendToTarget();
			}
		} catch (Exception e) {
		}
	}

}
