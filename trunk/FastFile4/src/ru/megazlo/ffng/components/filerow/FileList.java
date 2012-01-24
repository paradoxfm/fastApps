package ru.megazlo.ffng.components.filerow;

import ru.megazlo.ffng.components.RowData;
import ru.megazlo.ffng.components.RowDataFTP;
import ru.megazlo.ffng.components.RowDataLAN;
import ru.megazlo.ffng.engine.BaseEngine;
import ru.megazlo.ffng.engine.EngineFTP;
import ru.megazlo.ffng.engine.EngineLAN;
import ru.megazlo.ffng.engine.EngineSDC;
import ru.megazlo.ffng.util.Sets;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class FileList extends ListView {

	protected Drawable mIcoProtocol;
	private BaseEngine eng;
	private FileRowAdapter itla;

	public FileList(Context context, RowData dat, boolean restore) {
		super(context);
		setScrollingCacheEnabled(false);
		itla = new FileRowAdapter();
		setAdapter(itla);
		eng = choiceEngine(dat);

		eng.setOnScrollFinish(new BaseEngine.OnLoadFinish() {
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
				FileList.this.getEngine().startLoadImage();
			}
		});

		eng.setOnDataChanger(new BaseEngine.OnDataChanged() {
			@Override
			public void onChange() {
				((BaseAdapter) FileList.this.getAdapter()).notifyDataSetChanged();
			}
		});

		eng.update();
	}

	@Override
	public boolean performItemClick(View view, int position, long id) {
		ItemClicker.click(eng, position);
		setSelection(0);
		return super.performItemClick(view, position, id);
	}

	private BaseEngine choiceEngine(RowData dat) {
		if (dat.getClass() == RowDataFTP.class)
			return new EngineFTP(dat, this);
		else if (dat.getClass() == RowDataLAN.class)
			return new EngineLAN(dat, this);
		return new EngineSDC(dat, this);
	}

	public BaseEngine getEngine() {
		return eng;
	}

}
