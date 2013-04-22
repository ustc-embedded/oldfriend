package com.oldfriend.group;

import java.util.ArrayList;

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
	private final static boolean DBG_LOADER = false;
	
	private ExpandableListView mListView;
	private Context mContext;
	private GroupBrowserFragmentInterface mListener;
	private CursorTreeAdapter mAdapter;
	LoaderManager mLoaderManager;
	private final static int GROUP_LOADER = CursorLoaderHelper.TYPE_GROUP_LOADER;
	private final static int GROUP_LOADER_ID = -1;
	private final static int MEMBER_LOADER = CursorLoaderHelper.TYPE_MEMBER_LOADER;
	
	// loader id = groupPosition + MEMBER_LOADER_ID_BASE
	private final static int MEMBER_LOADER_ID_BASE = 0;

	private ArrayList<Integer> mExpandedGroups = new ArrayList<Integer>();
	private int mSelectedGroup = -1;
	private int mSelectedChild = -1;
	
	public GroupBrowserFragment(){
		super();
	}
	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		try{
			mListener = (GroupBrowserFragmentInterface) activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()+
					"you must implement GroupBrowserFragmentInterface");
		}

		mLoaderManager = getLoaderManager();
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onStart() {
		super.onStart();
		mListView = getListView();
		if(mListView == null){
			throw new RuntimeException("the expandable listview is null");
		}

		if (mAdapter != null){
			mListView.setAdapter(mAdapter);
		}else {
			initFragment();		
		}
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
		mExpandedGroups.add(Integer.valueOf(groupPosition));
		super.onGroupExpand(groupPosition);
	}
	/**
	 * The children Cursor will be closed by CursorTreeAdapter,
	 * And loader MUST be destroyed if the cursor is closed in adapter.
	 */
	@Override
	public void onGroupCollapse(int groupPosition) {
		mExpandedGroups.remove(Integer.valueOf(groupPosition));
		mLoaderManager.destroyLoader(groupPosition + MEMBER_LOADER_ID_BASE);
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
		loadGroup(groupPosition);
		return true;
	}
	
	private void loadGroup(int groupPosition) {
		
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
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		mSelectedChild = childPosition;
		mSelectedGroup = groupPosition;
		
		GroupMemberItem tag = (GroupMemberItem)v.getTag();
		GroupMemberPageFragment page = new GroupMemberPageFragment();
		Bundle args = new Bundle();
		args.putSerializable(GroupMemberPageFragment.MEMBER_ITEM, tag);
		page.setArguments(args);
		mListener.showMemberPage(page);
		return true;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if(data == null ){
			return;
		}
		if (DBG_LOADER){
			logCursor(data);
		}
		int id = loader.getId();
		if (mAdapter == null){
			if (isGroupLoader(id)){
				mAdapter = new GroupBrowserAdapter(data,mContext,false);
				mListView.setAdapter(mAdapter);
			}
		} else {
			if (isGroupLoader(id)) {
				mAdapter.setGroupCursor(data);
			} else if (data.getCount()>0){
				id -= MEMBER_LOADER_ID_BASE;
				mAdapter.setChildrenCursor(id, data);
				/*if (id == mSelectedGroup) {
					mListView.setSelectedChild(id, mSelectedChild, true);
				}*/
				// in case returned from back stack
				if (!mListView.isGroupExpanded(id)){
					mListView.expandGroup(id);
				}	
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
