package com.oldfriend.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.oldfriend.ui.R;

public class TwoLevelListViewFragment extends Fragment
implements ExpandableListView.OnChildClickListener,
		ExpandableListView.OnGroupClickListener,
		ExpandableListView.OnGroupExpandListener,
		ExpandableListView.OnGroupCollapseListener
{

	private View mRootView;
	private ExpandableListView mListView = null;
	private CursorTreeAdapter mAdapter = null;
	private Context mContext;
	
	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(
				R.layout.two_level_list_view_container, container, false);
		
		mListView = (ExpandableListView)mRootView.findViewById(R.id.two_level_list_view);
		setListener();
		return mRootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onOptionsMenuClosed(menu);
	}
	
	public CursorTreeAdapter getListAdapter (){
		return mAdapter;
	}
	
	public ExpandableListView getListView (){
		return mListView;
	}

	@Override
	public void onGroupCollapse(int groupPosition) {		
	}

	@Override
	public void onGroupExpand(int groupPosition) {		
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		return false;
	}
	
	private void setListener(){
		if(mListView == null ){
			return;
		}
		mListView.setOnChildClickListener(this);
		mListView.setOnGroupClickListener(this);
		mListView.setOnGroupCollapseListener(this);
		mListView.setOnGroupExpandListener(this);
	}
}
