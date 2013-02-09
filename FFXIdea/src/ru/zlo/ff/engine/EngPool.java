package ru.zlo.ff.engine;

import android.content.Context;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;
import ru.zlo.ff.components.RowData;
import ru.zlo.ff.components.RowDataSD;
import ru.zlo.ff.util.Options;

import java.util.ArrayList;
import java.util.List;

@EBean(scope = Scope.Singleton)
public class EngPool {

	@RootContext
	Context context;

	private List<BaseEngine> mEngs = new ArrayList<BaseEngine>();

	protected static EngPool mRef;
	private int currentPosition = 0;

	public static EngPool Inst() {
		return mRef;
	}

	protected EngPool() {
		if (mRef == null) {
			mRef = this;
			addEngine(new RowDataSD(Options.HOME_PATH), context);
			addEngine(new RowDataSD(Options.HOME_PATH), context);
		} else {
			for (BaseEngine eng : mEngs)
				this.addEngine(eng);
		}
	}

	public int count() {
		return mEngs.size();
	}

	public void addEngine(BaseEngine engine) {
		if (mEngs.size() < 2)
			mEngs.add(engine);
	}

	public void addEngine(RowData dat, Context context) {
		addEngine(choiceEngine(dat, context));
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

	public void setCurrentEngine(BaseEngine engine) {
		currentPosition = mEngs.indexOf(engine);
	}
}
