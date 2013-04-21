package com.oldfriend.group;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.*;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldfriend.ui.R;
import com.oldfriend.utilities.PhotoManager;

public class GroupMemberPageFragment extends Fragment{
	GroupMemberItem mItem;
	Context mContext;
	View mRootView;
	LayoutInflater mInflater;
	
	ImageView mPhotoView;
	TextView mNameView;
	LinearLayout mDetailView;
	
	boolean mHasPhoneNumber=false;
	boolean mHasEmail = false;
	
	public static final String MEMBER_ITEM = "member item";
	public static final String SAVED_ITEM = "saved item";
	public static final String FRAGMENT_NAME = "group member page fragment";
	public GroupMemberPageFragment(){
		super();
	}
	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		mInflater = LayoutInflater.from(mContext);
		super.onAttach(activity);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle args = getArguments();
		if(args == null){
			throw new IllegalArgumentException("you should send argument to!");
		}
		mItem = (GroupMemberItem)args.getSerializable(MEMBER_ITEM);
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(savedInstanceState != null){
			mItem = (GroupMemberItem)savedInstanceState.getSerializable(SAVED_ITEM);
		}
		mRootView = inflater.inflate(R.layout.group_member_page, container, false);
		return mRootView;
	}

	@Override
	public void onStart() {
		mNameView = (TextView)mRootView.findViewById(R.id.member_page_name);
		mPhotoView = (ImageView)mRootView.findViewById(R.id.member_page_photo);
		mDetailView = (LinearLayout)mRootView.findViewById(R.id.member_page_detail);
		displayContent();
		super.onStart();
	}
	
	private void displayContent(){
		
		// display photo and name
		mNameView.setText(mItem.mDisplay_name);
		Bitmap photo = PhotoManager.getNailPhotoById(mContext, mItem.mPhotoId);
		mPhotoView.setImageBitmap(photo);
		
		// display details
		Cursor cursor = queryMataData(mItem.mRaw_data_id);
		displayPhoneNumber(cursor);
		displayEmail(cursor);
		displayOther(cursor);
		cursor.close();
	}
	
	private void displayHeader(String label){
		View title = mInflater.inflate(R.layout.member_page_element_title, 
				mDetailView, false);
		((TextView)title.findViewById(R.id.title_title))
		.setText(label);
		((ImageView)title.findViewById(R.id.title_divider))
		.setImageResource(android.R.drawable.divider_horizontal_dim_dark);
		mDetailView.addView(title);
	}
	
	private void displayPhoneNumber(Cursor cursor){
		cursor.moveToFirst();
		int i;
		for(i=0;i<cursor.getCount();i++){
			String type = cursor.getString(0);
			String number = cursor.getString(1);
			int phone_type = cursor.getInt(2);
			String label = cursor.getString(3);
			if (Phone.CONTENT_ITEM_TYPE.equals(type)){
				if(!mHasPhoneNumber){
					String headStr = (String) getResources().getText(R.string.phone_ch);
					displayHeader(headStr);
					mHasPhoneNumber = true;
				}
				
				View detail = mInflater.inflate(R.layout.member_page_element_detail, 
						mDetailView, false);
				((TextView)detail.findViewById(R.id.detail_detail)).setText(number);
				
				TextView type_field = (TextView)detail.findViewById(R.id.detail_title);
				
				switch(phone_type){
				case Phone.TYPE_CUSTOM:
					type_field.setText(label);
					break;
				case Phone.TYPE_HOME:
					type_field.setText(R.string.telephone);
					break;
				case Phone.TYPE_MOBILE:
					type_field.setText(R.string.mobilephone);
					break;
				default:
					type_field.setText(R.string.other);
					break;
				}
				mDetailView.addView(detail);
			} 
			cursor.moveToNext();
		}	
	}
	
	private void displayEmail(Cursor cursor){
		cursor.moveToFirst();
		int i;
		int n = cursor.getCount();
		for (i=0;i<n;i++){
			String type = cursor.getString(0);
			String email = cursor.getString(1);
			int email_type = cursor.getInt(2);
			String label = cursor.getString(3);
			if (Email.CONTENT_ITEM_TYPE.equals(type)){
				if(!mHasEmail){
					String headStr = (String) getResources().getText(R.string.email);
					displayHeader(headStr);
					mHasEmail = true;
				}
				View detail = mInflater.inflate(R.layout.member_page_element_detail, 
						mDetailView, false);
				((TextView)detail.findViewById(R.id.detail_detail)).setText(email);
				
				TextView type_field = (TextView)detail.findViewById(R.id.detail_title);
				switch (email_type){
				case Email.TYPE_HOME:
					type_field.setText(R.string.home);
					break;
				case Email.TYPE_WORK:
					type_field.setText(R.string.work);
					break;
				case Email.TYPE_CUSTOM:
					if(label != null){
						type_field.setText(label);
						break;
					}//else set "other"
				case Email.TYPE_MOBILE:
				case Email.TYPE_OTHER:
				default:
					type_field.setText(R.string.other);
					break;
				}
				mDetailView.addView(detail);
			}
			cursor.moveToNext();
		}
	}

	private void displayOther(Cursor cursor){
		String headerStr = getResources().getString(R.string.other);
		displayHeader(headerStr);
		cursor.moveToFirst();
		int i;
		int n = cursor.getCount();
		for (i=0;i<n;i++){
			String type = cursor.getString(0);
			if (Organization.CONTENT_ITEM_TYPE.equals(type)){
				String company = cursor.getString(1);
				View detail = mInflater.inflate(R.layout.member_page_element_detail, 
						mDetailView, false);
				((TextView)detail.findViewById(R.id.detail_detail)).setText(company);
				((TextView)detail.findViewById(R.id.detail_title)).setText(R.string.company);
				mDetailView.addView(detail);
			} else if (Note.CONTENT_ITEM_TYPE.equals(type)){
				String note = cursor.getString(1);
				View detail = mInflater.inflate(R.layout.member_page_element_detail, 
						mDetailView, false);
				((TextView)detail.findViewById(R.id.detail_detail)).setText(note);
				((TextView)detail.findViewById(R.id.detail_title)).setText(R.string.note);
				mDetailView.addView(detail);
			} else if (Nickname.CONTENT_ITEM_TYPE.equals(type)){
				String nickName = cursor.getString(1);
				View detail = mInflater.inflate(R.layout.member_page_element_detail, 
						mDetailView, false);
				((TextView)detail.findViewById(R.id.detail_detail)).setText(nickName);
				((TextView)detail.findViewById(R.id.detail_title)).setText(R.string.nick_name);
				mDetailView.addView(detail);
			} else if (Website.CONTENT_ITEM_TYPE.equals(type)){
				String website = cursor.getString(1);
				View detail = mInflater.inflate(R.layout.member_page_element_detail, 
						mDetailView, false);
				((TextView)detail.findViewById(R.id.detail_detail)).setText(website);
				((TextView)detail.findViewById(R.id.detail_title)).setText(R.string.website);
				mDetailView.addView(detail);
			}
			cursor.moveToNext();
		}
	}
	
	private Cursor queryMataData(int raw_data_id){
		Cursor result=null;
		ContentResolver cr = mContext.getContentResolver();
		String[] PROJECTION = {Data.MIMETYPE,Data.DATA1,Data.DATA2,Data.DATA3};
		
		StringBuilder selection = new StringBuilder();
        selection.append(Data.RAW_CONTACT_ID + "=?");
        String SELECTION = selection.toString();
        
        String[] SELECTION_ARGS = {String.valueOf(raw_data_id)};
        String SORT_ORDER = Data.MIMETYPE + " COLLATE LOCALIZED ASC";
        
        result = cr.query(Data.CONTENT_URI, PROJECTION, SELECTION, SELECTION_ARGS, SORT_ORDER);
        
		return result;
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(SAVED_ITEM, mItem);
		super.onSaveInstanceState(outState);
	}
}
