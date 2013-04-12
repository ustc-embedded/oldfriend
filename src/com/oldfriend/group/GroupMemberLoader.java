package com.oldfriend.group;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Directory;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.CursorLoader;

/**
 * Contact Loader for specified local contact group.
 * Including display name , phone number , photo, raw contact ID
 * 
 *
 */

public class GroupMemberLoader extends CursorLoader {
        private static final String[] COLUMNS = new String[] {
        	Phone.CONTACT_ID,
        	Phone.RAW_CONTACT_ID,
        	Phone.DISPLAY_NAME, 
        	Phone.NUMBER,
        	Phone.PHOTO_ID        	
        };

        public static final int CONTACT_ID                   = 0;
        public static final int RAW_CONTACT_ID               = 0;
        public static final int DISPLAY_NAME    	         = 0;
        public static final int NUMBER      	             = 0;
        public static final int PHOTO_ID	                 = 0;
//        public static final int CONTACT_PHOTO_URI            = 1;
//        public static final int CONTACT_LOOKUP_KEY           = 2;
//        public static final int CONTACT_DISPLAY_NAME_PRIMARY = 3;
//        public static final int CONTACT_PRESENCE_STATUS      = 4;
//        public static final int CONTACT_STATUS               = 5;
	
	
	private final int mGroupId;
	
	public GroupMemberLoader (Context context, int groupId){
		super(context);
        mGroupId = groupId;
        setUri(createUri());
        setProjection(COLUMNS);
        setSelection(createSelection());
        setSelectionArgs(createSelectionArgs());
        
        setSortOrder(Contacts.SORT_KEY_ALTERNATIVE);
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
