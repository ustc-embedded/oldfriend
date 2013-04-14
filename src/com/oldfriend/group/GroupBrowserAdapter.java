package com.oldfriend.group;

import com.oldfriend.ui.R;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Groups;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class GroupBrowserAdapter extends CursorTreeAdapter{
	
	private final static String TAG = "GroupBrowserAdapter";
	private final static boolean DEBUG = true;

	LayoutInflater mInflater;
	Context mContext;
	Bitmap mDefaultPhoto = null;
	public GroupBrowserAdapter(Cursor cursor, Context context,
			boolean autoRequery) {
		super(cursor, context, autoRequery);
//		mInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mDefaultPhoto = BitmapFactory.decodeResource(
				mContext.getResources(), R.drawable.photos);
	}

	@Override
	protected void bindChildView(View view, Context context, Cursor cursor,
			boolean isLastChild) {
		if(DEBUG){
			Log.d(TAG, "bindChildView called");
		}
		
		TextView nameText = (TextView)view.findViewById(R.id.people_name);
		TextView phoneText = (TextView)view.findViewById(R.id.phone_number);
		ImageView photoView = (ImageView)view.findViewById(R.id.people_photo);
		
		if(nameText == null || phoneText == null){
			return;
		}
		int nameCol = cursor.getColumnIndex(Phone.DISPLAY_NAME);
		int phoneCol = cursor.getColumnIndex(Phone.NUMBER);
		int photoCol = cursor.getColumnIndex(Phone.PHOTO_ID);
		
		nameText.setText(cursor.getString(nameCol));
		phoneText.setText(cursor.getString(phoneCol));
		
		long photoId = cursor.getLong(photoCol);
		Bitmap nailPhoto = null;
		if (photoId > 0) {
			byte[] photo_bytes = getNailPhoto(photoId);
			nailPhoto = BitmapFactory.decodeByteArray(photo_bytes, 0, photo_bytes.length);
			
		} else {
			nailPhoto = mDefaultPhoto;
		}
		photoView.setImageBitmap(nailPhoto);
	}

	@Override
	protected void bindGroupView(View view, Context context, Cursor cursor,
			boolean isExpanded) {
		if(DEBUG){
			Log.d(TAG, "bindGroupView called");
		}
		
		TextView nameText = (TextView)view.findViewById(R.id.group_browser_group_name);
		TextView countText = (TextView)view.findViewById(R.id.group_browser_member_count);
		if(nameText == null || countText == null){
			return;
		}
		int nameCol = cursor.getColumnIndex(Groups.TITLE);
		int countCol = cursor.getColumnIndex(Groups.SUMMARY_COUNT);
		nameText.setText(cursor.getString(nameCol));
		countText.setText(Integer.toString(cursor.getInt(countCol)));	
	}

	@Override
	protected Cursor getChildrenCursor(Cursor groupCursor) {
		Uri.Builder builder = Phone.CONTENT_URI.buildUpon();
		int groupIdCol = groupCursor.getColumnIndex(Groups._ID);
		ContentUris.appendId(builder, groupCursor.getLong(groupIdCol));

		Uri phoneNumbersUri = builder.build();

		// The returned Cursor MUST be managed by us, so we use Activity's helper
		// functionality to manage it for us.
//		return managedQuery(phoneNumbersUri, new String[] {Phone._ID, Phone.NUMBER}, null, null, null);
		if(DEBUG){
			Log.d(TAG,"getChildrenCursor() called ");
		}
		return null;
	}

	@Override
	protected View newChildView(Context context, Cursor cursor,
			boolean isLastChild, ViewGroup parent) {
		if(DEBUG){
			Log.d(TAG, "newChildView() is called");
		}
		
		View rootView = mInflater.inflate(
				R.layout.group_member_list_item, parent, false);
		TextView nameText = (TextView)rootView.findViewById(R.id.people_name);
		TextView phoneText = (TextView)rootView.findViewById(R.id.phone_number);
		ImageView photoView = (ImageView)rootView.findViewById(R.id.people_photo);
		
		if(nameText == null || phoneText == null){
			return rootView;
		}
		int nameCol = cursor.getColumnIndex(Phone.DISPLAY_NAME);
		int phoneCol = cursor.getColumnIndex(Phone.NUMBER);
		int photoCol = cursor.getColumnIndex(Phone.PHOTO_ID);
		
		nameText.setText(cursor.getString(nameCol));
		phoneText.setText(cursor.getString(phoneCol));
		
		long photoId = cursor.getLong(photoCol);
		Bitmap nailPhoto = null;
		if (photoId > 0) {
			byte[] photo_bytes = getNailPhoto(photoId);
			nailPhoto = BitmapFactory.decodeByteArray(photo_bytes, 0, photo_bytes.length);
			
		} else {
			nailPhoto = mDefaultPhoto;
		}
		photoView.setImageBitmap(nailPhoto);
		
		return rootView;
	}

	@Override
	protected View newGroupView(Context context, Cursor cursor,
			boolean isExpanded, ViewGroup parent) {
		if(DEBUG){
			Log.d(TAG, "newGroupView() is called");
		}
		
		View rootView = mInflater.inflate(R.layout.group_list_item, parent, false);
		TextView nameText = (TextView)rootView.findViewById(R.id.group_browser_group_name);
		TextView countText = (TextView)rootView.findViewById(R.id.group_browser_member_count);
		if(nameText == null || countText == null){
			return rootView;
		}
		int nameCol = GroupListLoader.TITLE;
		int countCol = GroupListLoader.MEMBER_COUNT;
		nameText.setText(cursor.getString(nameCol));
		countText.setText(Integer.toString(cursor.getInt(countCol)));
		return rootView;
	}

	public byte[] getNailPhoto(long photo_id) {
	     Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);
	     Cursor cursor = mContext.getContentResolver().query(photoUri,
	          new String[] {Contacts.Photo.PHOTO}, null, null, null);
	     if (cursor == null) {
	         return null;
	     }
	     try {
	         if (cursor.moveToFirst()) {
	             byte[] data = cursor.getBlob(0);
	             if (data != null) {
	                 return data;
	             }
	         }
	     } finally {
	         cursor.close();
	     }
	     return null;
	 }
}
