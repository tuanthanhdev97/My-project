package com.skyfree.transparentscreen.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RadioButton;

import com.skyfree.transparentscreen.service.AnimationService;
import transparentscreen.skyfree.com.transparentscreen.R;
import com.skyfree.transparentscreen.service.Tatservice;


public class AnimationActivity extends Activity {
    Button btnOnAnimation , btnback;
    RadioButton radiongang , radiodoc;
    private AlphaAnimation click;
    private IntentFilter mIntentFilter;
    PendingIntent resultItent;
    private String myString;
    NotificationCompat.Builder mBulder;
    AnimationService animationService;
    String txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animationService = new AnimationService();
        setContentView(R.layout.animation_layout);
        addcontrol();
        addEvent();
    }

    private void addEvent() {
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(click);
                finish();
            }
        });

        btnOnAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(click);

                    Intent serviceIntent = new Intent(AnimationActivity.this, AnimationService.class);
                    startService(serviceIntent);
                    mBulder = (NotificationCompat.Builder) new NotificationCompat.Builder(AnimationActivity.this)
                            .setSmallIcon(R.drawable.turtle)
                            .setContentTitle("Transparent Screen")
                            .setContentText("Touch to turn off the effect");
                    Intent stopService = new Intent(AnimationActivity.this, Tatservice.class);
                    resultItent = PendingIntent.getBroadcast(AnimationActivity.this, (int) System.currentTimeMillis(), stopService, PendingIntent.FLAG_CANCEL_CURRENT);
                    mBulder.setContentIntent(resultItent);
                    mBulder.setAutoCancel(true);
                    int mNotification = 001;
                    NotificationManager mNotifi = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifi.notify(mNotification, mBulder.build());

                }


        });

    }
    private void addcontrol() {
        click = new AlphaAnimation(1F, 0.8F);
        radiodoc = findViewById(R.id.radiodoc);
        radiongang = findViewById(R.id.radiongang);
        btnOnAnimation = findViewById(R.id.btnOnAnimation);
        btnOnAnimation.getBackground().setAlpha(150);
        btnback=findViewById(R.id.btnback);
    }
}
