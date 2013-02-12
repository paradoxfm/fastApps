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

	String getTitle();

	void selectAll();

	void stopThreads();
}
