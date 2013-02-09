package ru.zlo.ff.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import ru.zlo.ff.engine.EngPool;

import java.util.ArrayList;
import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	List<Fragment> arr = new ArrayList<Fragment>(2);

	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		if (i + 1 > arr.size())
			arr.add(FileListFragment_.builder().build());
		return arr.get(i);
	}

	@Override
	public int getCount() {
		return EngPool.Inst().count();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return EngPool.Inst().getEngine(position).getTitle();
	}
}
