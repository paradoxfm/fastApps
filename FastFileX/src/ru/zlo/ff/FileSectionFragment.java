package ru.zlo.ff;

import ru.zlo.ff.components.filerow.FileList;
import ru.zlo.ff.engine.EngPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FileSectionFragment extends Fragment {
	public static final String ENG_NUM = "ENG_NUM";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		int num = getArguments().getInt(ENG_NUM);
		return new FileList(container.getContext(), EngPool.Inst().getEngine(num));
	}

}
