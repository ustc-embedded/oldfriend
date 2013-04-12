package com.oldfriend.group;

import com.oldfriend.ui.R;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupMemberListAdapter extends BaseAdapter{
	private final static String TAG = "GroupMemberListAdapter";
	private final static boolean DEBUG = true;
	
	Context mContext;
	Bitmap nail_photo = null;
	Bitmap mDefaultPhoto = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.photos);
	private Cursor mCursor = null;
	
	public GroupMemberListAdapter(Context context){
		mContext = context;
	}
	
	public void setCursor(Cursor cursor){
		mCursor = cursor;
	}

	@Override
	public int getCount() {
		if (mCursor != null){
			return mCursor.getCount();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {  
        ImageView image = null;  
        TextView name = null;  
        TextView phone_number = null;  
        View root_view = null;
        if (convertView == null){
        	root_view = LayoutInflater.from(mContext).inflate(  
        			R.layout.show_contact_layout, null);  
        }
        else {
        	root_view = convertView;
        }
        	
        image = (ImageView) root_view.findViewById(R.id.local_show_image);  
        name = (TextView) root_view.findViewById(R.id.local_show_name);  
        phone_number = (TextView) root_view.findViewById(R.id.local_show_number);  
      
        
        mCursor.moveToPosition(position);

        name.setText(mCursor.getString(GroupMemberLoader.DISPLAY_NAME));
        phone_number.setText(mCursor.getString(GroupMemberLoader.NUMBER));  
        long photo_id = mCursor.getLong(GroupMemberLoader.PHOTO_ID);
        if (DEBUG){
        	Log.d(TAG, "name:"+name.getText().toString()+"phone number:"+phone_number.getText().toString());
        }
        
		if (photo_id > 0) {
			byte[] photo_bytes = getNailPhoto(photo_id);
			nail_photo = BitmapFactory.decodeByteArray(photo_bytes, 0, photo_bytes.length);
			
		} else {
			nail_photo = mDefaultPhoto;
		}
        image.setImageBitmap(nail_photo);  	        
        
        return root_view;  
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
