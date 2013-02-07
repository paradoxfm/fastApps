package ru.zlo.ff.util.file;

import android.app.AlertDialog;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;
import ru.zlo.ff.NAct;
import ru.zlo.ff.R;
import ru.zlo.ff.components.filerow.FileRowData;
import ru.zlo.ff.engine.BaseEngine;
import ru.zlo.ff.engine.EngPool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileTools {
	public static int OPERATION;
	public static String KEY = "file";
	public static Object[] FROM;
	public static Object TO;
	public static String SEARCH;
	public static MediaPlayer M_PLAYER = new MediaPlayer();
	private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
	private static final BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();
	public static String CURRENT_PLAY = "";
	private static MimeTypes TYPE = new MimeTypes();

	private static EditText EDIT;

	private static OnCancelListener cans = new OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			M_PLAYER.stop();
		}
	};

	private static OnClickListener newFld = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (EDIT.getText().length() == 0)
				return;
			if (TO.getClass() == File.class) {
				File newdir = new File((File) TO, EDIT.getText().toString());
				newdir.mkdir();
			}
			EngPool.Inst().getCurrent().update();
		}
	};

	private static OnClickListener renm = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (EDIT.getText().length() == 0)
				return;
			if (TO.getClass() == File.class) {
				File fl = (File) TO;
				String path = fl.getPath().substring(0, fl.getPath().lastIndexOf('/') + 1) + EDIT.getText();
				fl.renameTo(new File(path));
			}
			EngPool.Inst().getCurrent().update();
		}
	};

	private static OnClickListener newFile = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (EDIT.getText().length() == 0)
				return;
			String name = EDIT.getText().toString();
			if (!name.toLowerCase().endsWith(".txt"))
				name += ".txt";
			File newfile = new File((File) TO, name);
			try {
				newfile.createNewFile();
				EngPool.Inst().getCurrent().update();
			} catch (IOException ignored) {
			}
		}
	};

	private static OnClickListener delet = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Context c = ((AlertDialog) dialog).getContext();
			new Delete(c).execute();
		}
	};

	public static void newFolder(Context context) {
		showDialog(context, newFld, R.string.name, R.string.new_folder, TO, true, R.drawable.i_fold);
	}

	public static void rename(Context context) {
		showDialog(context, renm, R.string.name, R.string.rename, null, true, R.drawable.qa_rename);
		if (TO.getClass() == File.class)
			EDIT.setText(((File) TO).getName());
	}

	public static void newFile(Context context) {
		showDialog(context, newFile, R.string.name, R.string.new_file, null, true, R.drawable.qa_new_file);
	}

	public static void openFileExt(Context c, File file) {
		if (!file.exists()) {
			Toast.makeText(c, R.string.file_n_exist, Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
		Uri data = Uri.fromFile(file);
		String type = TYPE.getMimeType(file.getName());
		intent.setDataAndType(data, type);
		try {
			c.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(c, R.string.app_n_found, Toast.LENGTH_SHORT).show();
		}
	}

	public static void openFileThis(Context c, File file) {
		String path = file.getPath().toLowerCase();
		final int tp = BaseEngine.getType(path.toLowerCase());
		if (tp == FileRowData.TP_MUSIC)
			playMusic(c, file);
		else if (tp == FileRowData.TP_BITMAP)
			veiwImage(c, file);
		else if (path.endsWith(".txt") || path.endsWith(".log")) {
			if (file.length() > 1024 * 1024 * 100) {
				Toast.makeText(c, R.string.txt_preview_err, Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intn = new Intent();
			intn.setClass(c, NAct.class);
			intn.putExtra(KEY, file.getPath());
			c.startActivity(intn);
		} else
			openFileExt(c, file);
	}

	private static void veiwImage(Context cont, File file) {
		DisplayMetrics dm = cont.getResources().getDisplayMetrics();
		final int minrez = Math.min(dm.widthPixels, dm.heightPixels);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.outHeight = options.outWidth = 0;
		options.inSampleSize = 1;
		String path = file.getAbsolutePath();
		BitmapFactory.decodeFile(path, options);
		if (options.outWidth > 0 && options.outHeight > 0) {
			// Now see how much we need to scale it down.
			int widthFactor = (options.outWidth + minrez - 1) / minrez;
			widthFactor = Math.max(widthFactor, (options.outHeight + minrez - 1) / minrez);
			widthFactor = Math.max(widthFactor, 1);
			options.inSampleSize = widthFactor;
			options.inJustDecodeBounds = false;
			ImageView img = new ImageView(cont);
			img.setImageBitmap(BitmapFactory.decodeFile(path, options));
			img.setPadding(10, 10, 10, 10);
			new AlertDialog.Builder(cont).setTitle(R.string.mus_preview).setIcon(R.drawable.i_file_img).setView(img).create()
					.show();
		}
	}

	private static void playMusic(Context cont, File file) {
		if (M_PLAYER.isPlaying()) {
			M_PLAYER.stop();
			if (CURRENT_PLAY == file.getPath())
				return;
		}
		M_PLAYER = MediaPlayer.create(cont, Uri.fromFile(file));
		CURRENT_PLAY = file.getPath();

		String whereclause = cont.getString(R.string.where_cau);
		whereclause = String.format(whereclause, MediaStore.Audio.Media.DATA, file.getAbsolutePath());
		Cursor cursor = cont.getContentResolver().query(
				MediaStore.Audio.Media.getContentUri("external"),
				new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media.ALBUM_ID}, whereclause, null, null);
		String title = "", album = "", artist = "";
		int albumId = -1;
		if (cursor != null && cursor.moveToFirst()) {
			title = cursor.getString(0);
			album = cursor.getString(1);
			artist = cursor.getString(2);
			albumId = Integer.parseInt(cursor.getString(3));
		}
		cursor.close();
		LayoutInflater factory = LayoutInflater.from(cont);
		final View formcon = factory.inflate(R.layout.plmusic, null);
		TextView tx = (TextView) formcon.findViewById(R.id.mp3info);
		ImageView img = (ImageView) formcon.findViewById(R.id.mp3cover);
		final SeekBar bar = (SeekBar) formcon.findViewById(R.id.mSeek);

		bar.setMax(M_PLAYER.getDuration());

		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser)
					M_PLAYER.seekTo(progress);
			}
		});

		Bitmap cov = getArtworkQuick(cont, albumId, 200, 200);
		if (cov != null)
			img.setImageBitmap(cov);
		String med = cont.getString(R.string.media_info);
		tx.setText(String.format(med, artist, title, album));
		new AlertDialog.Builder(cont).setOnCancelListener(cans).setTitle(R.string.mus_preview)
				.setIcon(R.drawable.i_file_mus).setView(formcon).create().show();

		Runnable qww = new Runnable() {
			@Override
			public void run() {
				int currentPosition = 0;
				int total = M_PLAYER.getDuration();
				bar.setMax(total);
				while (M_PLAYER != null && currentPosition < total) {
					try {
						Thread.sleep(1000);
						currentPosition = M_PLAYER.getCurrentPosition();
					} catch (Exception e) {
						return;
					}
					bar.setProgress(currentPosition);
				}
			}
		};

		M_PLAYER.start();
		Thread thr = new Thread(qww);
		thr.start();
	}

	public static Bitmap getArtworkQuick(Context context, long album_id, int w, int h) {
		// NOTE: There is in fact a 1 pixel border on the right side in the
		// ImageView
		// used to display this drawable. Take it into account now, so we don't have
		// to
		// scale later.
		w -= 1;
		ContentResolver res = context.getContentResolver();
		Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
		if (uri != null) {
			ParcelFileDescriptor fd = null;
			try {
				fd = res.openFileDescriptor(uri, "r");
				int sampleSize = 1;

				// Compute the closest power-of-two scale factor
				// and pass that to sBitmapOptionsCache.inSampleSize, which will
				// result in faster decoding and better quality
				sBitmapOptionsCache.inJustDecodeBounds = true;
				BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, sBitmapOptionsCache);
				int nextWidth = sBitmapOptionsCache.outWidth >> 1;
				int nextHeight = sBitmapOptionsCache.outHeight >> 1;
				while (nextWidth > w && nextHeight > h) {
					sampleSize <<= 1;
					nextWidth >>= 1;
					nextHeight >>= 1;
				}

				sBitmapOptionsCache.inSampleSize = sampleSize;
				sBitmapOptionsCache.inJustDecodeBounds = false;
				Bitmap b = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, sBitmapOptionsCache);

				if (b != null) {
					// finally rescale to exactly the size we need
					if (sBitmapOptionsCache.outWidth != w || sBitmapOptionsCache.outHeight != h) {
						Bitmap tmp = Bitmap.createScaledBitmap(b, w, h, true);
						// Bitmap.createScaledBitmap() can return the same bitmap
						if (tmp != b)
							b.recycle();
						b = tmp;
					}
				}

				return b;
			} catch (FileNotFoundException ignored) {
			} finally {
				try {
					if (fd != null)
						fd.close();
				} catch (IOException ignored) {
				}
			}
		}
		return null;
	}

	public static void delete(Context context) {
		showDialog(context, delet, R.string.aredelete, R.string.delete, null, false, R.drawable.qa_delete);
	}

	public static void showDialog(Context context, OnClickListener lis, int msgid, int titleid, Object fil, Boolean showEdit, int ico) {
		EDIT = new EditText(context);
		EDIT.setPadding(15, 15, 15, 15);
		String msg = context.getString(msgid);
		String title = context.getString(titleid);
		if (showEdit)
			new AlertDialog.Builder(context).setPositiveButton(R.string.ok, lis).setNegativeButton(R.string.cansel, null)
					.setMessage(msg).setIcon(ico).setTitle(title).setView(EDIT).show();
		else
			new AlertDialog.Builder(context).setPositiveButton(R.string.ok, lis).setNegativeButton(R.string.cansel, null)
					.setMessage(msg).setIcon(ico).setTitle(title).show();
	}

	static void rename(File fileFrom, String fileTo) {
		String path = fileFrom.getPath().replace(fileFrom.getName(), "");
		File newfl = new File(path + fileTo);
		fileFrom.renameTo(newfl);
	}

}
