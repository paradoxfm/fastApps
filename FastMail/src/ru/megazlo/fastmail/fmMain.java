package ru.megazlo.fastmail;

import ru.megazlo.pager.HorizontalPager;
import ru.megazlo.pager.PagerControl;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class fmMain extends Activity {

	public HorizontalPager pager;
	public PagerControl control;
	private TextView title;
	private ImageView newnote;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		initChild();
		title.setText(R.string.app_name);
		pager.addView(new TextView(this));
		control.setNumPages(1);
	}

	private void initChild() {
		title = (TextView) findViewById(R.id.title);
		newnote = (ImageView) findViewById(R.id.newnote);
		control = (PagerControl) findViewById(R.id.pageind);
		pager = (HorizontalPager) findViewById(R.id.pager);
		pager.addOnScrollListener(new HorizontalPager.OnScrollListener() {
			public void onScroll(int scrollX) {
				float scale = (float) (pager.getPageWidth() * pager.getChildCount()) / (float) control.getWidth();
				control.setPosition((int) (scrollX / scale));
			}

			public void onViewScrollFinished(int currentPage) {
				control.setCurrentPage(currentPage);
			}
		});

		newnote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.exit(0);
			}
		});
	}

}
