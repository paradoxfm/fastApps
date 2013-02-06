package ru.zlo.ff.components;

import ru.zlo.ff.components.filerow.FileRowData;

import java.util.ArrayList;
import java.util.List;

public abstract class RowData {
	public List<FileRowData> dir = new ArrayList<FileRowData>();
	public List<FileRowData> fil = new ArrayList<FileRowData>();
}
