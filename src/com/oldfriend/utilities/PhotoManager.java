package com.oldfriend.utilities;

import com.oldfriend.ui.R;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

public class PhotoManager {
	private static Bitmap mDefaultPhoto = null;

	public static Bitmap getNailPhotoById(Context context, long photo_id) {
		Bitmap result = null;
		if (photo_id <= 0) {
			if (mDefaultPhoto == null) {
				mDefaultPhoto = BitmapFactory.decodeResource(
						context.getResources(), R.drawable.photos);
			}
			result = mDefaultPhoto;
		} else {
			Uri photoUri = ContentUris.withAppendedId(
					ContactsContract.Data.CONTENT_URI, photo_id);
			Cursor cursor = context.getContentResolver().query(photoUri,
					new String[] { Contacts.Photo.PHOTO }, null, null, null);
			if (cursor != null) {
				try {
					if (cursor.moveToFirst()) {
						byte[] data = cursor.getBlob(0);
						if (data != null) {
							result = BitmapFactory.decodeByteArray(data, 0,
									data.length);
						}
					}
				} finally {
					cursor.close();
				}
			}
		}
		return result;
	}
}
