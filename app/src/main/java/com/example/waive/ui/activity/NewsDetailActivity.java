package com.example.waive.ui.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.view.CircularImageView;
import com.example.waive.ui.view.TextureVideoView;
import com.example.waive.ui.view.TextureVideoView.MediaPlayerListener;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.CountCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.example.waive.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsDetailActivity extends Activity{
	
	private ImageButton 		mPlayButton = null;
	private CircularImageView 	mProfileImageView = null;
	private ImageView			mThumbnailImageview = null;
	private TextView			mFullnameTextView = null;
	private TextView			mViewCntTextView = null;
	private TextView			mCommentCntTextView = null;
	private TextView			mLikeCntTextView = null;
	private TextView			mProgressTextView = null;
	private TextView 			mTimeTextView = null;
	private TextureVideoView	mVideoView = null;
	private boolean				mIsDownloadingVideo = false;
	private ParseObject			mWaive = null;
	private String				mOwnerController = null;
	private boolean				mIsLive = false;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeddetail);

        final int index = getIntent().getIntExtra("index", 0);
        mOwnerController = getIntent().getExtras().getString("ownerController");
        
        mProfileImageView = (CircularImageView)findViewById(R.id.profileImageView);
        mThumbnailImageview = (ImageView)findViewById(R.id.thumbnailImageView);
        mViewCntTextView = (TextView)findViewById(R.id.viewsCntTextView);
        mCommentCntTextView = (TextView)findViewById(R.id.commentCntTextView);
        mLikeCntTextView = (TextView)findViewById(R.id.likeCntTextView);
        mTimeTextView = (TextView)findViewById(R.id.timeTextView);
        mProgressTextView = (TextView)findViewById(R.id.progressTextView);
        mProgressTextView.setVisibility(View.GONE);

        mFullnameTextView = (TextView)findViewById(R.id.detailfullnameTextview);
        mFullnameTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ParseObject waive = mWaive;
				ParseObject user = waive.getParseObject("user");
				
				if(user.equals(ParseUser.getCurrentUser())){
					
					
				}else{
					
				}
			}
		});
        
        mVideoView = (TextureVideoView)findViewById(R.id.videoView);
        mVideoView.setVisibility(View.GONE);
        mVideoView.setScaleType(TextureVideoView.ScaleType.CENTER_CROP);

//        mVideoView.setOnPreparedListener(new OnPreparedListener(){
//
//			@Override
//			public void onPrepared(MediaPlayer mp) {
//				
//			}
//        });
        mVideoView.setListener(new MediaPlayerListener(){

			@Override
			public void onVideoPrepared() {
				
			}

			@Override
			public void onVideoEnd() {
				mVideoView.setVisibility(View.GONE);
				mPlayButton.setVisibility(View.VISIBLE);
				mThumbnailImageview.setVisibility(View.VISIBLE);
			}
        });
        
//        mVideoView.setOnCompletionListener(new OnCompletionListener(){
//
//			@Override
//			public void onCompletion(MediaPlayer mp) {
//				
//			}
//        });
        
        mPlayButton = (ImageButton)findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mPlayButton.setVisibility(View.GONE);
				mProgressTextView.setVisibility(View.VISIBLE);
				downloadVideo();
			}
		});
        
        ImageButton commentButton = (ImageButton)findViewById(R.id.commentButton);
        commentButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewsDetailActivity.this, CommentsActivity.class);
				intent.putExtra("index", index);
				startActivity(intent);
			}
		});
        
        ImageButton likeButton = (ImageButton)findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewsDetailActivity.this, LikesActivity.class);
				intent.putExtra("index", index);
				startActivity(intent);
			}
		});

        ImageButton backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mVideoView.stop();
				//mVideoView.stopPlayback();
				finish();
			}
		});

        ImageButton flagButton = (ImageButton)findViewById(R.id.flagButton);
        flagButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        AlertDialog.Builder ab = new AlertDialog.Builder(NewsDetailActivity.this);
		        ab.setMessage("Are you sure you want to flag this waive as inapproriate?");
		        ab.setNegativeButton("Don't Flag", null);
		        ab.setPositiveButton("Flag", new OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mWaive.addUnique("inAppropriate", ParseUser.getCurrentUser());
						mWaive.saveInBackground();
					}
		        });
		        ab.show();
			}
		});

        if(mOwnerController.equals("N")){
            mWaive = DataManager.sharedInstance().mWaives.get(index);
        }else if(mOwnerController.equals("P")){
        	mWaive = DataManager.sharedInstance().mWaivesOfProfile.get(index);
        }else if(mOwnerController.equals("M")){
        	
        	int monthIndex = getIntent().getExtras().getInt("monthIndex");
        	int waiveIndex = getIntent().getExtras().getInt("index");
    		Map <String,ArrayList<ParseObject>> waivesForMonthDict = DataManager.sharedInstance().mWaivesMonthlyOfProfile.get(monthIndex);
    		String headerString = (String)waivesForMonthDict.keySet().toArray()[0];
    		ArrayList<ParseObject> waivesForMonthArr = waivesForMonthDict.get(headerString);
    		mWaive = waivesForMonthArr.get(waiveIndex);
    		
        }else if(mOwnerController.equals("W")){
        	
        	int monthIndex = getIntent().getExtras().getInt("weekIndex");
        	int waiveIndex = getIntent().getExtras().getInt("index");
    		Map <String,ArrayList<ParseObject>> waivesForWeekDict = DataManager.sharedInstance().mWaivesWeeklyOfProfile.get(monthIndex);
    		String headerString = (String)waivesForWeekDict.keySet().toArray()[0];
    		ArrayList<ParseObject> waivesForWeekArr = waivesForWeekDict.get(headerString);
    		mWaive = waivesForWeekArr.get(waiveIndex);
    		
        }
        
        try {
        	mWaive.fetchIfNeeded();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
        
        ParseObject waiver =  mWaive.getParseObject("user");
        
        try {
			waiver.fetchIfNeeded();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
        
        ParseFile userImageFile = waiver.getParseFile("profileImage");
        userImageFile.getDataInBackground(new GetDataCallback() {

            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                mProfileImageView.setImageBitmap(bitpic);
            }
        });
        
        ParseFile videoThumbnailFile = mWaive.getParseFile("thumbnail");
        videoThumbnailFile.getDataInBackground(new GetDataCallback() {

            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                mThumbnailImageview.setImageBitmap(bitpic);
            }
        });

        mFullnameTextView.setText(waiver.getString("fullName"));
        
        List<ParseObject> numberOfViews = mWaive.getList("numberOfViews");
        if(numberOfViews != null && numberOfViews.size() > 0)
        	mViewCntTextView.setText(numberOfViews.size() + " views");
        else
        	mViewCntTextView.setText("0 views");
        
        List<ParseObject> likingUsers = mWaive.getList("likingUsers");
        if(likingUsers != null && likingUsers.size() > 0)
        	mLikeCntTextView.setText(String.valueOf(likingUsers.size()));
        else
        	mLikeCntTextView.setText("0");
		
		mTimeTextView.setText(mWaive.getInt("duration") + " sec");
    }
	
	@Override
	protected void onResume() {
		super.onResume();

		if(!mIsLive){
			overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
			mIsLive = true;
		}

		if(NetworkUtils.isInternetAvailable(this)){
	        ParseQuery<ParseObject> commentsQuery = ParseQuery.getQuery("Comment");
			commentsQuery.whereEqualTo("waive", mWaive);
			commentsQuery.countInBackground(new CountCallback() {
				@Override
				public void done(int count, ParseException e) {
					mCommentCntTextView.setText(String.valueOf(count));
				}
			 });
		}else{
			DialogUtils.showErrorAlert(this, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
	}
	
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
	}

	void downloadVideo(){
		
		mIsDownloadingVideo = true;
		ParseFile videoFile = mWaive.getParseFile("video");
		videoFile.getDataInBackground(new GetDataCallback(){

			@Override
			public void done(byte[] data, ParseException e) {
				
				File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
		                Environment.DIRECTORY_MOVIES), "waivelength");
				
				File file = new File(mediaStorageDir.getPath() + File.separator + "waivelength_temp_video.mp4");
				FileOutputStream stream;
				
				try {
					stream = new FileOutputStream(file);
				    stream.write(data);
				    stream.close();

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				mProgressTextView.setVisibility(View.GONE);
				mThumbnailImageview.setVisibility(View.GONE);
				mVideoView.setVisibility(View.VISIBLE);
		        mVideoView.setDataSource(file.getAbsolutePath());
		        mVideoView.play();
//				mVideoView.setVideoPath(file.getAbsolutePath());
//				mVideoView.start();  
			}
		}, new ProgressCallback(){
			@Override
			public void done(Integer progress) {
				
				mProgressTextView.setText(progress + " %");
				
				if(progress == 100){
					
					mIsDownloadingVideo = false;
					
					mWaive.addUnique("numberOfViews", ParseUser.getCurrentUser());
					mWaive.saveInBackground();
				}
			}
		});
	}
}
