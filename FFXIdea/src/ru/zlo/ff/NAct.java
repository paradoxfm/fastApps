package ru.zlo.ff;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.googlecode.androidannotations.annotations.*;
import ru.zlo.ff.util.Options;
import ru.zlo.ff.util.file.FileTools;

import java.io.*;

@EActivity(R.layout.note_editor)
@OptionsMenu(R.menu.editor)
public class NAct extends Activity {

	@Bean
	Options options;
	@ViewById(R.id.note)
	EditText mText;
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

	@AfterViews
	void initOnCreate() {
		file = new File(getIntent().getExtras().getString(FileTools.KEY));
		setTitle(file.getName());
		mText.setTextSize(16);
		mText.setTextColor(Color.BLACK);
		mText.setBackgroundColor(Color.WHITE);
		mText.setFocusable(false); // не показываем клавиатуру
		try {
			mText.setText(read());
		} catch (IOException ignored) {
		}
	}

	@Override
	protected void onResume() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.i_file_doc);
		int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (Options.FULL_SCR)
			getWindow().setFlags(flg, flg);
		else
			getWindow().clearFlags(flg);
		setRequestedOrientation(Options.ORIENT_TYPE);
		super.onResume();
	}

	@Override
	@OptionsItem(android.R.id.home)
	public void onBackPressed() {
		super.onBackPressed();
	}

	@OptionsItem(R.id.edit_text)
	void menuEditText() {
		mText.setFocusable(true);
		mText.setFocusableInTouchMode(true);
		InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMgr.toggleSoftInput(0, 0);
	}

	@OptionsItem(R.id.save_text)
	void menuSaveText() {
		mText.setFocusable(false);
		try {
			write();
		} catch (IOException ignored) {
		}
		((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				mText.getWindowToken(), 0);
		Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
	}

	private String read() throws IOException {
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader rd = new BufferedReader(isr);
		String s;
		StringBuilder buf = new StringBuilder();
		while ((s = rd.readLine()) != null) {
			buf.append(s).append("\n");
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
