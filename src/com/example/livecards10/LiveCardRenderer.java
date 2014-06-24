package com.example.livecards10;

import java.util.concurrent.TimeUnit;

import com.google.android.glass.timeline.DirectRenderingCallback;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LiveCardRenderer implements DirectRenderingCallback
{

    private static final String TAG = LiveCardRenderer.class.getSimpleName();

    private static final int REFRESH_RATE_FPS = 33;

    private static final long FRAME_TIME_MILLIS = TimeUnit.SECONDS.toMillis(1) / 
    		REFRESH_RATE_FPS;
    
    private SurfaceHolder mHolder;
    private RenderThread mRenderThread;
    private int mSurfaceWidth;
    private int mSurfaceHeight;

    private final LinearLayout mLayout;

    private final TextView mHelloLiveCardView;
    
    public LiveCardRenderer(Context context)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        mLayout = (LinearLayout) inflater.inflate(R.layout.hello_live_card, null);
        mLayout.setWillNotDraw(false);

        mHelloLiveCardView = (TextView) mLayout.findViewById(R.id.hello_live_card_view);
    }
    
	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		// TODO Auto-generated method stub
	     mHolder = holder;

	     mRenderThread = new RenderThread();
	     mRenderThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
		// TODO Auto-generated method stub
		 mSurfaceWidth = width;
	     mSurfaceHeight = height;
	     
	     doLayout();
	}

	private void doLayout() 
	{
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(mSurfaceWidth,
                View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(mSurfaceHeight,
                View.MeasureSpec.EXACTLY);

        mLayout.measure(measuredWidth, measuredHeight);
        mLayout.layout(0, 0, mLayout.getMeasuredWidth(), mLayout.getMeasuredHeight());
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub
		mRenderThread.quit();
	}

	@Override
	public void renderingPaused(SurfaceHolder arg0, boolean arg1) 
	{
		// TODO Auto-generated method stub
		
	}
	
	 private synchronized void repaint() 
	 {
	     Canvas canvas = null;

	     try 
	     {
	         canvas = mHolder.lockCanvas();
	     } 
	     catch (RuntimeException e) 
	     {
	         Log.d(TAG, "lockCanvas failed", e);
	     }

	     if (canvas != null)
	     {
	        //Show text
	        mHelloLiveCardView.setText("Hello World");
	        
	        doLayout();
	        
	        mLayout.draw(canvas);

	        try
	        {
	            mHolder.unlockCanvasAndPost(canvas);
	        } 
	        catch (RuntimeException e)
	        {
	            Log.d(TAG, "unlockCanvasAndPost failed", e);
	        }
	    }
	}
	
	public class RenderThread extends Thread
	{

		private boolean mShouldRun;

        public RenderThread() 
        {
            mShouldRun = true;
        }
        
        private synchronized boolean shouldRun()
        {
            return mShouldRun;
        }
        
		public synchronized void quit() 
		{
			mShouldRun = false;
		}
		
		@Override
	    public void run()
	    {
			 while(shouldRun())
			 {
				 long frameStart = SystemClock.elapsedRealtime();
	             repaint();
	             long frameLength = SystemClock.elapsedRealtime() - frameStart;

	             long sleepTime = FRAME_TIME_MILLIS - frameLength;
	             if (sleepTime > 0) 
	             {
	                 SystemClock.sleep(sleepTime);
	             }
			 }
	    }
	}
	
}