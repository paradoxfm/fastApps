package ru.megazlo.quicker;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/** Popup window, shows action list as icon and text */
public class QuickAction extends CustomPopupWindow {
	private final View root;
	private final ImageView mArrowUp;
	private final ImageView mArrowDown;
	private final Animation mTrackAnim;
	private final LayoutInflater inflater;

	private boolean animateTrack;
	private ViewGroup mTrack;
	private ArrayList<ActionItem> actionList;

	/**
	 * Constructor
	 * @param anchor {@link android.view.View} on where the popup should be displayed
	 */
	public QuickAction(View anchor) {
		super(anchor);

		actionList = new ArrayList<ActionItem>();
		Context context = anchor.getContext();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		root = inflater.inflate(R.layout.quickaction, null);
		mArrowDown = (ImageView) root.findViewById(R.id.arrow_down);
		mArrowUp = (ImageView) root.findViewById(R.id.arrow_up);
		setContentView(root);
		mTrackAnim = AnimationUtils.loadAnimation(anchor.getContext(), R.anim.rail);

		mTrackAnim.setInterpolator(new Interpolator() {
			public float getInterpolation(float t) {
				// Pushes past the target area, then snaps back into place.
				// Equation for graphing: 1.2-((x*1.6)-1.1)^2
				final float inner = (t * 1.55f) - 1.1f;
				return 1.2f - inner * inner;
			}
		});

		mTrack = (ViewGroup) root.findViewById(R.id.tracks);
		animateTrack = true;
	}

	/**
	 * Animate track
	 * @param animateTrack flag to animate track
	 */
	public void animateTrack(boolean animateTrack) {
		this.animateTrack = animateTrack;
	}

	/**
	 * Add action item
	 * @param action {@link ru.megazlo.quicker.ActionItem}
	 */
	public void addAction(ActionItem action) {
		action.setQuickAction(this);
		actionList.add(action);
	}

	/** Show popup window */
	public void show() {
		preShow();
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1]
				+ anchor.getHeight());
		root.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		root.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//int rootWidth = root.getMeasuredWidth();
		int rootHeight = root.getMeasuredHeight();
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		// int screenHeight = windowManager.getDefaultDisplay().getHeight();

		//int xPos = (screenWidth - rootWidth) / 2;
		int yPos = anchorRect.top - rootHeight;
		boolean onTop = true;
		// display on bottom
		if (rootHeight > anchorRect.top) {
			yPos = anchorRect.bottom;
			onTop = false;
		}
		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), anchorRect.centerX());
		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
		createActionList();
		window.showAtLocation(this.anchor, Gravity.NO_GRAVITY, anchorRect.left, yPos);
		if (animateTrack)
			mTrack.startAnimation(mTrackAnim);
	}

	/**
	 * Set animation style
	 * @param screenWidth Screen width
	 * @param requestedX  distance from left screen
	 * @param onTop       flag to indicate where the popup should be displayed. Set TRUE if
	 *                    displayed on top of anchor and vice versa
	 */
	private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {
		int arrowPos = requestedX - mArrowUp.getMeasuredWidth() / 2;
		int anim;
		if (arrowPos <= screenWidth / 4) {
			anim = onTop ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left;
		} else if (arrowPos > screenWidth / 4 && arrowPos < 3 * (screenWidth / 4)) {
			anim = onTop ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center;
		} else {
			anim = onTop ? R.style.Animations_PopDownMenu_Right : R.style.Animations_PopDownMenu_Right;
		}
		window.setAnimationStyle(anim);
	}

	/** Create action list */
	private void createActionList() {
		for (int i = 0; i < actionList.size(); i++) {
			ActionItem itm = actionList.get(i);
			View view = getActionItem(itm.getTitle(), itm.getIcon(), itm.getListener());
			view.setFocusable(true);
			view.setClickable(true);
			mTrack.addView(view, i + 1);
		}
	}

	/**
	 * Get action item {@link android.view.View}
	 * @param title    action item title
	 * @param icon     {@link android.graphics.drawable.Drawable} action item icon
	 * @param listener {@link android.view.View.OnClickListener} action item listener
	 * @return action item {@link android.view.View}
	 */
	private View getActionItem(String title, Drawable icon, OnClickListener listener) {
		LinearLayout container = (LinearLayout) inflater.inflate(R.layout.action_item, null);
		ImageView img = (ImageView) container.findViewById(R.id.icon);
		TextView text = (TextView) container.findViewById(R.id.title);

		if (icon != null)
			img.setImageDrawable(icon);
		else
			img.setVisibility(View.GONE);

		if (title != null)
			text.setText(title);
		else
			text.setVisibility(View.GONE);

		if (listener != null)
			container.setOnClickListener(listener);

		return container;
	}

	/**
	 * Show arrow
	 * @param whichArrow arrow type resource id
	 * @param requestedX distance from left screen
	 */
	private void showArrow(int whichArrow, int requestedX) {
		final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp : mArrowDown;
		final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown : mArrowUp;

		final int arrowWidth = mArrowUp.getMeasuredWidth();
		showArrow.setVisibility(View.VISIBLE);
		ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow.getLayoutParams();
		param.leftMargin = /*requestedX - */arrowWidth / 2; /*requestedX - arrowWidth / 2;*/
		hideArrow.setVisibility(View.INVISIBLE);
	}
}