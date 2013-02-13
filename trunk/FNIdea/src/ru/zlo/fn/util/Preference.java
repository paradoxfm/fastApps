package ru.zlo.fn.util;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultInt;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Preference {
	@DefaultBoolean(false)
	boolean isFullScreen();

	@DefaultInt(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
	int orientation();

	@DefaultInt(22)
	int fontSize();

	@DefaultInt(Color.BLACK)
	int fontColor();

	@DefaultInt(0x800000FF)
	int lineColor();
}