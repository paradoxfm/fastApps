package ru.megazlo.ffng.components.filerow;

import ru.megazlo.ffng.components.RowDataSD;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FileListFragment extends ListFragment {

	static FileListFragment newInstance(int num) {
		FileListFragment f = new FileListFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);
		return f;
	}

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mNum = getArguments() != null ? getArguments().getInt("num") : 1;
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		FileList lv = new FileList(container.getContext(), new RowDataSD(), false);
		container.addView(lv);
		return lv;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// FileRowAdapter rAdp = new FileRowAdapter();
		// setListAdapter(rAdp);
		// setListAdapter(new ArrayAdapter<String>(getActivity(),
		// android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

	}
}
