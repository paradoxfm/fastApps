package ru.zlo.ff.engine;

import ru.zlo.ff.components.RowData;

import java.util.ArrayList;
import java.util.List;

public class EngPool {

	private List<BaseEngine> mEngs = new ArrayList<BaseEngine>();

	private static EngPool mRef;

	public static EngPool Inst() {
		if (mRef == null)
			mRef = new EngPool();
		return mRef;
	}

	private EngPool() {

	}

	public int count() {
		return mEngs.size();
	}

	public void addEngine(BaseEngine engine) {
		mEngs.add(engine);
	}

	public void addEngine(RowData dat) {
		mEngs.add(choiceEngine(dat));
	}

	private BaseEngine choiceEngine(RowData dat) {
		return new EngineSDC(dat, null);
	}

	public BaseEngine getEngine(int position) {
		return mEngs.get(position);
	}

	public void removeCurrent(int position) {
		mEngs.remove(position);
	}

	public void removeCurrent(BaseEngine eng) {
		mEngs.remove(eng);
	}

}
