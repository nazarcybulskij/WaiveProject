package com.example.waive.ui.fragment;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.waive.R;
import com.example.waive.common.media.CameraHelper;
import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.activity.AddNewWaiveActivity;
import com.example.waive.ui.interfaces.OnChangeListener;
import com.example.waive.ui.view.CameraPreview;
import com.example.waive.ui.view.VideoThumbnailsView;
import com.example.waive.ui.view.WaiveView;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.VideoUtils;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecordNewWaiveFragment extends Fragment implements ImageChooserListener{

	private final String TAG = "Recorder";
	private final int TIME_INTERVAL = 100;
    private final int SELECT_VIDEO_REQUEST_CODE = 18795;

	private CameraPreview		mPreview = null;
    private MediaRecorder 		mMediaRecorder = null;
    private WaiveView			mWaiveView = null;
    private RelativeLayout		mTimeLayout = null;
    private TextView			mTimeTextView = null;
    
    private int 				mTimeSinceTimeViewPause = 0;
    private List<String> 		mVideoList = null;
    private boolean 			mIsFacingCamera = false;
    private boolean 			mPresentedVideoLibraryUI = false;
    private boolean 			mIsRecording = false;
    private boolean				mIsPlaying = false;
    private boolean				mRecordedFull30Secs = false;
    private boolean				mStopButtonHeld = false;
    private AddNewWaiveActivity mParent = null;
    
    private Handler 			mTimerHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
	
			if(!mStopButtonHeld){
				mStopButtonHeld = true;
				mTimerHandler.sendEmptyMessageDelayed(0, TIME_INTERVAL);

				mTimeLayout.setVisibility(View.VISIBLE);
				new MediaPrepareTask().execute(null, null, null);

			}else{

				if(mTimeSinceTimeViewPause >= 30000){
					
					mTimerHandler.removeMessages(0);
					mRecordedFull30Secs = true;
					mStopButtonHeld = false;
					
				}else{
					
					mTimeSinceTimeViewPause += TIME_INTERVAL;
					mWaiveView.setWaiveScale((float)mTimeSinceTimeViewPause/30000);
					mWaiveView.invalidate();
					
					mTimeTextView.setText(String.format("%05.2f", (float)mTimeSinceTimeViewPause/1000));
					mTimeLayout.setX((float)mTimeSinceTimeViewPause/30000*mWaiveView.getWidth() - mTimeLayout.getWidth()/2);
					mTimerHandler.sendEmptyMessageDelayed(0, TIME_INTERVAL);
				}
			}
			
			super.handleMessage(msg);
		}
    };
    
    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (prepareVideoRecorder()) {
                mMediaRecorder.start();
                
            } else {
                releaseMediaRecorder();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
            	Toast.makeText(mParent, "Camera Recording Error", Toast.LENGTH_LONG).show();
            }
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.activity_addvideo, container, false);	
		mParent = (AddNewWaiveActivity)getActivity();
		
        ImageButton closeButton = (ImageButton)v.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mVideoList.size() > 0){

			        AlertDialog.Builder ab = new AlertDialog.Builder(mParent);
			        ab.setTitle("Discard Video");
			        ab.setMessage("Would you like to discard the recorded video?");
			        ab.setNegativeButton("No", null);
			        ab.setPositiveButton("Yes", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
			        ab.show();
				}else{
					mParent.finish();
				}
			}
		});

        ImageView recordButton = (ImageView)v.findViewById(R.id.recordButton);
        recordButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == MotionEvent.ACTION_DOWN){
					
					if(!mRecordedFull30Secs){
						
		                mTimerHandler.sendEmptyMessageDelayed(0, TIME_INTERVAL);
					}else{
						DialogUtils.showErrorAlert(mParent, "30 Seconds!", "Waivelength only supports posting videos that are leas than 30 seconds.");
					}
					
				}else if(event.getAction() == MotionEvent.ACTION_UP){

					if (mStopButtonHeld) {
			        	try{
				            mMediaRecorder.stop();  // stop the recording
				            releaseMediaRecorder(); // release the MediaRecorder object
				            mPreview.getCamera().lock();         // take camera access back from MediaRecorder
				            mStopButtonHeld = false;
			        	}catch(RuntimeException e){
			        		
			        	}
			        }
					
					mTimerHandler.removeMessages(0);
		        }

				return true;
			}
		});

        ImageButton rotateButton = (ImageButton)v.findViewById(R.id.rotateCameraButton);
        rotateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mIsFacingCamera = !mIsFacingCamera;
				
				mPreview.releaseCamera();
				mPreview.prepareCamera(mIsFacingCamera);
				mPreview.refreshCamera();
				releaseMediaRecorder();
			}
		});
        
        ImageButton galleryButton = (ImageButton)v.findViewById(R.id.uploadButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chooseVideo();
			}
		});
        
        ImageButton nextButton = (ImageButton)v.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mVideoList.size() > 0){
					
					
					DataManager.sharedInstance().mVideoPath = mVideoList.get(0);
					mParent.goTo(AddNewWaiveActivity.FRAGMENT_TRIM);
				}else{
					DialogUtils.showErrorAlert(mParent, "No Waive", "Please record a waive");
				}
			}
		});
        
        mTimeLayout = (RelativeLayout)v.findViewById(R.id.timeLayout);
        mTimeLayout.setVisibility(View.GONE);
        mTimeTextView = (TextView)v.findViewById(R.id.timeTextView);
        mWaiveView = (WaiveView)v.findViewById(R.id.waiveView);
        
        mVideoList = new ArrayList<String>();
        mPreview = (CameraPreview)v.findViewById(R.id.texture);
        int rotation = mParent.getWindowManager().getDefaultDisplay().getRotation();
        mPreview.setRotation(rotation);
        
        return v;
	}
	
    @Override
	public void onStop() {
		super.onStop();
		
        releaseMediaRecorder();
        mPreview.releaseCamera();
	}

	@Override
	public void onError(String arg0) {
		
		Log.d(TAG, arg0);
	}

	@Override
	public void onImageChosen(ChosenImage arg0) {
		
	}
	
	private void chooseVideo() {
		
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_video)), SELECT_VIDEO_REQUEST_CODE);

	}

    private boolean prepareVideoRecorder(){

        mMediaRecorder = new MediaRecorder();

        mPreview.getCamera().unlock();
        mMediaRecorder.setCamera(mPreview.getCamera());

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
        mMediaRecorder.setProfile(profile);

        String videoPath = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO).toString();
        mVideoList.add(videoPath);
        mMediaRecorder.setOutputFile(videoPath);
        mMediaRecorder.setOrientationHint(90);

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder(){

    	if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mPreview.getCamera().lock();
        }
    }

    void mergeVideos(){
    	
//		try {
//			VideoUtils.trim(Environment.getExternalStoragePublicDirectory(
//			        Environment.DIRECTORY_DOWNLOADS) + File.separator + "20160428_231428.mp4" , "", 0, 3000);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


    }
    
    
    
}
