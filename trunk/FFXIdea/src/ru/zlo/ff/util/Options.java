package ru.zlo.ff.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.view.animation.*;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.res.DrawableRes;
import com.googlecode.androidannotations.annotations.res.StringArrayRes;
import com.googlecode.androidannotations.api.Scope;

import java.io.File;

@EBean(scope = Scope.Singleton)
public class Options {
	private final static String name = "fastfile";
	public static final String dbPath = "dbase.db";

	@RootContext
	Context context;

	public static Boolean ISGRID;
	public static Boolean OPEN_THIS;// open ext
	public static Boolean SHOW_HIDDEN;// hidden
	public static Boolean SHOW_IMG;// show images preview
	public static Boolean SHOW_MP3;// show mp3 cover preview
	public static Boolean SHOW_APK;// show apk preview
	public static File HOME_PATH;// homedir

	public static Boolean FULL_SCR;// fullscr
	public static Boolean ANIMATE;// animate
	public static Integer ORIENT_TYPE;// orientype
	public static Integer TXT_CLR;// text color

	// -------------------------------------------
	public static LayoutAnimationController LIST_ANIM;
	@DrawableRes
	public static Drawable i_fold, i_chk, i_unchk;
	@DrawableRes
	public static Drawable i_file_doc, i_file_img, i_file_mov, i_file_mus, i_file_non, i_file_bin;
	@StringArrayRes
	public static String[] docs, imgs, vids, muss, bin;
	public SQLiteDatabase SQLITE;
	public static java.text.DateFormat F_TIME;
	public static java.text.DateFormat F_DATE;

	@AfterInject
	public void loadOptions() {
		SharedPreferences p = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		/*if (dat.size() != 0) {
			applySets(res);
			return;
		}*/
		F_DATE = DateFormat.getDateFormat(context);
		F_TIME = DateFormat.getTimeFormat(context);
		ISGRID = p.getBoolean("ISGRID", false);
		OPEN_THIS = p.getBoolean("OPEN_THIS", false);
		HOME_PATH = new File(p.getString("HOME_PATH", "/"));
		if (!HOME_PATH.exists() || !HOME_PATH.canRead())
			HOME_PATH = new File("/");

		SHOW_HIDDEN = p.getBoolean("SHOW_HIDDEN", false);
		SHOW_IMG = p.getBoolean("SHOW_IMG", true);
		SHOW_MP3 = p.getBoolean("SHOW_MP3", true);
		SHOW_APK = p.getBoolean("SHOW_APK", true);

		FULL_SCR = p.getBoolean("FULL_SCR", false);
		ANIMATE = p.getBoolean("ANIMATE", false);
		ORIENT_TYPE = p.getInt("ORIENT_TYPE", ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		TXT_CLR = p.getInt("TXT_CLR", -4276546);
		processRes();
	}

	private void processRes() {
		LIST_ANIM = createAnim();
		int i;
		for (i = 0; i < docs.length; i++)
			docs[i] = docs[i].intern();
		for (i = 0; i < imgs.length; i++)
			imgs[i] = imgs[i].intern();
		for (i = 0; i < muss.length; i++)
			muss[i] = muss[i].intern();
		for (i = 0; i < bin.length; i++)
			bin[i] = bin[i].intern();
		for (i = 0; i < vids.length; i++)
			vids[i] = vids[i].intern();
	}

	public static void save(Context context) {
		SharedPreferences p = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = p.edit();
		edit.putBoolean("ISGRID", ISGRID);
		edit.putBoolean("OPEN_THIS", OPEN_THIS);
		edit.putString("HOME_PATH", HOME_PATH.getPath());
		edit.putBoolean("SHOW_HIDDEN", SHOW_HIDDEN);
		edit.putBoolean("SHOW_IMG", SHOW_IMG);
		edit.putBoolean("SHOW_MP3", SHOW_MP3);
		edit.putBoolean("SHOW_APK", SHOW_APK);
		edit.putBoolean("FULL_SCR", FULL_SCR);
		edit.putBoolean("ANIMATE", ANIMATE);
		edit.putInt("ORIENT_TYPE", ORIENT_TYPE);
		edit.commit();
	}

	private static LayoutAnimationController createAnim() {
		AnimationSet set = new AnimationSet(true);
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(50);
		set.addAnimation(animation);
		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(100);
		set.addAnimation(animation);
		// return AnimationUtils.loadLayoutAnimation(act, R.anim.layanim);
		return new LayoutAnimationController(set, 0.5f);
	}
}
