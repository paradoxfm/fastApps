package ru.megazlo.ledxremote.util;

import ru.megazlo.ledxremote.R;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SeekEvent implements OnSeekBarChangeListener {
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		int id = seekBar.getId();
		if (id == R.id.sb_speed)
			Util.sendSpeed(progress);
		else if (id == R.id.sb_brigh)
			Util.sendBrightness(progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

}
