package com.skyfree.transparentscreen.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by dinhtuanthanh on 15/03/2018.
 */

public class Tatservice extends BroadcastReceiver {
    public static final int REQUEST_CODE = 333;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AnimationService.class);
        context.stopService(service);
    }
}