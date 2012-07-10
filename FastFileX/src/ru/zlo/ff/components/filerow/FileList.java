package ru.zlo.ff.components.filerow;

import ru.zlo.ff.engine.BaseEngine;
import ru.zlo.ff.util.Sets;
import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class FileList extends ListView implements BaseEngine.OnLoadFinish, BaseEngine.OnDataChanged {

	private BaseEngine eng;
	private FileRowAdapter itla;

	public FileList(Context context, BaseEngine engine) {
		super(context);
		setScrollingCacheEnabled(false);
		itla = new FileRowAdapter();
		setAdapter(itla);
		engine.setList(this);
		eng = engine;
		eng.setOnScrollFinish(this);
		eng.setOnDataChanger(this);
		eng.update();
	}

	@Override
	public boolean performItemClick(View view, int position, long id) {
		ItemClicker.click(eng, position);
		setSelection(0);
		return super.performItemClick(view, position, id);
	}

	public BaseEngine getEngine() {
		return eng;
	}

	@Override
	public void onFinish() {
		itla.setListItems(FileList.this.getEngine().getDat().dir);
		itla.notifyDataSetChanged();
		if (eng.scrollPoz > 0)
			FileList.this.setSelectionFromTop(eng.scrollPoz, FileList.this.getChildAt(0).getHeight() / 2);
		if (Sets.ANIMATE) {
			if (FileList.this.getLayoutAnimation() == null)
				FileList.this.setLayoutAnimation(Sets.LIST_ANIM);
			else
				FileList.this.startLayoutAnimation();
		}
		getEngine().startLoadImage();
	}

	@Override
	public void onChange() {
		((BaseAdapter) this.getAdapter()).notifyDataSetChanged();
	}

}
