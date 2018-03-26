package com.skyfree.transparentscreen.service;

import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import static android.graphics.PixelFormat.OPAQUE;

public class CameraLiveWallpaper extends WallpaperService {
    @Override
    public Engine onCreateEngine() {


        return new GIFWallpaperEngine();
    }


    private class GIFWallpaperEngine extends Engine {

        private final Handler handler = new Handler();
        private final Runnable viewRunner = new Runnable() {

            @Override
            public void run() {

                draw();

            }

        };

        private boolean visible = true;
        private CameraPreview view;

        public GIFWallpaperEngine(){
            view = new CameraPreview(getBaseContext(), getSurfaceHolder());
            handler.post(viewRunner);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(viewRunner);
            }
            else {
                handler.removeCallbacks(viewRunner);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(viewRunner);
        }

        private void draw() {
            view.surfaceChanged(getSurfaceHolder(), OPAQUE, view.getWidth(), view.getHeight());
            handler.removeCallbacks(viewRunner);
            if (visible) {
                handler.postDelayed(viewRunner, 4000);
            }

        }

    }
}  