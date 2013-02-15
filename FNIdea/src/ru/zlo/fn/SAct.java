package ru.zlo.fn;

import android.preference.PreferenceActivity;
import android.view.WindowManager;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import ru.zlo.fn.util.Options_;

import java.util.List;

@EActivity
public class SAct extends PreferenceActivity {

	@Pref
	Options_ opt;

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}

	@Override
	protected void onResume() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		int flg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (opt.fullScreen().get())
			getWindow().setFlags(flg, flg);
		else
			getWindow().clearFlags(flg);
		setRequestedOrientation(opt.orientation().get());
		super.onResume();
	}

	@OptionsItem(android.R.id.home)
	public void homePress() {
		onBackPressed();
	}
}
