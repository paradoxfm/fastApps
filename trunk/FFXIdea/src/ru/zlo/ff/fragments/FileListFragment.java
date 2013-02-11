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
public class FileListFragment extends ListFragment implements BaseEngine.OnLoadFinish {
	public static final String ENG_NUM = "ENG_NUM";
	private static int current = -1;

	@Bean
	protected EngPool engines;
	private BaseEngine engine;
	private FileRowAdapter adapter = new FileRowAdapter();
	private OnEngineActivator engineActivator;

	public interface OnEngineActivator {
		void activateEngine(BaseEngine engine);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		registerForContextMenu(getListView());
		getListView().setLayoutAnimation(Options.LIST_ANIM);
		if (current == 1)
			current = -1;
		current++;
		engine = engines.getEngine(current);
		setListAdapter(adapter);
		engine.setOnLoadFinish(this);
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
	public void onLoadFinish(List<FileRowData> dataRows) {
		adapter.setListItems(dataRows);
		adapter.notifyDataSetChanged();
		if (engine.scrollPoz > 0 && getListView().getChildCount() > 0)
			getListView().setSelectionFromTop(engine.scrollPoz, getListView().getChildAt(0).getHeight() / 2);
		if (Options.ANIMATE)
			getListView().startLayoutAnimation();
		if (engineActivator != null)
			engineActivator.activateEngine(engine);
	}

	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.context_file, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
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
