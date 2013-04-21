package com.oldfriend.group;

import com.oldfriend.ui.R;
import com.oldfriend.utilities.PhotoManager;

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
	public GroupBrowserAdapter(Cursor cursor, Context context,
			boolean autoRequery) {
		super(cursor, context, autoRequery);
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	protected void bindChildView(View view, Context context, Cursor cursor,
			boolean isLastChild) {
		if(DEBUG){
			Log.d(TAG, "bindChildView called");
		}
		
		TextView nameText = (TextView)view.findViewById(R.id.people_name);
		ImageView photoView = (ImageView)view.findViewById(R.id.people_photo);
		
		if(nameText == null ){
			return;
		}
		
		int contact_id = cursor.getInt(GroupMemberListLoader.CONTACT_ID);
		int raw_data_id = cursor.getInt(GroupMemberListLoader.RAW_CONTACT_ID);
		int data_id = cursor.getInt(GroupMemberListLoader.DATA_ID);
		int has_phone_number = cursor.getInt(GroupMemberListLoader.HAS_PHONE_NUMBER);
		long photoId = cursor.getLong(GroupMemberListLoader.CONTACT_PHOTO_ID);
		String look_up_key = cursor.getString(GroupMemberListLoader.LOOKUP_KEY);
		String display_name = cursor.getString(GroupMemberListLoader.CONTACT_DISPLAY_NAME);
		GroupMemberItem tag = new GroupMemberItem(contact_id,raw_data_id,data_id,
				has_phone_number,photoId,look_up_key,display_name);
		view.setTag(tag);
		nameText.setText(display_name);

		Bitmap nailPhoto = PhotoManager.getNailPhotoById(mContext, photoId);
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

//		Uri phoneNumbersUri = builder.build();

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
	/*
		TextView nameText = (TextView)rootView.findViewById(R.id.people_name);
		ImageView photoView = (ImageView)rootView.findViewById(R.id.people_photo);
		
		if(nameText == null){
			return rootView;
		}
		int nameCol = cursor.getColumnIndex(Phone.DISPLAY_NAME);
		int photoCol = cursor.getColumnIndex(Phone.PHOTO_ID);
		
		nameText.setText(cursor.getString(nameCol));
		
		long photoId = cursor.getLong(photoCol);
		Bitmap nailPhoto = null;
		if (photoId > 0) {
			byte[] photo_bytes = PhotoManager.getNailPhotoById(mContext, photoId);
			nailPhoto = BitmapFactory.decodeByteArray(photo_bytes, 0, photo_bytes.length);
			
		} else {
			nailPhoto = mDefaultPhoto;
		}
		photoView.setImageBitmap(nailPhoto);
		*/
		return rootView;
	}

	@Override
	protected View newGroupView(Context context, Cursor cursor,
			boolean isExpanded, ViewGroup parent) {
		if(DEBUG){
			Log.d(TAG, "newGroupView() is called");
		}
		
		View rootView = mInflater.inflate(R.layout.group_list_item, parent, false);
		/*
		TextView nameText = (TextView)rootView.findViewById(R.id.group_browser_group_name);
		TextView countText = (TextView)rootView.findViewById(R.id.group_browser_member_count);
		if(nameText == null || countText == null){
			return rootView;
		}
		int nameCol = GroupListLoader.TITLE;
		int countCol = GroupListLoader.MEMBER_COUNT;
		nameText.setText(cursor.getString(nameCol));
		countText.setText(Integer.toString(cursor.getInt(countCol)));
		*/
		return rootView;
	}
}
