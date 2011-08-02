package ru.megazlo.fastfile.components.filerow;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FileGridCell extends LinearLayout {

	private TextView m_txt_dir;
	private ImageView m_icon, m_check;
	private FileRowData dat;

	public FileGridCell(Context context, FileRowData data) {
		super(context);
		dat = data;
		initChild(context, data);
	}

	private void initChild(Context context, FileRowData data) {
		// TODO Auto-generated method stub

	}

}
