package com.oldfriend.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.oldfriend.group.GroupBrowserFragment;
import com.oldfriend.group.GroupMemberPageFragment;
import com.oldfriend.ui.slidingmenu.lib.SlidingMenu;
import com.oldfriend.ui.slidingmenu.lib.app.SlidingFragmentActivity;
// TODO 横屏的时候有bug
// TODO 没有实现单击图标打开侧边栏的功能
public class OldFriendActivity extends SlidingFragmentActivity 
			implements MenuFragment.OnMenuItemSelectedListener,
			GroupBrowserFragment.GroupBrowserFragmentInterface
{
	public static final String TAG = "SlidingFragmentActivity";
	public static final boolean DEBUG = true;

	protected Fragment mFrag;
	private int mTitleRes = R.string.oldfriend_contact;


	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setTitle(mTitleRes);
		
		setContentView(R.layout.content_frame);
		//setContentFragment(new SampleListFragment(),false);
		//setContentFragment(new LocalContactFragment(),false);
		//setContentFragment(new GroupBrowseListFragment(),false);
		setContentFragment(new GroupBrowserFragment(),false,null);
		
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			//mFrag = (Fragment)new SampleListFragment();
			mFrag = (Fragment)new MenuFragment();
			//mFrag = new GroupBrowseListFragment();
		} else {
			mFrag = (ListFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}
		setMenuFragment(mFrag, false,null);
		
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setFadeDegree(0.35f);
		sm.setBehindOffset(300);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		getActionBar().setDisplayHomeAsUpEnabled(false);
	} //end onCreate
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setFragment(int containerResId, Fragment fr, boolean isAddToBackStack,String name){
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.replace(containerResId, fr);
		
		if (isAddToBackStack) {
			ft.addToBackStack(name);
		}
		
		ft.commit();
	}
	
	public void setContentFragment(Fragment fr, boolean isAddToBackStack,String name) {
		setFragment(R.id.content_frame,fr,isAddToBackStack,name);
		if (DEBUG) {
			Log.d(TAG,"set content Fragment");
		}
	}
	
	public void setMenuFragment(Fragment fr, boolean isAddToBackStack, String name) {
		setFragment(R.id.menu_frame,fr, isAddToBackStack,name);
		if (DEBUG) {
			Log.d(TAG,"set menu Fragment");
		}
	}

	@Override
	public void onShowLocalContact() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "show local contact", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onShowOldfriendContact() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "show oldfriend contact", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onShowPreferenceMenu() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "show preference menu ", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onShowUserAccount() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "show user account", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showMemberPage(Fragment newFragment) {
		// TODO Auto-generated method stub
		setContentFragment(newFragment, true, GroupMemberPageFragment.FRAGMENT_NAME);
	}
	
	
}
