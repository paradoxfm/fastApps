package ru.zlo.ff.components.filerow;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.*;
import ru.zlo.ff.MAct;
import ru.zlo.ff.R;
import ru.zlo.ff.util.ActionFactory;

import java.io.File;

@EViewGroup(R.layout.filerow)
public class FileRow extends RelativeLayout {
	@ViewById
	protected TextView m_txt_dir, m_txt_items, m_txt_date;
	@ViewById
	protected ImageView m_icon, m_check;
	protected FileRowData dat;

	public FileRow(Context context, FileRowData data) {
		super(context);
		dat = data;
	}

	@AfterViews
	protected void initChildUI() {
		m_txt_dir.setText(dat.getName());
		m_txt_items.setText(dat.getItmText());
		m_txt_date.setText(dat.getDateText());
		m_check.setImageDrawable(dat.getIconCheck());
		m_icon.setImageDrawable(dat.getIcon());
	}

	@Click(R.id.m_icon)
	protected void iconClick(View v) {
		Object tst = FileRow.this.dat.getFile();
		if (tst.getClass() == File.class) {
			File fl = (File) tst;
			if ((fl.canWrite() && fl.isFile()) || (fl.isDirectory()))
				ActionFactory.create(v, fl).show();
		}
	}

	@Click(R.id.m_check)
	protected void checkClick() {
		dat.setChecked(!dat.isChecked());
		FileRow.this.m_check.setImageDrawable(dat.getIconCheck());
	}

	@LongClick(R.id.m_check)
	protected boolean checkLongClick() {
		MAct.I.getCurEng().selectAll();
		return true;
	}

	public void setData(FileRowData data) {
		dat = data;
		initChildUI();
	}

	public File getFile() {
		return dat.getFile();
	}
}
