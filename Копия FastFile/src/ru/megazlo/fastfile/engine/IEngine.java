package ru.megazlo.fastfile.engine;

import ru.megazlo.fastfile.components.RowData;

public interface IEngine {
	
	public int browseUp();

	public void browseCatalog(Object cat);

	public Object[] getFiles();

	public void update();

	public void browseRoot();

	public void search(String search);

	public RowData getDat();

	public Object getCurrentDir();

	public boolean isAllowSearsh();

	void fill(Object filar);

	public void startLoadImage();

	public String getTitle();

}
