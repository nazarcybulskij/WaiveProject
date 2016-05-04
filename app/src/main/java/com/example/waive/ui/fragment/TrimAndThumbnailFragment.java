package com.example.waive.ui.fragment;

import com.example.waive.R;
import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.activity.AddNewWaiveActivity;
import com.example.waive.ui.interfaces.OnChangeListener;
import com.example.waive.ui.interfaces.OnTrimChangeListener;
import com.example.waive.ui.view.VideoThumbnailsView;
import com.example.waive.ui.view.VideoTrimView;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.FFMPEGUtils;
import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

public class TrimAndThumbnailFragment extends Fragment{

	AddNewWaiveActivity 	mParent = null;
	VideoView 				mVideoView = null;
	VideoThumbnailsView		mVideoThumbnailsView = null;
	VideoTrimView			mVideoTrimView = null;
	ImageView				mLeftCropBtn = null;
	ImageView				mRightCropBtn = null;
	
	int						mDuration = 0;
	int						mStartTime = 0;
	int 					mEndTime = 0;
	int						mThumbnailTime = 0;
	boolean					mIsPlaying = false;
	String 					mVideoPath = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.activity_trimvideo, container, false);	
		mParent = (AddNewWaiveActivity)getActivity();
		
		mVideoPath = DataManager.sharedInstance().mVideoPath;
		
		mVideoView = (VideoView)v.findViewById(R.id.videoView);
		mVideoView.setVideoPath(mVideoPath);
		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

			@Override
			public void onPrepared(MediaPlayer mp) {
				mDuration = mp.getDuration();
				mStartTime = 0;
				mEndTime = mDuration/1000;
			}
		});
		
		mVideoThumbnailsView = (VideoThumbnailsView)v.findViewById(R.id.videoThumbnails);
		mVideoThumbnailsView.setVideoSource(mVideoPath);
		mVideoThumbnailsView.loadThumbnails();
		mVideoThumbnailsView.setOnChangeListener(new OnChangeListener(){

			@Override
			public void onChange(long miliseconds) {
				
				mVideoView.seekTo((int)miliseconds);
			}
		});
		
		mVideoTrimView = (VideoTrimView)v.findViewById(R.id.videoTrimView);
		mVideoTrimView.setOnChangeListener(new OnTrimChangeListener(){

			@Override
			public void onChange(float start, float end) {
				
				mStartTime = (int)(start * mDuration / 1000);
				mEndTime = (int)(end * mDuration / 1000);
				
				Log.i("TrimAndThumbnailFragment", "start = " + mStartTime + " , end = " + mEndTime);
				float scale = mParent.getResources().getDisplayMetrics().density;
				int w = (int) (13 * scale + 0.5f);

				mLeftCropBtn.setX(mVideoTrimView.getX()+(float)start*mVideoTrimView.getWidth());
				mRightCropBtn.setX(mVideoTrimView.getX()+(float)end*mVideoTrimView.getWidth() - w);
			}
		});
		
		ImageButton playButton = (ImageButton)v.findViewById(R.id.playButton);
		playButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mIsPlaying){
					mIsPlaying = false;
					mVideoView.pause();
				}else{
					mIsPlaying = true;
					mVideoView.start();
				}
			}
		});
		
		ImageButton backButton = (ImageButton)v.findViewById(R.id.closeButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mVideoView.pause();
				mIsPlaying = false;
				mParent.goTo(AddNewWaiveActivity.FRAGMENT_RECORD);
			}
		});

		ImageButton nextButton = (ImageButton)v.findViewById(R.id.nextButton);
		nextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mVideoView.pause();
				mIsPlaying = false;

				if(mStartTime == 0 && mEndTime == mDuration/1000){
					mParent.goTo(AddNewWaiveActivity.FRAGMENT_FILTER);
				}else{
					DialogUtils.displayProgress(mParent);
					FFMPEGUtils.trimVideo(mVideoPath, mStartTime, mEndTime, mCallback);
				}
			}
		});

		mLeftCropBtn = (ImageView)v.findViewById(R.id.leftCropBtm);
		mRightCropBtn = (ImageView)v.findViewById(R.id.rightCropBtm);
		
		float scale = mParent.getResources().getDisplayMetrics().density;
		int x = (int) (13 * scale + 0.5f);
		int w = (int) (494 * scale + 0.5f);
		mLeftCropBtn.setX(x);
		mRightCropBtn.setX(w);
		
        return v;
	}
	
    private final FFMPEGUtils.Callback mCallback = new FFMPEGUtils.Callback() {
        @Override
        public void finished(String file) {
        	
        	DialogUtils.closeProgress();
        	DataManager.sharedInstance().mVideoPath = file;
        	mParent.goTo(AddNewWaiveActivity.FRAGMENT_FILTER);
        }

        @Override
        public void error() {
        	
        	DialogUtils.closeProgress();
        	new AlertDialog.Builder(mParent)
                    .setTitle(R.string.error)
                    .setMessage(R.string.cant_process_video)
                    .show();
        }
    };

}
