package com.oldfriend.group;

import java.io.Serializable;

public class GroupMemberItem implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public int mContact_id;
	public int mRaw_data_id;
	public int mData_id;
	public int mHas_phone_number;
	public long mPhotoId;
	public String mLook_up_key;
	public String mDisplay_name;
	
	public GroupMemberItem(int contactId,int rawId,
			int dataId,int hasPhoneNumber,long photoId,
			String lookUpKey,String name){
		mContact_id = contactId;
		mRaw_data_id = rawId;
		mData_id = dataId;
		mHas_phone_number = hasPhoneNumber;
		mPhotoId = photoId;
		mLook_up_key = lookUpKey;
		mDisplay_name = name;
	}
}
