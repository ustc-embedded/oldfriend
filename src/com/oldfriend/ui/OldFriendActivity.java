package com.oldfriend.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;



import com.oldfriend.ui.slidingmenu.lib.SlidingMenu;
import com.oldfriend.ui.slidingmenu.lib.app.SlidingFragmentActivity;

public class OldFriendActivity extends SlidingFragmentActivity {
//public class OldFriendActivity extends BaseActivity {
	protected ListFragment mFrag;
	private int mTitleRes = R.string.oldfriend_contact;
/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSlidingMenu().setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new SampleListFragment())
		.commit();
	}
	*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(mTitleRes);
		
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new SampleListFragment())
		.commit();
		
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFrag = new SampleListFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (ListFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}
		
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		//sm.setShadowWidthRes(R.dimen.shadow_width);
		//sm.setShadowDrawable(R.drawable.shadow);
		//sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
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
		//case R.id.github:
		//	Util.goToGitHub(this);
	//		return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
