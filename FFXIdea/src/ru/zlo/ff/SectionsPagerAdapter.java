package ru.zlo.ff;

import ru.zlo.ff.engine.EngPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new FileSectionFragment();
		Bundle args = new Bundle();
		args.putInt(FileSectionFragment.ENG_NUM, i);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public int getCount() {
		return EngPool.Inst().count();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "\\" + EngPool.Inst().getEngine(position).getTitle();
	}

}