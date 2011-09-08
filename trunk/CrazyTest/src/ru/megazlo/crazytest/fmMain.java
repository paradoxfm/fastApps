package ru.megazlo.crazytest;

import ru.megazlo.crazytest.components.NotifyList;
import android.app.Activity;
import android.os.Bundle;

public class fmMain extends Activity {
	private NotifyList lst;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(lst);
	}
}
