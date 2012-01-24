package ru.megazlo.ffng.components;

import java.util.ArrayList;
import java.util.List;

import ru.megazlo.ffng.components.filerow.FileRowData;

public abstract class RowData {
	public List<FileRowData> dir = new ArrayList<FileRowData>();
	public List<FileRowData> fil = new ArrayList<FileRowData>();
}
