package ru.zlo.ff.engine;

import android.content.Context;
import ru.zlo.ff.components.RowData;

public class EngineARC extends BaseEngine {

	public EngineARC(Context context) {
		super(context);
		// RARFile fl = null;
		// try {
		// fl = new RARFile(new File("/"));
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// Enumeration<RAREntry> ent = fl.entries();
	}

	@Override
	public boolean browseUp() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void browseCatalog(Object cat) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void browseRoot() {
		// TODO Auto-generated method stub

	}

	@Override
	public void search(String search) {
		// TODO Auto-generated method stub

	}

	@Override
	public RowData getDat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCurrentDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object exec(int cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
