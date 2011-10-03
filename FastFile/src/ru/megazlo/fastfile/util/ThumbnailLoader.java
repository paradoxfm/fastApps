package ru.megazlo.fastfile.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import ru.megazlo.fastfile.R;
import ru.megazlo.fastfile.fmMain;
import ru.megazlo.fastfile.components.filerow.FileRowData;
import ru.megazlo.fastfile.util.file.MimeTypes;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

public class ThumbnailLoader extends Thread {

	public boolean cancel;
	private List<FileRowData> listFile;
	private Handler handler;
	private Context context;
	private MimeTypes mMimeTypes;
	private BitmapFactory.Options options = new BitmapFactory.Options();
	private static int thumbnailHeight = 64, thumbnailWidth = 64;

	public ThumbnailLoader(List<FileRowData> dirEntr, Handler handler, Context context, MimeTypes mimetypes) {
		super("Thumbnail Loader");
		listFile = dirEntr;
		this.handler = handler;
		this.context = context;
		this.mMimeTypes = mimetypes;
	}

	public static void setThumbnailHeight(int height) {
		thumbnailHeight = height;
		thumbnailWidth = height * 4 / 3;
	}

	public void run() {
		int count = listFile.size();
		for (int x = 0; x < count; x++) {
			if (cancel)
				break;
			FileRowData frdata = listFile.get(x);
			if (!((File) frdata.getFile()).isFile())
				continue;
			String path = ((File) frdata.getFile()).getAbsolutePath();
			try {
				Drawable drw = Sets.SHOW_IMG ? extractImg(path) : null;
				if (drw == null && Sets.SHOW_MP3
						&& (path.toLowerCase().endsWith(".mp3") || path.toLowerCase().endsWith(".flac")))
					drw = extractMp3(path);
				if (drw == null && Sets.SHOW_APK && path.toLowerCase().endsWith(".apk"))
					drw = extractApk(path);
				if (drw != null) {
					frdata.setIcon(drw);
					Message msg = handler.obtainMessage(1);
					msg.obj = frdata;
					msg.sendToTarget();
				}
			} catch (Exception e) {
			}
		}
		listFile = null;
	}

	private Drawable extractMp3(String path) {
		String whereclause = fmMain.CONTEXT.getResources().getString(R.string.where_cau);
		whereclause = String.format(whereclause, MediaStore.Audio.Media.DATA, path);
		Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.getContentUri("external"),
				new String[] { MediaStore.Audio.Media.ALBUM_ID }, whereclause, null, null);
		int albumId = -1;
		if (cursor != null && cursor.moveToFirst())
			albumId = Integer.parseInt(cursor.getString(0));
		Bitmap cov = getArtworkQuick(fmMain.CONTEXT, albumId, 200, 200);
		return cov != null ? new BitmapDrawable(context.getResources(), cov) : null;
	}

	// TODO: не рефактить, в FileTools есть такой же метод, но почему-то вылетает
	// (желательно разобраться с потоками)
	public static Bitmap getArtworkQuick(Context context, long album_id, int w, int h) {
		final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
		final BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();

		w -= 1;
		ContentResolver res = context.getContentResolver();
		Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
		if (uri != null) {
			ParcelFileDescriptor fd = null;
			try {
				fd = res.openFileDescriptor(uri, "r");
				int sampleSize = 1;

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
			} catch (FileNotFoundException e) {
			} finally {
				try {
					if (fd != null)
						fd.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	private Drawable extractApk(String path) {
		try {
			PackageInfo info = context.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
			info.applicationInfo.publicSourceDir = info.applicationInfo.sourceDir = path;
			return info.applicationInfo.loadIcon(context.getPackageManager());
		} catch (Exception e) {
			return null;
		}
	}

	private Drawable extractImg(String path) {
		Drawable drw = null;
		options.inJustDecodeBounds = true;
		options.outHeight = options.outWidth = 0;
		options.inSampleSize = 1;
		if ("video/mpeg".equals(mMimeTypes.getMimeType(path)))
			return null; // don't try to decode mpegs.
		BitmapFactory.decodeFile(path, options);
		if (options.outWidth > 0 && options.outHeight > 0) {
			// Now see how much we need to scale it down.
			int widthFactor = (options.outWidth + thumbnailWidth - 1) / thumbnailWidth;
			int heightFactor = (options.outHeight + thumbnailHeight - 1) / thumbnailHeight;
			widthFactor = Math.max(widthFactor, heightFactor);
			widthFactor = Math.max(widthFactor, 1);

			// Now turn it into a power of two.
			if ((widthFactor > 1) & ((widthFactor & (widthFactor - 1)) != 0)) {
				while ((widthFactor & (widthFactor - 1)) != 0)
					widthFactor &= widthFactor - 1;
				widthFactor <<= 1;
			}
			options.inSampleSize = widthFactor;
			options.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			if (bitmap != null) {
				BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
				drawable.setBounds(0, 0, thumbnailWidth, thumbnailHeight);
				drw = drawable;
			}
		}
		return drw;
	}

}
