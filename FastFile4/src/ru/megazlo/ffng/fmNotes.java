package ru.megazlo.ffng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import ru.megazlo.ffng.R;
import ru.megazlo.ffng.util.Sets;
import ru.megazlo.ffng.util.file.FileTools;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class fmNotes extends Activity {

	private EditText mText;
	private File file;

	public static class LinedEditText extends EditText {
		private Rect mRect;
		private Paint mPaint;

		// we need this constructor for LayoutInflater
		public LinedEditText(Context context, AttributeSet attrs) {
			super(context, attrs);
			mRect = new Rect();
			mPaint = new Paint();
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setColor(0x800000FF);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			int count = getLineCount();
			for (int i = 0; i < count; i++) {
				int baseline = getLineBounds(i, mRect);
				canvas.drawLine(mRect.left, baseline + 1, mRect.right, baseline + 1, mPaint);
			}
			super.onDraw(canvas);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Sets.applySets(this);
		if (Sets.IS_COLORED) {
			Bitmap bmp = Bitmap.createBitmap(new int[] { Sets.BACK_COLOR }, 1, 1, Config.ARGB_8888);
			Drawable drw = new BitmapDrawable(bmp);
			getWindow().setBackgroundDrawable(drw);
		}
		setContentView(R.layout.note_editor);
		getActionBar().setIcon(R.drawable.file_doc);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		String name = getIntent().getExtras().getString(FileTools.KEY);
		file = new File(name);

		// TextView title = (TextView) findViewById(R.id.title);
		// title.setText(R.string.tl_edit);
		// title.setText(title.getText() + file.getName());

		mText = (EditText) findViewById(R.id.note);
		mText.setTextSize(16);
		mText.setTextColor(Color.BLACK);
		mText.setFocusable(false); // не показываем клавиатуру
		mText.setBackgroundColor(Color.WHITE);
		try {
			mText.setText(read());
		} catch (IOException e) {
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.editor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		case R.id.edit_text:
			mText.setFocusable(true);
			mText.setFocusableInTouchMode(true);
			InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMgr.toggleSoftInput(0, 0);
			break;
		case R.id.save_text:
			mText.setFocusable(false);
			try {
				write();
			} catch (IOException e) {
			}
			((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					mText.getWindowToken(), 0);
			Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}

	private String read() throws IOException {
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader rd = new BufferedReader(isr);
		String s = new String();
		StringBuffer buf = new StringBuffer();
		while ((s = rd.readLine()) != null) {
			buf.append(s + "\n");
		}
		rd.close();
		isr.close();
		fis.close();
		return buf.toString();
	}

	private void write() throws IOException {
		Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		try {
			out.write(mText.getText().toString());
		} finally {
			out.close();
		}
	}

}
