package com.oldfriend.group;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;

import com.oldfriend.utilities.CursorLoaderHelper;
import com.oldfriend.utilities.TwoLevelListViewFragment;

public class GroupBrowserFragment extends TwoLevelListViewFragment
implements CursorLoaderHelper.OnLoadFinishedListener
{
	private final static String TAG = "GroupBrowserFragment";
	private final static boolean DEBUG = true;
	
	private ExpandableListView mListView;
	private Context mContext;
	private CursorTreeAdapter mAdapter;
	LoaderManager mLoaderManager;
	private final static int GROUP_LOADER = CursorLoaderHelper.TYPE_GROUP_LOADER;
	private final static int GROUP_LOADER_ID = -1;
	private final static int MEMBER_LOADER = CursorLoaderHelper.TYPE_MEMBER_LOADER;
	private final static int MEMBER_LOADER_ID_BASE = 0;

	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		super.onAttach(activity);
	}

	@Override
	public void onStart() {
		super.onStart();
		mListView = getListView();
		if(mListView == null){
			throw new RuntimeException("the expandable listview is null");
		}
//		mAdapter = new GroupBrowserAdapter()
		mLoaderManager = getLoaderManager();
		
		initFragment();
	}

	private void initFragment(){
		startLoader(GROUP_LOADER, GROUP_LOADER_ID);
	}
	
	private void startLoader(int type,int id){
		new CursorLoaderHelper(mContext,this,mLoaderManager,type,id,null);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		if(DEBUG){
			Log.d(TAG, "Group clicked! ");
		}		
		
		boolean expanded = mListView.isGroupExpanded(groupPosition);
		//just toggle the expanding
		if (expanded){
			mListView.collapseGroup(groupPosition);
			return true;
		}
		
		long groupId;
		long groupSysId;
		Cursor groupCursor = mAdapter.getCursor();
		groupId = groupCursor.getLong(GroupListLoader.GROUP_ID);
		groupSysId = groupCursor.getLong(GroupListLoader.SYSTEM_ID);
		
		int loaderId = MEMBER_LOADER_ID_BASE+groupPosition;

		Bundle arg = new Bundle();
		arg.putLong(CursorLoaderHelper.GROUP_ID_ARG, groupId);
		arg.putLong(CursorLoaderHelper.GROUP_SYS_ID_ARG, groupSysId);
		new CursorLoaderHelper(mContext,this,mLoaderManager,MEMBER_LOADER,loaderId,arg);

		mListView.expandGroup(groupPosition);		
		return true;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		return true;
	}

	// Called when a loader has finished loading
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if(data == null ){
			return;
		}
		int id = loader.getId();
		if (mAdapter == null){
			if (isGroupLoader(id)){
				mAdapter = new GroupBrowserAdapter(data,mContext,true);
				mListView.setAdapter(mAdapter);
			}
		} else {
			if (isGroupLoader(id)) {
				mAdapter.setGroupCursor(data);
			} else if (data.getCount()>0){
				id -= MEMBER_LOADER_ID_BASE;
				mAdapter.setChildrenCursor(id, data);
			}
		}
	}
	
	private boolean isGroupLoader(int id){
		return id == -1;
	}

}
