package ru.zlo.ff.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import ru.zlo.ff.R;
import ru.zlo.ff.components.filerow.FileRowAdapter;
import ru.zlo.ff.engine.BaseEngine;
import ru.zlo.ff.engine.EngPool;
import ru.zlo.ff.util.Options;
import ru.zlo.ff.util.file.FileTools;

import java.io.File;

@EFragment
public class FileListFragment extends ListFragment implements BaseEngine.OnLoadFinish, BaseEngine.OnDataChanged {
	public static final String ENG_NUM = "ENG_NUM";

	private BaseEngine engine;
	private FileRowAdapter adapter = new FileRowAdapter();

	@AfterViews
	protected void initData() {
		setListAdapter(adapter);
		int num = getArguments().getInt(ENG_NUM);
		engine = EngPool.Inst().getEngine(num);
		engine.setOnScrollFinish(this);
		engine.setOnDataChanger(this);
		engine.update();
		adapter.setListItems(engine.getDat().dir);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		registerForContextMenu(getListView());
		getListView().setLayoutAnimation(Options.LIST_ANIM);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		File curFile = engine.getDat().dir.get(position).getFile();
		if (curFile.isDirectory()) {
			engine.browseCatalog(curFile);
		} else if (Options.OPEN_THIS)
			FileTools.openFileThis(getListView().getContext(), curFile);
		else if (!Options.OPEN_THIS)
			FileTools.openFileExt(getListView().getContext(), curFile);
		l.setSelection(0);
	}

	@Override
	public void onChange() {
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onFinish() {
		adapter.setListItems(engine.getDat().dir);
		adapter.notifyDataSetChanged();
		if (engine.scrollPoz > 0 && getListView().getChildCount() > 0)
			getListView().setSelectionFromTop(engine.scrollPoz, getListView().getChildAt(0).getHeight() / 2);
		if (Options.ANIMATE)
			getListView().startLayoutAnimation();
		engine.startLoadImage();
	}

	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.context_file, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		/*switch (item.getItemId()) {
			case R.id.edit:
				editNote(info.id);
				return true;
			case R.id.delete:
				deleteNote(info.id);
				return true;
			default:
				return super.onContextItemSelected(item);
		}*/
		return super.onContextItemSelected(item);
	}
}
