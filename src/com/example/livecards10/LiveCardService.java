package com.example.livecards10;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.glass.timeline.LiveCard;

public class LiveCardService extends Service 
{

    private static final String LIVE_CARD_ID = "hello_live_card";

    private LiveCard mLiveCard;
    private LiveCardRenderer mRenderer;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (mLiveCard == null)
        {
            mLiveCard = new LiveCard(this, LIVE_CARD_ID);
            mRenderer = new LiveCardRenderer(this);

            mLiveCard.setDirectRenderingEnabled(true);
            mLiveCard.getSurfaceHolder().addCallback(mRenderer);

            // Display the options menu when the live card is tapped.
            Intent menuIntent = new Intent(this, LiveCardMenuActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));

            mLiveCard.publish(LiveCard.PublishMode.REVEAL);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() 
    {
        if (mLiveCard != null && mLiveCard.isPublished()) 
        {
            mLiveCard.unpublish();
            mLiveCard.getSurfaceHolder().removeCallback(mRenderer);
            mLiveCard = null;
        }
        super.onDestroy();
    }
}
