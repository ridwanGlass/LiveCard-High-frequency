package com.example.livecards10;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class LiveCardMenuActivity extends Activity 
{
	public final Handler mHandler = new Handler();
	
	@Override
	public void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		openOptionsMenu();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_one, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.item1:
			post(new Runnable() 
			{
				@Override
				public void run() 
				{
					// TODO Auto-generated method stub
					stopService(new Intent(LiveCardMenuActivity.this, LiveCardService.class));
				}
			});
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onOptionsMenuClosed(Menu menu)
	{
		//super.onOptionsMenuClosed(menu);
		finish();
	}
	
	private void post(Runnable runnable)
	{
		// TODO Auto-generated method stub
		mHandler.post(runnable);
	}
	

}