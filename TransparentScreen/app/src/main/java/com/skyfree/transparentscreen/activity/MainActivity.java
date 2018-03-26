package com.skyfree.transparentscreen.activity;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.skyfree.transparentscreen.service.CameraLiveWallpaper;
import transparentscreen.skyfree.com.transparentscreen.R;

public class MainActivity extends Activity{

    private static final int PERMISSIONS_REQUEST_CAMERA= 454;
    private Context mContext;
    static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    Button btnCamera , btnAnimation,btnLaser;
    private AlphaAnimation buttonClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        addcontrol();
        addevent();

    }

    private void addevent() {

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                checkSelfPermission();
            }
        });
        btnAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
               Intent intent = new Intent(MainActivity.this,AnimationActivity.class);
                startActivity(intent);
            }
        });
        btnLaser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Toast.makeText(MainActivity.this,"Laser",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(mContext, PERMISSION_CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{PERMISSION_CAMERA},
                    PERMISSIONS_REQUEST_CAMERA);
        } else {
            setTransparentWallpaper();
            startWallpaper();
        }
    }

    private void startWallpaper() {
        Intent intent = new Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, CameraLiveWallpaper.class));
        startActivity(intent);

    }

    private void setTransparentWallpaper() {
        startService(new Intent(mContext, CameraLiveWallpaper.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    setTransparentWallpaper();
                    startWallpaper();

                } else {
                    Toast.makeText(mContext, getString(R.string._lease_open_permissions), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void addcontrol() {
        buttonClick = new AlphaAnimation(1F, 0.8F);
        btnCamera = findViewById(R.id.btncamera);
        btnCamera.getBackground().setAlpha(150);
        btnAnimation = findViewById(R.id.btnanimation);
        btnAnimation.getBackground().setAlpha(150);
        btnLaser = findViewById(R.id.btnlaser);
        btnLaser.getBackground().setAlpha(150);
    }

}
