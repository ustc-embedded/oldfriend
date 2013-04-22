package com.oldfriend.group;

import com.oldfriend.ui.R;
import com.oldfriend.utilities.PhotoManager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
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

	// Children cursor will be set by setChildren Cursor when asynchronously query finished
	@Override
	protected Cursor getChildrenCursor(Cursor groupCursor) {
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
		return rootView;
	}

	@Override
	protected View newGroupView(Context context, Cursor cursor,
			boolean isExpanded, ViewGroup parent) {
		if(DEBUG){
			Log.d(TAG, "newGroupView() is called");
		}
		
		View rootView = mInflater.inflate(R.layout.group_list_item, parent, false);
		return rootView;
	}
}
