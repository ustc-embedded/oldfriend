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
        Data.PHOTO_ID,                       	// 3
        Data.LOOKUP_KEY,                        // 4
        Data._ID,								// 5
        Data.HAS_PHONE_NUMBER        			// 6
    };

    public static final int CONTACT_ID                   = 0;
    public static final int RAW_CONTACT_ID               = 1;
    public static final int CONTACT_DISPLAY_NAME		 = 2;
    public static final int CONTACT_PHOTO_ID            = 3;
    public static final int LOOKUP_KEY           = 4;
    public static final int DATA_ID		           		= 5;
    public static final int HAS_PHONE_NUMBER           = 6;
    
	private static final String SORT_ORDER = Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
	private static final Uri uri = Data.CONTENT_URI;
	
	private long mGroupId;
	
	
	public GroupMemberListLoader(Context context, long groupId){
		super(context);
		mGroupId = groupId;
		
		setProjection(PROJECTION);
		setSelection(createSelection());
		setSelectionArgs(createSelectionArgs());
		setSortOrder(SORT_ORDER);
		setUri(uri);
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
