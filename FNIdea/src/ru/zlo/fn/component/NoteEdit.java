package ru.zlo.fn.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.googlecode.androidannotations.annotations.EView;

@EView
public class NoteEdit extends EditText {

	private Boolean _isMoved = false;
	private Boolean _isEditable = false;

	private final Rect mRect = new Rect();
	private Paint mPaint = new Paint();

	public NoteEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint.setStyle(Paint.Style.STROKE);
	}

	/*public void disableEdit() {
		_isEditable = false;
		//setFocusable(false);
		// InputMethodManager inMan = (InputMethodManager)
		// getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		// inMan.hideSoftInputFromWindow(getWindowToken(),
		// InputMethodManager.HIDE_NOT_ALWAYS);
	}*/

	@Override
	public boolean onKeyPreIme(int cod, KeyEvent e) {
		if (_isEditable) {
			setFocusable(false);
			_isEditable = false;
		}
		return super.onKeyPreIme(cod, e);
	}

	/*public Boolean isEdit() {
		return _isEditable;
	}*/

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_MOVE) {
			_isMoved = true;
		} else if (e.getAction() == MotionEvent.ACTION_UP) {
			if (!_isMoved && !_isEditable) {
				_isEditable = !_isEditable;
				setFocusable(true);
				setFocusableInTouchMode(true);
				InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInputFromInputMethod(getWindowToken(), 0);
			}
			_isMoved = false;
		}
		return super.onTouchEvent(e);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		final int count = getLineCount();
		for (int i = 0; i < count; i++) {
			int baseline = getLineBounds(i, mRect) + 1;
			canvas.drawLine(mRect.left, baseline, mRect.right, baseline, mPaint);
		}
		super.onDraw(canvas);
	}

	public void setPaintColor(int newColor) {
		mPaint.setColor(newColor);
	}
}
