package ru.zlo.ff.engine;

import ru.zlo.ff.components.RowData;

import java.io.File;

public interface IEngine {

	boolean browseUp();

	void browseCatalog(Object cat);

	Object[] getFiles();

	void update();

	void browseRoot();

	void search(String search);

	RowData getDat();

	File getCurrentDir();

	boolean isAllowSearsh();

	void fill(Object filar);

	void startLoadImage();

	String getTitle();

	Object exec(int cmd);

	void selectAll();

	void stopThreads();
}
