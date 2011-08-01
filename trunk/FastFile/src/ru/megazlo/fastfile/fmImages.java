package ru.megazlo.fastfile;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.megazlo.fastfile.util.Sets;
import ru.megazlo.fastfile.util.file.FileTools;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class fmImages extends Activity {

	public static final String EXTEN = "jpg png";

	private ImageView image = null;
	private TextView title;
	private int counter = 0;
	private boolean drag = false;
	private AlphaAnimation animation = null;
	private File[] listf;
	private int wid;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wid = this.getResources().getDisplayMetrics().widthPixels / 2;
		Sets.applySets(this);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.image);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		title = (TextView) findViewById(R.id.title);
		ImageView close = (ImageView) findViewById(R.id.close_tab);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fmImages.this.finish();
			}
		});
		ImageView icon = (ImageView) findViewById(R.id.protocol);
		icon.setImageResource(R.drawable.file_img);

		String fil = this.getIntent().getExtras().getString(FileTools.KEY);
		File fl = new File(fil);
		listf = fl.getParentFile().listFiles(new PicFilter(EXTEN));
		List<File> tmp = new ArrayList<File>();
		for (int i = 0; i < listf.length; i++)
			tmp.add(listf[i]);
		Collections.sort(tmp, new CompFile());
		listf = tmp.toArray(listf);
		for (int i = 0; i < listf.length; i++)
			if (listf[i].getName().equals(fl.getName())) {
				counter = i;
				break;
			}
		title.setText(Integer.toString(counter + 1) + '/' + Integer.toString(listf.length) + "   "
				+ listf[counter].getName());
		// -----------------------------------------------------------------------------------

		image = (ImageView) findViewById(R.id.image);
		Drawable d = Drawable.createFromPath(listf[counter].getPath());
		image.setImageDrawable(d);
		animation = new AlphaAnimation(1.0f, 0.0f);
		animation.setStartOffset(500);
		animation.setDuration(300);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Drawable d = Drawable.createFromPath(listf[counter].getPath());
				image.setImageDrawable(d);
				title.setText(Integer.toString(counter + 1) + '/' + Integer.toString(listf.length) + "   "
						+ listf[counter].getName());
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			drag = true;
			break;
		case MotionEvent.ACTION_UP:
			float x = event.getX(0);
			if (drag) {
				if (x < wid)
					counter--;
				else if (x > wid)
					counter++;
				counter = counter < 0 ? listf.length : counter;
				counter = counter >= listf.length ? 0 : counter;
				image.startAnimation(animation);
				drag = false;
			}
			break;
		}
		return super.onTouchEvent(event);
	}
}

class PicFilter implements FilenameFilter {
	String[] exts;

	PicFilter(String afn) {
		exts = afn.split(" ");
		for (int i = 0; i < exts.length; i++)
			exts[i] = exts[i].startsWith(".") ? exts[i] : "." + exts[i];
	}

	@Override
	public boolean accept(File dir, String name) {
		String f = new File(name).getName();
		for (int i = 0; i < exts.length; i++)
			if (f.endsWith(exts[i]))
				return true;
		return false;
	}
}

class CompFile implements Comparator<File> {

	@Override
	public int compare(File obj1, File obj2) {
		return obj1.getName().toLowerCase().compareTo(obj2.getName().toLowerCase());
	}

}
