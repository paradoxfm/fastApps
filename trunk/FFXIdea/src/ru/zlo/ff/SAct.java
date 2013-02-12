package ru.zlo.ff;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.WindowManager;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import ru.zlo.ff.util.Options;

import java.util.List;

@EActivity
public class SAct extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
	@OptionsItem(android.R.id.home)
	public void onBackPressed() {
		Options.save(this.getApplicationContext());
		super.onBackPressed();
	}
}
