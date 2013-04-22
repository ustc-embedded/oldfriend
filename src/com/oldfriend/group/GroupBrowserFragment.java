package com.oldfriend.group;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.oldfriend.utilities.CursorLoaderHelper;
import com.oldfriend.utilities.TwoLevelListViewFragment;

public class GroupBrowserFragment extends TwoLevelListViewFragment
implements CursorLoaderHelper.OnLoadFinishedListener
{
	public interface GroupBrowserFragmentInterface{
		void showMemberPage(Fragment newFragment);
	}
	private final static String TAG = "GroupBrowserFragment";
	private final static boolean DEBUG = true;
	
	private ExpandableListView mListView;
	private Context mContext;
	private GroupBrowserFragmentInterface mListener;
	private CursorTreeAdapter mAdapter;
	LoaderManager mLoaderManager;
	private final static int GROUP_LOADER = CursorLoaderHelper.TYPE_GROUP_LOADER;
	private final static int GROUP_LOADER_ID = -1;
	private final static int MEMBER_LOADER = CursorLoaderHelper.TYPE_MEMBER_LOADER;
	private final static int MEMBER_LOADER_ID_BASE = 0;

	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		try{
			mListener = (GroupBrowserFragmentInterface) activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()+
					"you must implement GroupBrowserFragmentInterface");
		}
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
		
		long groupId;
		Cursor groupCursor = mAdapter.getCursor();
		int origPos = groupCursor.getPosition();
		groupCursor.moveToPosition(groupPosition);
		groupId = groupCursor.getLong(GroupListLoader.GROUP_ID);
		
		groupCursor.moveToPosition(origPos);
		
		int loaderId = MEMBER_LOADER_ID_BASE+groupPosition;

		Bundle arg = new Bundle();
		arg.putLong(CursorLoaderHelper.GROUP_ID_ARG, groupId);
		new CursorLoaderHelper(mContext,this,mLoaderManager,MEMBER_LOADER,loaderId,arg);
		
		super.onGroupExpand(groupPosition);
	}
	

	@Override
	public void onGroupCollapse(int groupPosition) {
		mAdapter.setChildrenCursor(groupPosition, null);
		super.onGroupCollapse(groupPosition);
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

		mListView.expandGroup(groupPosition);		
		return true;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
//		View view= v.findViewById(R.id.extra_buttons);
//		view.setVisibility(View.VISIBLE);
		GroupMemberItem tag = (GroupMemberItem)v.getTag();
		Toast.makeText(mContext, tag.mDisplay_name, Toast.LENGTH_LONG).show();
		GroupMemberPageFragment page = new GroupMemberPageFragment();
		Bundle args = new Bundle();
		args.putSerializable(GroupMemberPageFragment.MEMBER_ITEM, tag);
		page.setArguments(args);
		mListener.showMemberPage(page);
		return true;
	}

	// Called when a loader has finished loading
	// BUG: 调用这个函数以后在cursor.getCount()中显示Cursor被关闭！
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if(data == null ){
			return;
		}
		if (DEBUG){
			logCursor(data);
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
		return -1 == id;
	}
	
	private void logCursor(Cursor cursor){
		int origPos = cursor.getPosition();
		int nCol = cursor.getColumnCount();
		cursor.moveToFirst();
		while(!cursor.isLast()){
			int i;
			for(i=0;i<nCol;i++) {
				System.out.print(cursor.getColumnName(i)+":");
				System.out.print(cursor.getString(i)+"  ");				
			}
			System.out.print('\n');
			cursor.moveToNext();
		}
		cursor.moveToPosition(origPos);
	}
	
	public void setListener(GroupBrowserFragmentInterface listener){
		mListener = listener;
	}

}
