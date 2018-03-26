package com.skyfree.transparentscreen.service;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by dinhtuanthanh on 03/03/2018.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private SurfaceView surfaceView;

    private Camera camera;
    public CameraPreview(Context context) {
        super(context   );
        camera = getCameraInstance();
    }
    public CameraPreview(Context context, SurfaceHolder holder) {

        this(context);
        this.holder = holder;

        holder.addCallback(this);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if(camera == null) {
            camera = getCameraInstance();
        }

        if(camera != null) {
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            }
            catch (IOException e) {
                Log.e("CameraView", "Error setting camera preview: " + e.getMessage());
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        boolean portrait = true;

        if (width > 0 && height >0 && width < height){
            portrait =true;
        } else if (width > 0 && height >0 && width > height){
            portrait = false;
        }

        Camera.Parameters parameters;
        if (camera == null) {
            camera = getCameraInstance();
        }
        if (camera != null){
            parameters = camera.getParameters();
            Camera.Size size = parameters.getPictureSize();
            size = parameters.getPreviewSize();
            parameters.setPreviewSize(size.width, size.height);
            if (portrait) {
                camera.setDisplayOrientation(90);
            } else {
                camera.setDisplayOrientation(180);
            }
            try {
                camera.setParameters(parameters);
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(camera != null) {
            try {
                camera.stopPreview();
                camera.release();
            }
            catch (Exception e) {
                Log.e("CameraView", "Error stopping camera preview: " + e.getMessage());
            }
        }
    }

    private Camera getCameraInstance() {

        Context context = getContext();
        Camera camera =null;
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            try {
                camera = Camera.open();
            }
            catch (Exception e) {
                Log.e("CameraView", "Error getting camera instance: " + e.getMessage());
            }
        }
        else {
            Log.i("CameraView", "No camera found!");
        }
        return camera;
    }
}
