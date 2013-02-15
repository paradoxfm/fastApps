package ru.zlo.fn.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	List<Fragment> arr = new ArrayList<Fragment>(2);

	public SectionsPagerAdapter(FragmentManager fm, Fragment... frags) {
		super(fm);
		Collections.addAll(arr, frags);
	}

	@Override
	public Fragment getItem(int i) {
		return arr.get(i);
	}

	@Override
	public int getCount() {
		return arr.size();
	}
}
