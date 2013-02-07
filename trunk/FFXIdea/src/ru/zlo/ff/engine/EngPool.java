package ru.zlo.ff.engine;

import android.content.Context;
import ru.zlo.ff.components.RowData;

import java.util.ArrayList;
import java.util.List;

public class EngPool {

	private List<BaseEngine> mEngs = new ArrayList<BaseEngine>();

	private static EngPool mRef;
	private int currentPosition = 0;

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

	public void addEngine(RowData dat, Context context) {
		mEngs.add(choiceEngine(dat, context));
	}

	private BaseEngine choiceEngine(RowData dat, Context context) {
		return new EngineSDC(dat, context);
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

	public void setCurrentPosition(int pos) {
		currentPosition = pos;
	}

	public BaseEngine getCurrent() {
		return mEngs.get(currentPosition);
	}
}
