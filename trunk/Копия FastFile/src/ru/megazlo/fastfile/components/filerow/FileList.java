package ru.megazlo.fastfile.components.filerow;

import ru.megazlo.fastfile.engine.BaseEngine;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ListView;

public class FileList extends ListView {

	protected Drawable mIcoProtocol;
	private BaseEngine eng;

	public FileList(Context context) {
		super(context);
		this.setPadding(0, 10, 0, 0);

		eng = null;
		eng.setOnScrollFinish(new BaseEngine.OnScrollFinish() {
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
			}
		});
	}

	public BaseEngine getEngine() {
		return eng;
	}

}
