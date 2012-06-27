package ru.megazlo.ffng.components.filerow;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class FilePagerAdapter extends FragmentPagerAdapter {

	private ArrayList<FileListFragment> frags;

	public FilePagerAdapter(FragmentManager fm) {
		super(fm);
		frags = new ArrayList<FileListFragment>();
		frags.add(new FileListFragment());
		frags.add(new FileListFragment());
	}

	@Override
	public Fragment getItem(int index) {
		return frags.get(index);
	}

	@Override
	public int getCount() {
		return frags.size();
	}

	public void addFileListFragment(FileListFragment fr) {
		frags.add(fr);
	}

	public void removeFileListFragment(FileListFragment fr) {
		frags.remove(fr);
	}

	public void removeFileListFragment(int index) {
		frags.remove(index);
	}

}
