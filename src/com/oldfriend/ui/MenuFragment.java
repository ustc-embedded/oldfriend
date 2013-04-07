package com.oldfriend.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MenuFragment extends Fragment{
	
	public static final String TAG = "MenuFragment";
	public static final boolean DEBUG = true;
	OnMenuItemSelectedListener mCallback;
	
	interface OnMenuItemSelectedListener {
		void onShowLocalContact();
		void onShowOldfriendContact();
		void onShowPreferenceMenu();
		void onShowUserAccount();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnMenuItemSelectedListener) activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()
                    + " must implement OnMenuItemSelected");
		}
		if(DEBUG){
			Log.d(TAG, "onAttach completed");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(DEBUG){
			Log.d(TAG, "onCreate completed");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.left_menu, null);
		if (v == null){
			Log.v(TAG, "cannot create view");
			return null;
		}
		if(DEBUG){
			Log.d(TAG, "onCreateView completed");
		}
		return v;
	}

	@Override
	public void onStart() {
		setItemSelectedListener(R.id.show_local_contact);
		setItemSelectedListener(R.id.show_oldfriend_contact);
		setItemSelectedListener(R.id.show_preference_menu);
		setItemSelectedListener(R.id.show_user_account);
		super.onStart();
		if(DEBUG){
			Log.d(TAG, "onStart completed");
		}
	}
	
	private void setItemSelectedListener(int resId){
		View theView = getView().findViewById(resId);
		if(theView == null) {
			Log.v(TAG, "cannot find menu item, resource id =" + resId);
			return;
		}
		theView.setOnClickListener(new OnItemSelectListener());
	}
	
	private class OnItemSelectListener implements OnClickListener {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.show_local_contact: 
				mCallback.onShowLocalContact();
				break;
			case R.id.show_oldfriend_contact: 
				mCallback.onShowOldfriendContact();
				break;
			case R.id.show_preference_menu:
				mCallback.onShowPreferenceMenu();
				break;
			case R.id.show_user_account:
				mCallback.onShowUserAccount();
				break;
			default:
				Log.v(TAG,"unkonwn set onClickListener");
				break;
			}
		}
	}
	
}
