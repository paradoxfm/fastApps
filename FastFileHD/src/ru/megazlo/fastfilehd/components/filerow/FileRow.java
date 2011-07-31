package ru.megazlo.fastfilehd.components.filerow;

import java.io.File;

import ru.megazlo.fastfilehd.fmMain;
import ru.megazlo.fastfilehd.util.ActionFactory;
import ru.megazlo.fastfilehd.util.Sets;
import ru.megazlo.ftplib.ftp.FTPFile;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FileRow extends RelativeLayout {

	private TextView m_txt_dir, m_txt_items, m_txt_date;
	private ImageView m_icon, m_check;
	private FileRowData dat;

	public FileRow(Context context, FileRowData data) {
		super(context);
		dat = data;
		// dat.setFileRow(this);
		this.setMinimumHeight(px(46.6));
		initChild(context, data);
	}

	public void updateChecked() {
		this.m_check.setImageDrawable(dat.getIconCheck());
	}

	private void initChild(final Context context, FileRowData data) {
		this.m_icon = new ImageView(context);
		this.m_icon.setImageDrawable(data.getIcon());
		this.m_icon.setPadding(px(5.3), px(6.6), 0, 0);
		this.addView(m_icon, px(42.6), px(42.6));

		this.m_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Object tst = FileRow.this.dat.getFile();
				if (tst.getClass() == File.class) {
					File fl = (File) tst;
					if ((fl.canWrite() && fl.isFile()) || (fl.isDirectory()))
						ActionFactory.create(v, fl).show();
				}
				if (tst.getClass() == FTPFile.class) {
					FTPFile fl = (FTPFile) tst;
					ActionFactory.create(v, fl).show();
				}
			}
		});

		m_txt_dir = new TextView(context);
		m_txt_dir.setTextSize(16);
		m_txt_dir.setHorizontallyScrolling(true);
		m_txt_dir.setMinimumHeight(px(24.6));
		m_txt_dir.setMaxHeight(px(24.6));
		m_txt_dir.setTypeface(null, Typeface.BOLD);
		m_txt_dir.setText(data.getName());
		LayoutParams m_txt_dir_lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		m_txt_dir_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		m_txt_dir.setPadding(px(46.6), 0, 0, 0);
		this.addView(m_txt_dir, m_txt_dir_lp);

		m_txt_items = new TextView(context);
		m_txt_items.setText(data.getItmText());
		m_txt_items.setMinimumHeight(px(16.6));
		m_txt_items.setMaxHeight(px(16.6));
		m_txt_items.setTextSize(12);
		LayoutParams m_txt_items_lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		m_txt_items_lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		m_txt_items.setPadding(px(46.6), 0, 0, 0);
		this.addView(m_txt_items, m_txt_items_lp);

		m_txt_date = new TextView(context);
		m_txt_date.setText(data.getDateText());
		m_txt_date.setGravity(Gravity.RIGHT);
		m_txt_date.setMinimumHeight(px(16.6));
		m_txt_date.setMaxHeight(px(16.6));
		m_txt_date.setTextSize(12);
		m_txt_date.setPadding(0, 0, px(5.3), 0);
		LayoutParams m_txt_date_lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		m_txt_date_lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		this.addView(m_txt_date, m_txt_date_lp);

		LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		this.m_check = new ImageView(context);
		this.m_check.setPadding(0, px(6.6), px(6.6), 0);
		this.m_check.setImageDrawable(data.getIconCheck());
		this.addView(m_check, lp);

		this.m_check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dat.setChecked(!dat.isChecked());
				FileRow.this.updateChecked();
			}
		});

		this.m_check.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				fmMain.CONTEXT.getCurrentList().selectAll();
				return true;
			}
		});
	}

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

	private static int px(double d) {
		return (int) (d * Sets.DIP_CONV);
	}
}
