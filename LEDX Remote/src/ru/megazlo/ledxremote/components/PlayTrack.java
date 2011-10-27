package ru.megazlo.ledxremote.components;

import ru.megazlo.ledxremote.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class PlayTrack extends ImageButton {
	public static final int INACTIVE = 0;
	public static final int PLAY = 1;
	public static final int PAUSE = 2;

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
		setStatePlay();
		setImageState();
		return super.performClick();
	}

	private void setStatePlay() {
		switch (state) {
		case PlayTrack.PLAY:
			state = PlayTrack.PAUSE;
			break;
		case PlayTrack.PAUSE:
			state = PlayTrack.PLAY;
			break;
		default:
			state = PlayTrack.PLAY;
			break;
		}
	}

	public void setStatePlay(int stt) {
		state = stt;
		setImageState();
	}

	public int getStatePlay() {
		return state;
	}

	private void setImageState() {
		switch (state) {
		case PlayTrack.PLAY:
			setImageResource(R.drawable.pause);
			break;
		case PlayTrack.PAUSE:
			setImageResource(R.drawable.play);
			break;
		default:
			setImageDrawable(def);
			break;
		}
	}
}
