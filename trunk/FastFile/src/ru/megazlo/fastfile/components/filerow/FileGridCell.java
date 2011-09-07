package ru.megazlo.fastfile.components.filerow;

import java.io.File;

import ru.megazlo.fastfile.fmMain;
import ru.megazlo.fastfile.util.ActionFactory;
import ru.megazlo.ftplib.ftp.FTPFile;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FileGridCell extends BaseCellRow {

	public FileGridCell(Context context, FileRowData data) {
		super(context, data);
		LayoutParams prm = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		setLayoutParams(prm);
	}

	@Override
	protected void initChild() {
		m_icon = new ImageView(getContext());
		m_icon.setImageDrawable(dat.getIcon());
		m_icon.setPadding(px(5.3), px(6.6), 0, 0);
		addView(m_icon, px(42.6), px(42.6));

		this.m_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Object tst = FileGridCell.this.dat.getFile();
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

		m_txt_dir = new TextView(getContext());
		m_txt_dir.setTextSize(16);
		m_txt_dir.setHorizontallyScrolling(true);
		m_txt_dir.setMinimumHeight(px(24.6));
		m_txt_dir.setMaxHeight(px(24.6));
		m_txt_dir.setTypeface(null, Typeface.BOLD);
		m_txt_dir.setText(dat.getName());
		LayoutParams m_txt_dir_lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		m_txt_dir_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		m_txt_dir.setPadding(px(46.6), 0, 0, 0);
		this.addView(m_txt_dir, m_txt_dir_lp);

		m_txt_items = new TextView(getContext());
		m_txt_items.setText(dat.getItmText());
		m_txt_items.setMinimumHeight(px(16.6));
		m_txt_items.setMaxHeight(px(16.6));
		m_txt_items.setTextSize(12);
		LayoutParams m_txt_items_lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		m_txt_items_lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		m_txt_items.setPadding(px(46.6), 0, 0, 0);
		this.addView(m_txt_items, m_txt_items_lp);

		m_txt_date = new TextView(getContext());
		m_txt_date.setText(dat.getDateText());
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
		this.m_check = new ImageView(getContext());
		this.m_check.setPadding(0, px(6.6), px(6.6), 0);
		this.m_check.setImageDrawable(dat.getIconCheck());
		this.addView(m_check, lp);

		this.m_check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dat.setChecked(!dat.isChecked());
				FileGridCell.this.m_check.setImageDrawable(dat.getIconCheck());
			}
		});

		this.m_check.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				fmMain.CONTEXT.getCurEng().selectAll();
				return true;
			}
		});

	}

}
