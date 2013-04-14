package com.oldfriend.group;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.CursorLoader;

public class GroupMemberListLoader extends CursorLoader{
	private static final String[] PROJECTION = new String[]{
		Phone._ID,
		Phone.DISPLAY_NAME, 
		Phone.NUMBER,
		Phone.PHOTO_ID, 
		Phone.RAW_CONTACT_ID };
	private static final String SORT_ORDER = "sort_key" + " collate NOCASE ASC";
	private static final Uri uri = Phone.CONTENT_URI;
	
	private long mGroupId;
	private long mGroupSysId;
	
	
	public GroupMemberListLoader(Context context, long groupId, long groupSysId){
		super(context);
		mGroupId = groupId;
		mGroupSysId = groupSysId;
		
		setProjection(PROJECTION);
		setSelection(createSelection());
		setSelectionArgs(createSelectionArgs());
		setSortOrder(SORT_ORDER);
		setUri(uri);
	}
	
//    private Uri createUri() {
//        Uri uri = Data.CONTENT_URI;
//        uri = uri.buildUpon().appendQueryParameter(ContactsContract.DIRECTORY_PARAM_KEY,
//                String.valueOf(Directory.DEFAULT)).build();
//        return uri;
//    }

    private String createSelection() {
        StringBuilder selection = new StringBuilder();
        selection.append(Data.MIMETYPE + "=?" + " AND (" + GroupMembership.GROUP_ROW_ID + "=? OR "+
        		GroupMembership.GROUP_ROW_ID + "=? )");
        return selection.toString();
    }

    private String[] createSelectionArgs() {
        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add(GroupMembership.CONTENT_ITEM_TYPE);
        selectionArgs.add(String.valueOf(mGroupId));
        selectionArgs.add(String.valueOf(mGroupSysId));
        return selectionArgs.toArray(new String[0]);
    }
}
