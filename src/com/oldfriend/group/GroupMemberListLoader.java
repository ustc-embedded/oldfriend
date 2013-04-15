package com.oldfriend.group;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Directory;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.CursorLoader;

public class GroupMemberListLoader extends CursorLoader{
	
    private static final String[] PROJECTION = new String[] {
        Data.CONTACT_ID,                        // 0
        Data.RAW_CONTACT_ID,                    // 1
        Data.DISPLAY_NAME_PRIMARY,              // 2
//        Data.PHOTO_URI,                         // 3
        Data.PHOTO_ID,                         // 3
        Data.LOOKUP_KEY,                        // 4
        Phone.NUMBER,
        GroupMembership.GROUP_ROW_ID
    };

    public static final int CONTACT_ID                   = 0;
    public static final int RAW_CONTACT_ID               = 1;
    public static final int CONTACT_DISPLAY_NAME_PRIMARY = 2;
//    public static final int CONTACT_PHOTO_URI            = 3;
    public static final int CONTACT_PHOTO_ID            = 3;
    public static final int CONTACT_LOOKUP_KEY           = 4;
    
//	private static final String SORT_ORDER = "sort_key" + " collate NOCASE ASC";
	private static final String SORT_ORDER = GroupMembership.GROUP_ROW_ID + " COLLATE LOCALIZED ASC";
//	private static final Uri uri = Phone.CONTENT_URI;
	private static final Uri uri = Data.CONTENT_URI;
	
	private long mGroupId;
	private long mGroupSysId;
	
	
	public GroupMemberListLoader(Context context, long groupId, long groupSysId){
		super(context);
		mGroupId = groupId;
		mGroupSysId = groupSysId;
		
		setProjection(PROJECTION);
//		setSelection(createSelection());
//		setSelectionArgs(createSelectionArgs());
		setSortOrder(SORT_ORDER);
//		setUri(createUri());
		setUri(uri);
	}
	
    private Uri createUri() {
        Uri uri = Data.CONTENT_URI;
        uri = uri.buildUpon().appendQueryParameter(ContactsContract.DIRECTORY_PARAM_KEY,
                String.valueOf(Directory.DEFAULT)).build();
        return uri;
    }

    private String createSelection() {
        StringBuilder selection = new StringBuilder();
        selection.append(Data.MIMETYPE + "=?" + " AND " + GroupMembership.GROUP_ROW_ID + "=?");
        return selection.toString();
    }

    private String[] createSelectionArgs() {
        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add(GroupMembership.CONTENT_ITEM_TYPE);
        selectionArgs.add(String.valueOf(mGroupId));
        return selectionArgs.toArray(new String[0]);
    }
}
