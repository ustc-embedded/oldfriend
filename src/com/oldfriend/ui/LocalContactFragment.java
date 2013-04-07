package com.oldfriend.ui;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

// TODO 使用子线程处理头像
// TODO 实现联系人群组
// TODO 实现子列表显示拨打电话和发短信

public class LocalContactFragment extends ListFragment{
	public static final String TAG = "LocalContactFragment";
	public static final boolean DEBUG = true;
	
	Context mContext = null;
	
	private static final String[] PHONES_PROJECTION = new String[]{
		Phone.DISPLAY_NAME, Phone.NUMBER,Phone.PHOTO_ID, Phone.RAW_CONTACT_ID};
	
//	private ArrayList<String> mContactsName = new ArrayList<String>();
//	private ArrayList<String> mContactsNumber = new ArrayList<String>();
//	private ArrayList<Bitmap> mContactsPhoto = new ArrayList<Bitmap>();
	
	ListView mListView = null;
	MyListAdapter adapter = null;
	
	Cursor phoneCursor;
	
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		
		if(DEBUG) {
			Log.d(TAG, "in onCreate");
		}
		
		ContentResolver resolver = mContext.getContentResolver();
		phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, 
				null, null, "sort_key" + " collate NOCASE ASC");
		
		adapter = new MyListAdapter(mContext);
		setListAdapter(adapter);
		
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> adapterView, View view,  
//		            int position, long id) {  
//		        //调用系统方法拨打电话  
//		        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri  
//		            .parse("tel:" + mContactsNumber.get(position)));  
//		        startActivity(dialIntent);  
//		        }
//		});
		
		if(DEBUG) {
			Log.d(TAG, "onCreate completed");
		}
	}
	
	/**获取通讯录联系人信息**/
	/*
	private void getPhoneContacts() {
		ContentResolver resolver = mContext.getContentResolver();
		
		// 获取手机联系人
		phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, 
				null, null, "sort_key" + " collate NOCASE ASC");
		
		if(phoneCursor != null) {
			while (phoneCursor.moveToNext()){
				String name = phoneCursor.getString(0);
				String number = phoneCursor.getString(1);
				long photo_id = phoneCursor.getLong(2);
				
				if (TextUtils.isEmpty(number))
					continue;
				
				//String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				//Long contactId = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
				//Long photoId = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
				Bitmap nail_photo = null;
				
				if (photo_id > 0) {
					byte[] photo_bytes = getNailPhoto(photo_id);
					nail_photo = BitmapFactory.decodeByteArray(photo_bytes, 0, photo_bytes.length);
					
				} else {
					nail_photo = BitmapFactory.decodeResource(getResources(), R.drawable.photos);
				}
				
				mContactsName.add(name);
				mContactsNumber.add(number);
				mContactsPhoto.add(nail_photo);
				//Log.v("联系人", contactName+":"+phoneNumber);
			}
		phoneCursor.close();
		}
	}
	*/
	
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
	
	
	class MyListAdapter extends BaseAdapter {  
		Bitmap nail_photo = null;
		Bitmap mDefaultPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.photos);
	    public MyListAdapter(Context context) {  
	        mContext = context;  
	    }  
	  
	    public int getCount() {  
	        //设置绘制数量  
	        //return mContactsName.size();
	    	return phoneCursor.getCount();
	    }  
	  
	    @Override  
	    public boolean areAllItemsEnabled() {  
	        return false;  
	    }  
	  
	    public Object getItem(int position) {  
	        return position;  
	    }  
	  
	    public long getItemId(int position) {  
	        return position;  
	    }  
	  
	    public View getView(int position, View convertView, ViewGroup parent) {  
	        ImageView image = null;  
	        TextView name = null;  
	        TextView phone_number = null;  
	        if (convertView == null || position < phoneCursor.getCount()) {  
	        convertView = LayoutInflater.from(mContext).inflate(  
	            R.layout.show_contact_layout, null);  
	        image = (ImageView) convertView.findViewById(R.id.local_show_image);  
	        name = (TextView) convertView.findViewById(R.id.local_show_name);  
	        phone_number = (TextView) convertView.findViewById(R.id.local_show_number);  
	        }  
	        
	        phoneCursor.moveToPosition(position);

	        name.setText(phoneCursor.getString(0));

	        phone_number.setText(phoneCursor.getString(1));  
	        Log.v("联系人", phoneCursor.getString(0));

	        long photo_id = phoneCursor.getLong(2);
	        
	        if (nail_photo!= null)
	        {
	        	Log.v("photo:","输出");
	        }

			if (photo_id > 0) {
				byte[] photo_bytes = getNailPhoto(photo_id);
				nail_photo = BitmapFactory.decodeByteArray(photo_bytes, 0, photo_bytes.length);
				
			} else {
				nail_photo = mDefaultPhoto;
			}
	        image.setImageBitmap(nail_photo);  	        
	        
	        return convertView;  
	    }  
	    }  
}
