package ru.zlo.ff.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import ru.zlo.ff.R;
import ru.zlo.ff.engine.EngPool;

import java.util.ArrayList;
import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	FragmentManager manager;
	List<Fragment> arr = new ArrayList<Fragment>(2);

	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
		manager = fm;
	}

	@Override
	public Fragment getItem(int i) {
		if (i + 1 > arr.size()) {
			Fragment fragment = FileListFragment_.builder().build();
			Bundle args = new Bundle();
			args.putInt(FileListFragment.ENG_NUM, i);
			fragment.setArguments(args);
			arr.add(fragment);
		}
		return arr.get(i);
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
