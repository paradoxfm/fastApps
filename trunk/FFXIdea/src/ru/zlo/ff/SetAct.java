package ru.zlo.ff;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.WindowManager;
import ru.zlo.ff.util.Options;

import java.util.List;

public class SetAct extends PreferenceActivity {

	public static SetAct I;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (Options.FULL_SCR)
			getWindow().setFlags(flg, flg);
		else
			getWindow().clearFlags(flg);
		setRequestedOrientation(Options.ORIENT_TYPE);
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}

	@Override
	public void onBackPressed() {
		Options.save(this.getApplicationContext());
		super.onBackPressed();
	}
}
