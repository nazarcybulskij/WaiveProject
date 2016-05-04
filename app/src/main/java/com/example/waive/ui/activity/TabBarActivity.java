
package com.example.waive.ui.activity;

import java.util.ArrayList;

import com.example.waive.ui.fragment.FindPeopleFragment;
import com.example.waive.ui.fragment.FollowersFragment;
import com.example.waive.ui.fragment.NewsfeedFragment;
import com.example.waive.ui.fragment.NotificationFragment;
import com.example.waive.ui.fragment.ProfileFragment;
import com.example.waive.ui.fragment.RecordNewWaiveFragment;
import com.example.waive.ui.fragment.SettingsFragment;
import com.example.waive.ui.fragment.TermsFragment;
import com.example.waive.ui.fragment.UserinfoFragment;
import com.parse.ParseUser;
import com.example.waive.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TabBarActivity extends FragmentActivity {

	final String TAG = "TabBarFragmentActivity";
	
    public final static int FRAGMENT_NEWSFEED = 0;
    public final static int FRAGMENT_NOTIFICATION = 1;
    public final static int FRAGMENT_ADDVIDEO = 2;
    public final static int FRAGMENT_PROFIEL = 3;
    public final static int FRAGMENT_SETTINGS = 4;
    public final static int FRAGMENT_USERINFO = 5;
    public final static int FRAGMENT_TERMS = 6;
    public final static int FRAGMENT_FOLLOWERS = 7;
    public final static int FRAGMENT_FINDPEOPLE = 8;
    
    private static final int REQUEST_SHARE = 0;
    
	int 						mCurIndex;
    int 						oldFragmentIndex;

    public ImageView 			mNewsfeedButton = null;
    public ImageView 			mNotificationButton = null;
    public ImageView 			mAddvideoButton = null;
    public ImageView 			mProfileButton = null;
    public ImageView 			mSettingsButton = null;

    public TextView 			mNewsfeedTextView = null;
    public TextView 			mNotificationTextView = null;
    public TextView 			mProfileTextView = null;
    public TextView 			mSettingsTextView = null;

    NewsfeedFragment 			mNewsfeedFragment = null;
    NotificationFragment 		mNotificationFragment = null;
    ProfileFragment 			mProfileFragment = null;
    SettingsFragment 			mSettingsFragment = null;
    UserinfoFragment 			mUserinfoFragment = null;
    TermsFragment 				mTermsFragment = null;
    FollowersFragment			mFollowersFragment = null;
    FindPeopleFragment			mFindPeopleFragment = null;
    
    public ArrayList<Integer> 	mArrayPath = null;
    public ParseUser			mUser = null;
    public boolean				mIsOtherUserProfile;
    
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.activity_tabbar_fragment);

		mArrayPath = new ArrayList<Integer>();
		
		mNewsfeedFragment = new NewsfeedFragment();
		mNotificationFragment = new NotificationFragment();
		mProfileFragment = new ProfileFragment();
		mSettingsFragment = new SettingsFragment();
		mUserinfoFragment = new UserinfoFragment();
		mTermsFragment = new TermsFragment();
		mFollowersFragment = new FollowersFragment();
		mFindPeopleFragment = new FindPeopleFragment();
		
		mNewsfeedTextView = (TextView)findViewById(R.id.tab_newsfeed_text);
		mNotificationTextView = (TextView)findViewById(R.id.tab_notification_text);
		mProfileTextView = (TextView)findViewById(R.id.tab_profile_text);
		mSettingsTextView = (TextView)findViewById(R.id.tab_settings_text);
		
		mNewsfeedButton = (ImageView)findViewById(R.id.tab_newsfeed);
		mNewsfeedButton.setBackgroundResource(R.drawable.newsfeed_pressed);
		mNewsfeedButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					goToTab(FRAGMENT_NEWSFEED);
				}
				
				return false;
			}
		});
        
		mNotificationButton = (ImageView)findViewById(R.id.tab_notification);
		mNotificationButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					
					goToTab(FRAGMENT_NOTIFICATION);
				}

				return false;
			}
		});

		mAddvideoButton = (ImageView)findViewById(R.id.tab_addvideo);
		mAddvideoButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					showShareActivity();
				}

				return false;
			}
		});
        
		mProfileButton = (ImageView)findViewById(R.id.tab_profile);
		mProfileButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					
					mIsOtherUserProfile = false;
					mUser = null;
					goToTab(FRAGMENT_PROFIEL);
				}

				return false;
			}
		});

		mSettingsButton = (ImageView)findViewById(R.id.tab_settings);
		mSettingsButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					
					goToTab(FRAGMENT_SETTINGS);
				}

				return false;
			}
		});
        
        push(FRAGMENT_NEWSFEED);
        fragmentReplace(cur());
        
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
	}
	
    /** Called before the activity is destroyed. */
    @Override
    public void onDestroy() {
        free();
        
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(resultCode == Activity.RESULT_OK){
        	
        	if(requestCode == REQUEST_SHARE){
        		
        		free();
        		push(FRAGMENT_NEWSFEED);
                fragmentReplace(cur());
        		
        	}else{
        	}
        	
        }else if(resultCode == Activity.RESULT_CANCELED){
        	
        }
    }
    
    public void goToTab(int whichtab){
    	switch(whichtab){
    	case FRAGMENT_NEWSFEED:
			mNewsfeedTextView.setTextColor(getResources().getColor(R.color.tab_button_pressed_text_color));
			mNotificationTextView.setTextColor(getResources().getColor(R.color.tab_button_disable_text_color));
			mProfileTextView.setTextColor(getResources().getColor(R.color.tab_button_disable_text_color));
			mSettingsTextView.setTextColor(getResources().getColor(R.color.tab_button_disable_text_color));
			
			mNewsfeedButton.setBackgroundResource(R.drawable.newsfeed_pressed);
			mNotificationButton.setBackgroundResource(R.drawable.notification);
			mProfileButton.setBackgroundResource(R.drawable.profile);
			mSettingsButton.setBackgroundResource(R.drawable.settings_disable);	

			free();
			push(FRAGMENT_NEWSFEED);
	        fragmentReplace(cur());

    		break;
    	case FRAGMENT_NOTIFICATION:
			mNewsfeedTextView.setTextColor(getResources().getColor(R.color.tab_button_disable_text_color));
			mNotificationTextView.setTextColor(getResources().getColor(R.color.tab_button_pressed_text_color));
			mProfileTextView.setTextColor(getResources().getColor(R.color.tab_button_disable_text_color));
			mSettingsTextView.setTextColor(getResources().getColor(R.color.tab_button_disable_text_color));

			mNewsfeedButton.setBackgroundResource(R.drawable.newsfeed);
			mNotificationButton.setBackgroundResource(R.drawable.notification_presed);
			mProfileButton.setBackgroundResource(R.drawable.profile);
			mSettingsButton.setBackgroundResource(R.drawable.settings_disable);	
	        
			free();
			push(FRAGMENT_NOTIFICATION);
	        fragmentReplace(cur());
    		break;
    	case FRAGMENT_PROFIEL:
			mNewsfeedTextView.setTextColor(getResources().getColor(R.color.tab_button_disable_text_color));
			mNotificationTextView.setTextColor(getResources().getColor(R.color.tab_button_disable_text_color));
			mProfileTextView.setTextColor(getResources().getColor(R.color.tab_button_pressed_text_color));
			mSettingsTextView.setTextColor(getResources().getColor(R.color.tab_button_disable_text_color));

			mNewsfeedButton.setBackgroundResource(R.drawable.newsfeed);
			mNotificationButton.setBackgroundResource(R.drawable.notification);
			mProfileButton.setBackgroundResource(R.drawable.profile_pressed);
			mSettingsButton.setBackgroundResource(R.drawable.settings_disable);	
	        
			free();
			push(FRAGMENT_PROFIEL);
	        fragmentReplace(cur());
    		break;
    	case FRAGMENT_SETTINGS:
			mNewsfeedTextView.setTextColor(getResources().getColor(R.color.tab_button_disable_text_color));
			mNotificationTextView.setTextColor(getResources().getColor(R.color.tab_button_disable_text_color));
			mProfileTextView.setTextColor(getResources().getColor(R.color.tab_button_disable_text_color));
			mSettingsTextView.setTextColor(getResources().getColor(R.color.tab_button_pressed_text_color));

			mNewsfeedButton.setBackgroundResource(R.drawable.newsfeed);
			mNotificationButton.setBackgroundResource(R.drawable.notification);
			mProfileButton.setBackgroundResource(R.drawable.profile);
			mSettingsButton.setBackgroundResource(R.drawable.settings_pressed);	
	        
			free();
			push(FRAGMENT_SETTINGS);
	        fragmentReplace(cur());
    		break;
    	case FRAGMENT_USERINFO:
			push(FRAGMENT_USERINFO);
	        fragmentReplace(cur());
    		break;
    	case FRAGMENT_TERMS:
			push(FRAGMENT_TERMS);
	        fragmentReplace(cur());
    		break;
    	case FRAGMENT_FOLLOWERS:
			push(FRAGMENT_FOLLOWERS);
	        fragmentReplace(cur());
    		break;
    	case FRAGMENT_FINDPEOPLE:
			push(FRAGMENT_FINDPEOPLE);
	        fragmentReplace(cur());
    		break;
    	}
    }

	public void push(int index){
		mArrayPath.add(Integer.valueOf(index));
		mCurIndex = index;
	}
	
	public void pop(){
		mArrayPath.remove(mArrayPath.size() - 1);
		mCurIndex = mArrayPath.get(mArrayPath.size() - 1);
	}
	
	public int cur(){
		return mCurIndex;
	}
	
	public void free(){
		mArrayPath.removeAll(mArrayPath);
	}
	
	public void fragmentReplace(int reqNewFragmentIndex) {
		 
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (reqNewFragmentIndex) {
        case FRAGMENT_NEWSFEED:
        	transaction.replace(R.id.layout_fragment_content, this.mNewsfeedFragment);

        	break;
        case FRAGMENT_NOTIFICATION:
        	transaction.replace(R.id.layout_fragment_content, this.mNotificationFragment);
        	break;
        case FRAGMENT_PROFIEL:
        	transaction.replace(R.id.layout_fragment_content, this.mProfileFragment);
        	break;
        case FRAGMENT_SETTINGS:
        	transaction.replace(R.id.layout_fragment_content, this.mSettingsFragment);
            break;
        case FRAGMENT_USERINFO:
        	transaction.replace(R.id.layout_fragment_content, this.mUserinfoFragment);
        	break;
        case FRAGMENT_TERMS:
        	transaction.replace(R.id.layout_fragment_content, this.mTermsFragment);
        	break;
        case FRAGMENT_FOLLOWERS:
        	transaction.replace(R.id.layout_fragment_content, this.mFollowersFragment);
        	break;
        case FRAGMENT_FINDPEOPLE:
        	transaction.replace(R.id.layout_fragment_content, this.mFindPeopleFragment);
        	break;
        }

        transaction.commit(); 
    }
    
    private void showShareActivity(){
    	
		free();
		push(FRAGMENT_ADDVIDEO);
        fragmentReplace(cur());

    	Intent intent = new Intent(this, AddNewWaiveActivity.class);
    	startActivityForResult(intent, REQUEST_SHARE);
    }
}
