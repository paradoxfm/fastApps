package ru.zlo.ff.util;

import java.io.File;

import ru.zlo.ff.R;
import ru.zlo.ff.MAct;
import ru.megazlo.quicker.ActionItem;
import ru.megazlo.quicker.QuickAction;
import ru.zlo.ff.engine.BaseEngine;
import ru.zlo.ff.util.file.FileTools;
import ru.zlo.ff.util.file.MimeTypes;
import ru.zlo.ff.util.file.Paste;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ActionFactory {

	public static final int NONE = -1;
	public static final int MOVE = 1;
	public static final int COPY = 2;

	public static QuickAction create(View v, Object... file) {
		QuickAction qa = new QuickAction(v);
		BaseEngine eng = MAct.I.getCurEng();
		Object[] obj = eng.getFiles();
		if (obj.length > 0)
			createMulti(v.getContext(), qa, obj);
		else if (file[0].getClass() == File.class)
			createLocal(v.getContext(), qa, file[0]);
		return qa;
	}

	private static void createMulti(Context cont, QuickAction qq, Object... fil) {
		qq.addAction(copy_move(cont, false, fil));// копировать
		qq.addAction(copy_move(cont, true, fil));// переместить
		qq.addAction(delete(cont, fil));// удалить
		if (fil.getClass() == File[].class)
			qq.addAction(searchInFilder(cont, fil));// поиск
	}

	private static void createLocal(Context cont, QuickAction qq, Object fil) {
		File fl = (File) fil;
		if ((fl.isFile() && fl.getParentFile().canWrite()) || (fl.isDirectory() && fl.canWrite())) {
			qq.addAction(newFile(cont, fl));// создать
			qq.addAction(copy_move(cont, false, fil));// копировать
			qq.addAction(copy_move(cont, true, fil));// переместить
			if (FileTools.FROM != null && fl.isDirectory())
				qq.addAction(paste(cont, fil));// вставить
			if (fl.isDirectory())
				qq.addAction(newDirectory(cont, fil));// создать папку
			qq.addAction(delete(cont, fil));// удалить
			qq.addAction(reaname(cont, fil));// переименовать
		}
		if (fl.isDirectory() && fl.canRead())
			qq.addAction(searchInFilder(cont, fil));// поиск
		if (fl.isFile() && fl.canRead())
			qq.addAction(bluetooth(cont, fl));// отправка
	}


	private static ActionItem copy_move(Context context, final Boolean isMove, final Object... fil) {
		final ActionItem copy_move = new ActionItem();
		copy_move.setIcon(context.getResources().getDrawable(isMove ? R.drawable.qa_cut : R.drawable.qa_copy));
		copy_move.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileTools.FROM = fil;
				FileTools.OPERATION = isMove ? ActionFactory.MOVE : ActionFactory.COPY;
				Toast.makeText(v.getContext(), R.string.show_place, Toast.LENGTH_SHORT).show();
				copy_move.dismiss();
			}
		});
		return copy_move;
	}

	private static ActionItem paste(Context context, final Object fil) {
		final ActionItem paste = new ActionItem();
		paste.setIcon(context.getResources().getDrawable(R.drawable.qa_paste));
		paste.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileTools.TO = fil;
				new Paste().execute();
				paste.dismiss();
			}
		});
		return paste;
	}

	private static ActionItem delete(Context context, final  Object... fil) {
		final ActionItem delet = new ActionItem();
		delet.setIcon(context.getResources().getDrawable(R.drawable.qa_delete));
		delet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileTools.FROM = fil;
				FileTools.delete();
				delet.dismiss();
			}
		});
		return delet;
	}

	private static ActionItem reaname(Context context, final Object fil) {
		final ActionItem reanam = new ActionItem();
		reanam.setIcon(context.getResources().getDrawable(R.drawable.qa_rename));
		reanam.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileTools.TO = fil;
				FileTools.rename();
				reanam.dismiss();
			}
		});
		return reanam;
	}

	private static ActionItem searchInFilder(Context context, final Object... fil) {
		final ActionItem search = new ActionItem();
		search.setIcon(context.getResources().getDrawable(R.drawable.qa_searsh));
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileTools.FROM = fil.length == 1 ? new File[] { (File) fil[0] } : fil;
				MAct.I.onSearchRequested();
				search.dismiss();
			}
		});
		return search;
	}

	private static ActionItem newFile(Context context, final File fil) {
		final ActionItem creat = new ActionItem();
		creat.setIcon(context.getResources().getDrawable(R.drawable.qa_new_file));
		creat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileTools.TO = fil.isFile() ? fil.getParentFile() : fil;
				FileTools.newFile();
				creat.dismiss();
			}
		});
		return creat;
	}

	private static ActionItem newDirectory(Context context, final Object fil) {
		final ActionItem creat = new ActionItem();
		creat.setIcon(context.getResources().getDrawable(R.drawable.qa_new_folder));
		creat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileTools.TO = fil;
				FileTools.newFolder();
				creat.dismiss();
			}
		});
		return creat;
	}

	private static ActionItem bluetooth(final Context c, final File fl) {
		final ActionItem blt = new ActionItem();
		blt.setIcon(c.getResources().getDrawable(R.drawable.qa_bluetooth));
		blt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent sender = new Intent(Intent.ACTION_SEND);
				MimeTypes mme = new MimeTypes();
				sender.setType(mme.getMimeType(fl.getName()));
				sender.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fl));
				v.getContext().startActivity(Intent.createChooser(sender, v.getResources().getString(R.string.snd_file)));
				blt.dismiss();
			}
		});
		return blt;
	}

}
