package ru.megazlo.pager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PagerControl extends View {
	// private static final String TAG =
	// "ru.megazlo.sdmanager.pager.PagerControl";

	private static final int DEFAULT_BAR_COLOR = 0xaa777777;
	private static final int DEFAULT_HIGHLIGHT_COLOR = 0xaa999999;

	private int numPages, currentPage, position;
	private Paint barPaint, highlightPaint;
	private float ovalRadius = 3;

	public PagerControl(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PagerControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		barPaint = new Paint();
		barPaint.setColor(DEFAULT_BAR_COLOR);

		highlightPaint = new Paint();
		highlightPaint.setColor(DEFAULT_HIGHLIGHT_COLOR);
	}

	/**
	 * 
	 * @return current number of pages
	 */
	public int getNumPages() {
		return numPages;
	}

	/**
	 * 
	 * @param numPages
	 *          must be positive number
	 */
	public void setNumPages(int numPages) {
		if (numPages <= 0) {
			throw new IllegalArgumentException("numPages must be positive");
		}
		this.numPages = numPages;
		invalidate();
	}

	/**
	 * 0 to numPages-1
	 * 
	 * @return
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * 
	 * @param currentPage
	 *          0 to numPages-1
	 */
	public void setCurrentPage(int currentPage) {
		if (currentPage < 0 || currentPage >= numPages) {
			throw new IllegalArgumentException("currentPage parameter out of bounds");
		}
		if (this.currentPage != currentPage) {
			this.currentPage = currentPage;
			this.position = currentPage * getPageWidth();
			invalidate();
		}
	}

	/**
	 * Equivalent to the width of the view divided by the current number of pages.
	 * 
	 * @return page width, in pixels
	 */
	public int getPageWidth() {
		return getWidth() / numPages;
	}

	/**
	 * 
	 * @param position
	 *          can be -pageWidth to pageWidth*(numPages+1)
	 */
	public void setPosition(int position) {
		if (this.position != position) {
			this.position = position;
			invalidate();
		}
	}

	/**
	 * 
	 * @param canvas
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), ovalRadius, ovalRadius, barPaint);
		canvas.drawRoundRect(new RectF(position, 0, position + (getWidth() / numPages), getHeight()), ovalRadius,
				ovalRadius, highlightPaint);
	}
}