package ru.megazlo.fastnote.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.EditText;

public class NoteEdit extends EditText {
	private final Rect mRect = new Rect();
	private Paint mPaint = new Paint();

	public NoteEdit(Context context) {
		super(context);
		mPaint.setStyle(Paint.Style.STROKE);
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

	public void setPaintColor(int newColor) {
		mPaint.setColor(newColor);
	}
}
