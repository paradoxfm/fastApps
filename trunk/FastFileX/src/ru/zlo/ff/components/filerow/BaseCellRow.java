package ru.zlo.ff.components.filerow;

import java.io.File;

import ru.zlo.ff.util.Sets;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class BaseCellRow extends RelativeLayout {

	protected TextView m_txt_dir, m_txt_items, m_txt_date;
	protected ImageView m_icon, m_check;
	protected FileRowData dat;

	public BaseCellRow(Context context, FileRowData data) {
		super(context);
		dat = data;
		initChild();
	}

	protected abstract void initChild();

	public void setData(FileRowData data) {
		dat = data;
		m_txt_dir.setText(dat.getName());
		m_txt_items.setText(dat.getItmText());
		m_txt_date.setText(dat.getDateText());
		m_icon.setImageDrawable(dat.getIcon());
		m_check.setImageDrawable(dat.getIconCheck());
	}

	public File getFile() {
		return dat.getFile();
	}

	protected static int px(double d) {
		return (int) (d * Sets.DIP_CONV);
	}

}
