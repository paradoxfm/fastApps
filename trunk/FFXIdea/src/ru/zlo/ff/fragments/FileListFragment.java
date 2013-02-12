package ru.zlo.ff.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import ru.zlo.ff.R;
import ru.zlo.ff.components.filerow.FileRowAdapter;
import ru.zlo.ff.components.filerow.FileRowData;
import ru.zlo.ff.engine.BaseEngine;
import ru.zlo.ff.engine.EngPool;
import ru.zlo.ff.util.Options;
import ru.zlo.ff.util.file.FileTools;

import java.io.File;
import java.util.List;

@EFragment
public class FileListFragment extends ListFragment implements BaseEngine.OnLoadFinish, BaseEngine.OnDataChanged {

	@Bean
	protected EngPool engines;
	protected BaseEngine engine;
	protected FileRowAdapter adapter = new FileRowAdapter();
	protected OnEngineActivator engineActivator;
	OnEngineBrowse onEngineBrowse;
	private static int current = -1;

	public interface OnEngineActivator {
		void activateEngine(BaseEngine engine);
	}

	public interface OnEngineBrowse {
		void engineBrowse(BaseEngine engine);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		//registerForContextMenu(getListView());
		getListView().setLayoutAnimation(Options.LIST_ANIM);
		if (current == 1)
			current = -1;
		current++;
		engine = engines.getEngine(current);
		engine.setContext(getListView().getContext());
		setListAdapter(adapter);
		engine.setOnLoadFinish(this);
		engine.setOnDataChanger(this);
		engine.update();
		getListView().setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (engineActivator != null)
					engineActivator.activateEngine(engine);
				return false;
			}
		});
	}

	public void setOnEngineActivator(OnEngineActivator engineActivator) {
		this.engineActivator = engineActivator;
	}

	public void setOnEngineBrowse(OnEngineBrowse onEngineBrowse) {
		this.onEngineBrowse = onEngineBrowse;
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
	public void onLoadFinish(List<FileRowData> dataRows) {
		adapter.setListItems(dataRows);
		adapter.notifyDataSetChanged();
		if (engine.scrollPoz > 0 && getListView().getChildCount() > 0)
			getListView().setSelectionFromTop(engine.scrollPoz, getListView().getChildAt(0).getHeight() / 2);
		if (Options.ANIMATE)
			getListView().startLayoutAnimation();
		if (onEngineBrowse != null)
			onEngineBrowse.engineBrowse(engine);
	}

	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.context_file, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		/*Object tst = FileRow.this.dat.getFile();
		if (tst.getClass() == File.class) {
			File fl = (File) tst;
			if ((fl.canWrite() && fl.isFile()) || (fl.isDirectory()))
				ActionFactory.create(v, fl).show();
		}*/
		/*AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
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
