package com.oldfriend.ui;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("debug", "MainActivity onCreate compeleted");
		// have nothing to do other than go to software
		Button bt = (Button) findViewById(R.id.start_oldfriend);		
		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				startActivity(new Intent(MainActivity.this, OldFriendActivity.class));
			}});
		
	}
	
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
