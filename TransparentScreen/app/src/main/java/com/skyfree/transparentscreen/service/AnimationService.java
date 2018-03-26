package com.skyfree.transparentscreen.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import transparentscreen.skyfree.com.transparentscreen.R;

/**
 * Created by dinhtuanthanh on 14/03/2018.
 */

public class AnimationService extends Service {
    private WindowManager wm;
    private WindowManager.LayoutParams wmParams;
    ImageView img;
    private MygroupView myView;
    View v;
    private boolean mRunning;
    private String LOG_TAG = "MyService";
    private String myString;
    private IntentFilter mIntentFilter;
    public static final String mBroadcastAction = "STRING_BROADCAST_ACTION";


    @Override
    public void onCreate() {
        super.onCreate();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastAction);
        myView = new MygroupView(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mRunning) {
            mRunning = true;
            registerReceiver(mReceiver,mIntentFilter);
            LayoutInflater inflater = LayoutInflater.from(this);
            v = inflater.inflate(R.layout.home, myView);
            img = v.findViewById(R.id.imageview_animation_list_filling);
            ((AnimationDrawable) img.getBackground()).start();

            wmParams = new WindowManager.LayoutParams(330, 380,
                    WindowManager.LayoutParams.TYPE_TOAST,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);
            wmParams.x = 0;
            wmParams.y = 0;
            wmParams.gravity = Gravity.TOP | Gravity.START;
            wm = (WindowManager) getSystemService(WINDOW_SERVICE);

            myView.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = wmParams.x;
                            initialY = wmParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_UP:
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            wmParams.x = initialX
                                    + (int) (event.getRawX() - initialTouchX);
                            wmParams.y = initialY
                                    + (int) (event.getRawY() - initialTouchY);
                            wm.updateViewLayout(myView, wmParams);
                            return true;
                    }
                    return false;

                }
            });
            wm.addView(myView, wmParams);
        }
        else {
            Toast.makeText(AnimationService.this, "You turned it on",Toast.LENGTH_LONG).show();
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wm.removeView(myView);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mBroadcastAction)) {
                try {
                    Service service = (Service) AnimationService.this;
                    service.stopSelf();
                }
                catch (Exception e) {}
                Toast.makeText(AnimationService.this, "Đã tắt service", Toast.LENGTH_LONG).show();
            }
        }
    };
}