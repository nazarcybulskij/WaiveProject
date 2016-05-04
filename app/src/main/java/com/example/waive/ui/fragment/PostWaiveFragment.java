package com.example.waive.ui.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.example.waive.R;
import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.activity.AddNewWaiveActivity;
import com.example.waive.ui.view.CircularImageView;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;
import android.provider.MediaStore.Video.Thumbnails;

public class PostWaiveFragment extends Fragment {

	private boolean 			mIsPlaying = false;
	private int 				mDuration = 0;
	private VideoView 			mVideoView = null;
	private EditText			mCaptionEdit = null;
	private String				mVideoPath = null;
	private AddNewWaiveActivity mParent = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.activity_postvideo, container, false);	
		mParent = (AddNewWaiveActivity)getActivity();
		
		mVideoPath = DataManager.sharedInstance().mVideoPath;
		
		mCaptionEdit = (EditText)v.findViewById(R.id.captionEdit);
		
		mVideoView = (VideoView)v.findViewById(R.id.videoView);
//		mVideoView.setVideoPath(mVideoPath);
//		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//			
//			@Override
//			public void onPrepared(MediaPlayer mp) {
//				mDuration = mp.getDuration();
//			}
//		});
		
		ImageButton backButton = (ImageButton)v.findViewById(R.id.closeButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mIsPlaying){
					mVideoView.stopPlayback();
					mIsPlaying = false;
				}
				
				mParent.goTo(AddNewWaiveActivity.FRAGMENT_FILTER);
			}
		});
		
		ImageButton playButton = (ImageButton)v.findViewById(R.id.playButton);
		playButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!mIsPlaying){
					mVideoView.start();
					mIsPlaying = true;
				}else{
					mVideoView.pause();
					mIsPlaying = false;
				}
			}
		});
		
		
		TextView postButton = (TextView)v.findViewById(R.id.nextButton);
		postButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
	
				if(mIsPlaying){
					mVideoView.stopPlayback();
					mIsPlaying = false;
				}
				
				if(NetworkUtils.isInternetAvailable(mParent)){
					
					DialogUtils.displayProgress(mParent);
					postVideo();
				}else{
					DialogUtils.showErrorAlert(mParent, "No Internet", "You are not connected to internet. Please connect and try again.");
				}
			}
		});
		
		CircularImageView videoThumbnailView = (CircularImageView)v.findViewById(R.id.videoThumbnail);
		Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(mVideoPath, Thumbnails.FULL_SCREEN_KIND);
		videoThumbnailView.setImageBitmap(bmThumbnail);

        return v;
	}

	void postVideo(){
		
		if(NetworkUtils.isInternetAvailable(mParent)){

			ParseObject waive = ParseObject.create("Waive");
			waive.put("user", ParseUser.getCurrentUser());
			
			ParseFile videoFile = new ParseFile(new File(mVideoPath)); 
			waive.put("video", videoFile);
			
			ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
			Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(mVideoPath, Thumbnails.FULL_SCREEN_KIND);
			bmThumbnail.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
			byte[] imageByte = byteArrayOutputStream.toByteArray();

			ParseFile thumbnail = new ParseFile("thumbnail.png", imageByte); 
			waive.put("thumbnail", thumbnail);
			waive.put("duration", mDuration/1000);
			waive.put("caption", mCaptionEdit.getEditableText().toString());

			waive.saveInBackground(new SaveCallback(){

				@Override
				public void done(ParseException e) {
					
					DialogUtils.closeProgress();
					
					if(e != null){
						DialogUtils.showErrorAlert(mParent, "Error!", e.getMessage());
					}else{
						mParent.setResult(Activity.RESULT_OK, null);
						mParent.finish();
					}
				}
			});
			
		}else{
			DialogUtils.closeProgress();
			DialogUtils.showErrorAlert(mParent, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}

}
