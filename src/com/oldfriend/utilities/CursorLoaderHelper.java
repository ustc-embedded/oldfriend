package com.oldfriend.utilities;

import com.oldfriend.group.GroupListLoader;
import com.oldfriend.group.GroupMemberListLoader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;

public class CursorLoaderHelper {
	public interface OnLoadFinishedListener {
		public void onLoadFinished (Loader<Cursor> loader, Cursor data);
	}
	private final static String TAG = "CursorLoaderHelper";
	private final static boolean DEBUG = true;
	
	public final static int TYPE_GROUP_LOADER = 0;
	public final static int TYPE_MEMBER_LOADER = 1;
	public final static String GROUP_ID_ARG = "group ID ARG";
	public final static String GROUP_SYS_ID_ARG = "group system ID ARG";
	
	
	private Context mContext;
	private OnLoadFinishedListener mListener;
	private Loader<Cursor> mLoader = null;
	private LoaderManager mLoaderManager;
	private int mLoaderType;
	private int mLoaderId;
	private Bundle mLoaderArgs;
	
	
	public CursorLoaderHelper(Context context, OnLoadFinishedListener listener,
			LoaderManager lm,int loaderType, int loaderId, Bundle loaderArgs){
		mContext = context;
//		try {
//			mListener = (OnLoadFinishedListener) context;
//		} catch (ClassCastException e){
//			throw new ClassCastException(mContext.toString()+
//					"must implement OnLoadFinishedListener");
//		}
		
		mListener = listener;
		mLoaderType = loaderType;
		mLoaderId = loaderId;
		mLoaderManager = lm;
		mLoaderArgs = loaderArgs;
		initLoader();
	}
	
	private void initLoader(){
		if (mLoaderManager == null){
			throw new IllegalArgumentException(TAG + "got empty loader manager");
		}
		
		mLoaderManager.initLoader(mLoaderId, null, mLoaderCallback);
	}
	
	LoaderCallbacks<Cursor> mLoaderCallback = new LoaderCallbacks<Cursor>() {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
			if (mLoaderType == TYPE_GROUP_LOADER){
				mLoader = new GroupListLoader(mContext);
				if(DEBUG){
					Log.d(TAG, "creating group loader...");
				}
			} else if (mLoaderType == TYPE_MEMBER_LOADER){
				long groupId ;
				long groupSysId;
				if (mLoaderArgs == null){
					throw new IllegalArgumentException(TAG+"you should send groupId");
				} else {
					groupId = mLoaderArgs.getLong(GROUP_ID_ARG);
					groupSysId = mLoaderArgs.getLong(GROUP_SYS_ID_ARG);
				}
				if(DEBUG){
					Log.d(TAG, "creating member loader...groupId="+groupId+"system id="+groupSysId);
				}
				mLoader = new GroupMemberListLoader(mContext,groupId,groupSysId);
			}
			return mLoader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			if(DEBUG && data != null && loader != null){
				Log.d(TAG, "loader" +loader.getId()+" finished, has "+ 
			data.getCount()+"items");
			}
			mListener.onLoadFinished(loader, data);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> arg0) {
		}
	};
	
	public Loader<Cursor> getLoader(){
		return mLoader;
	}
}
