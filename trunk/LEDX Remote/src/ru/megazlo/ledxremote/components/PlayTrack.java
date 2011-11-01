package ru.megazlo.ledxremote.components;

import ru.megazlo.ledxremote.R;
import ru.megazlo.ledxremote.util.Util;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class PlayTrack extends ImageButton {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	// public static final int PLAY = 1;
	// public static final int PAUSE = 2;

	private boolean is_viewed = false;
	private int state = INACTIVE;
	private Drawable def;

	public PlayTrack(Context context) {
		super(context);
	}

	public PlayTrack(Context context, AttributeSet attrs) {
		super(context, attrs);
		setAdjustViewBounds(true);
		def = getDrawable();
		setStatePlay(INACTIVE);
	}

	@Override
	public boolean performClick() {
		if (Util.isErr())
			return false;
		if (state == PlayTrack.INACTIVE) {
			setStatePlay();
			setImageState();
		}
		return super.performClick();
	}

	private void setStatePlay() {
		switch (state) {
		case PlayTrack.ACTIVE:
			state = PlayTrack.INACTIVE;
			break;
		case PlayTrack.INACTIVE:
			state = PlayTrack.ACTIVE;
			break;
		default:
			state = PlayTrack.INACTIVE;
			break;
		}
	}

	public void setStatePlay(int stt) {
		if (stt == PlayTrack.INACTIVE)
			is_viewed = false;
		state = stt;
		setImageState();
	}

	public boolean isViewed() {
		return is_viewed;
	}

	public void setViewed(boolean val) {
		is_viewed = val;
	}

	public int getStatePlay() {
		return state;
	}

	private void setImageState() {
		switch (state) {
		case PlayTrack.INACTIVE:
			setImageDrawable(def);
			break;
		case PlayTrack.ACTIVE:
			setImageResource(R.drawable.pl_ps);
			break;
		default:
			setImageDrawable(def);
			break;
		}
	}
}
