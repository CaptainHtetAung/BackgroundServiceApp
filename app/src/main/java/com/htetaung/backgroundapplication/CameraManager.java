package com.htetaung.backgroundapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

/**
 * Created by HtetAung on 5/18/18.
 */

public class CameraManager implements Camera.PreviewCallback, Camera.ErrorCallback, Camera.PictureCallback {
    private Context context;
    private Camera mCamera;
    private SurfaceTexture mSurface;
    private PictureCapture pictureCapture;

    public CameraManager(Context context) {
        this.context = context;
    }

    private void openCamera() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if(Util.isBackCamera(context)){
                        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);

                    }else {
                        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);

                    }
                } catch (RuntimeException e) {
                    Log.e("ERROR :", "Cannot open camera");
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    if(mCamera != null) {
                        mSurface = new SurfaceTexture(111);
                        mCamera.setPreviewTexture(mSurface);
                        Camera.Parameters params = mCamera.getParameters();
                        mCamera.setParameters(params);
                        mCamera.setPreviewCallback(CameraManager.this);
                        mCamera.setErrorCallback(CameraManager.this);

                        mCamera.startPreview();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    closeCamera();
                }
            }

        }.execute();
    }

    public void takePhoto() {
        openCamera();
    }
    private void closeCamera() {
        if(mCamera != null) {
            mCamera.release();
            mSurface.release();
            mCamera = null;
            mSurface = null;
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        camera.setPreviewCallback(null);
        camera.takePicture(null, null, this);

    }

    @Override
    public void onError(int error, Camera camera) {
        closeCamera();

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        if(pictureCapture!=null){
            pictureCapture.onPictureCapture(Util.getBitmapFromByteArray(data));
        }
        closeCamera();
    }

    public void setPictureCapture(PictureCapture pictureCapture) {
        this.pictureCapture = pictureCapture;
    }

    public interface PictureCapture{
        void onPictureCapture(Bitmap bitmap);
    }
}
