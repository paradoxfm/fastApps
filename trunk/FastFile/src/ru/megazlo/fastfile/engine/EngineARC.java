package ru.megazlo.fastfile.engine;

import ru.megazlo.fastfile.components.RowData;
import ru.megazlo.fastfile.components.filerow.FileList;

public class EngineARC extends BaseEngine {

	public EngineARC(FileList list) {
		super(list);
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
