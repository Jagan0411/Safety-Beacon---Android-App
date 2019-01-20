package com.example.alert;


import android.graphics.SurfaceTexture;
import android.hardware.Camera;

public class Flash{
    private static boolean flashOnOff = false;
        public static Camera camera;
        private static Camera.Parameters params;

        public static boolean getTorch() {
            if (flashOnOff)  // turn off flash
                off();
            else              // turn on flash
                on();
            return flashOnOff;

        }

        private static void on() {
            if (!flashOnOff) {
                if (camera == null || params == null) {
                    try {
                        camera = Camera.open();
                        params = camera.getParameters();
                    } catch (RuntimeException e) {
                        int a = 10;
                    }
                }
                try {
                    params = camera.getParameters();
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(params);
                    camera.setPreviewTexture(new SurfaceTexture(0));
                    camera.startPreview();
                    flashOnOff = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        private static void off() {
            if (camera == null || params == null)
                return;
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            flashOnOff = false;
        }
}

