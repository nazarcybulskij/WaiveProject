package com.example.waive.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.waive.R;
import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.adapter.VideoViewPagerAdapter;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MultipleWaivesDetailActivity extends Activity {

	int 					mVideoIndex = 0;
	ArrayList<ParseObject> 	mWaives = null;
	VideoViewPagerAdapter	mAdapter = null;
	LinearLayout 			mIndicator = null;
	TextView				mNumViewsTextView = null;
	TextView				mNumCommentsTextView = null;
	TextView				mNumLikesTextView = null;
	ImageView[] 			mDots;
	boolean					mIsLive;
	Context					mContext = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_multifeeddetail);
		
		mContext = this;
		
		String ownerController = getIntent().getExtras().getString("ownerController");
		int index = getIntent().getExtras().getInt("index");
		
		if(ownerController.equals("W")){
			Map <String,ArrayList<ParseObject>> waivesForWeekDict = DataManager.sharedInstance().mWaivesWeeklyOfProfile.get(index);
			mWaives = waivesForWeekDict.get(waivesForWeekDict.keySet().toArray()[0]);
		}else if(ownerController.equals("M")){
			Map <String,ArrayList<ParseObject>> waivesForMonthDict = DataManager.sharedInstance().mWaivesMonthlyOfProfile.get(index);
			mWaives = waivesForMonthDict.get(waivesForMonthDict.keySet().toArray()[0]);
		}
		
		String wavetime = getIntent().getExtras().getString("wavetime");
		TextView wavetimeTextView = (TextView)findViewById(R.id.dateTextView);
		wavetimeTextView.setText(wavetime);
		
		TextView fullnameTextView = (TextView)findViewById(R.id.fullNameTextView);
		
		ImageButton backButton = (ImageButton)findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ImageButton flagButton = (ImageButton)findViewById(R.id.flagButton);
		flagButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
		        ab.setTitle(null);
		        ab.setMessage("Are you sure you want to flag this waive as inappropriate?");
		        ab.setNegativeButton("Don't Flag", null);
		        ab.setPositiveButton("Flag", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						mWaives.get(mVideoIndex).addUnique("inAppropriate", ParseUser.getCurrentUser());
						mWaives.get(mVideoIndex).saveInBackground();
					}
		        });
		        ab.show();

			}
		});
		
		mNumViewsTextView = (TextView)findViewById(R.id.viewsCntTextView);
		mNumCommentsTextView = (TextView)findViewById(R.id.commentCntTextView);
		mNumLikesTextView = (TextView)findViewById(R.id.likeCntTextView);
		
		setUiPageViewController();
		updateNumbers();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(!mIsLive){
			overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
			mIsLive = true;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
	}

	private void updateNumbers(){
	
		if(NetworkUtils.isInternetAvailable(this)){
			
			List<ParseObject> numberOfViews = mWaives.get(mVideoIndex).getList("numberOfViews");
			if(numberOfViews == null)
				mNumViewsTextView.setText("0");
			else
				mNumViewsTextView.setText(String.valueOf(numberOfViews.size()) + " views");
			
			List<ParseObject> likingUsers = mWaives.get(mVideoIndex).getList("likingUsers");
			if(likingUsers == null)
				mNumLikesTextView.setText("0");
			else
				mNumLikesTextView.setText(String.valueOf(likingUsers.size()));
			
			ParseQuery<ParseObject> commentsQuery = ParseQuery.getQuery("Comment");
			commentsQuery.whereEqualTo("waive", mWaives.get(mVideoIndex));
			commentsQuery.countInBackground(new CountCallback(){

				@Override
				public void done(int count, ParseException e) {
					
					if(e == null){
						mNumCommentsTextView.setText(String.valueOf(count));
					}
				}
			});
			
		}else{
			DialogUtils.showErrorAlert(this, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
	
    private void setUiPageViewController() {

		mIndicator = (LinearLayout)findViewById(R.id.viewPagerIndicator);
		
		ViewPager pager = (ViewPager)findViewById(R.id.viewPager);
		
		mAdapter = new VideoViewPagerAdapter(this, mWaives);
		pager.setAdapter(mAdapter);
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				
				mVideoIndex = arg0;
				updateNumbers();
				
				for (int i = 0; i < mWaives.size(); i++) {
		            mDots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
		        }
		 
		        mDots[arg0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));		
			}
		});

        mDots = new ImageView[mWaives.size()];
 
        for (int i = 0; i < mWaives.size(); i++) {
        	mDots[i] = new ImageView(this);
        	mDots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
 
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
 
            params.setMargins(15, 0, 15, 0);
            mIndicator.addView(mDots[i], params);
        }
 
        mDots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }
}
