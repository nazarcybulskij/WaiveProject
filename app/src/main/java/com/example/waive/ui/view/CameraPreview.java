package com.example.waive.ui.view;

import java.util.List;

import com.example.waive.common.media.CameraHelper;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	SurfaceHolder 			mHolder;
	Context					mContext;
	Camera					mCamera;
	public int				mRotation;
	
	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		
		mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
    	prepareCamera(false);
    	refreshCamera();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		refreshCamera();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		refreshCamera();
	}
	
	public void setRotation(int rotation){
		mRotation = rotation;
	}
	
	public Camera getCamera(){
		return mCamera;
	}
	
    public void prepareCamera(boolean isFrontCamera){

        mCamera = isFrontCamera ? CameraHelper.getDefaultFrontFacingCameraInstance() : 
        	CameraHelper.getDefaultBackFacingCameraInstance();

        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalSize = CameraHelper.getOptimalPreviewSize(mSupportedPreviewSizes,
                this.getWidth(), this.getHeight());

        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
        profile.videoFrameWidth = optimalSize.width;
        profile.videoFrameHeight = optimalSize.height;

        parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
        mCamera.setParameters(parameters);
        
//        int cameraId = 0;
//        
//        Camera.CameraInfo info=new Camera.CameraInfo();
//        for (int i=0; i < Camera.getNumberOfCameras(); i++) {
//            Camera.getCameraInfo(i, info);
//            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                cameraId = i;
//            }
//        }
//        
//        Camera.getCameraInfo(cameraId, info);
//        
//        int degrees = 0;
//        switch (mRotation) {
//            case Surface.ROTATION_0: degrees = 0; break;
//            case Surface.ROTATION_90: degrees = 90; break;
//            case Surface.ROTATION_180: degrees = 180; break;
//            case Surface.ROTATION_270: degrees = 270; break;
//        }
//
//        int result;
//        if (isFrontCamera) {
//            result = (info.orientation + degrees) % 360;
//            result = (360 - result) % 360;  // compensate the mirror
//        } else {  // back-facing
//            result = (info.orientation - degrees + 360) % 360;
//        }
        
        mCamera.setDisplayOrientation(90);
    }
    
    public void refreshCamera() {
        
    	if (mHolder.getSurface() == null) {
           return;
        }
        
    	try {
    		mCamera.stopPreview();
    	}        
    	catch (Exception e) {
    	}
        
    	try {
    		mCamera.setPreviewDisplay(mHolder);
    		mCamera.startPreview();
    	}
    	catch (Exception e) {
        
    	}
    }
    
    public void releaseCamera(){
        if (mCamera != null){

        	try {
        		mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
        	}        
        	catch (Exception e) {
        	}
        }
    }

}
