package com.example.waive.ui.fragment;

import com.example.waive.Define;
import com.example.waive.ui.activity.MainActivity;
import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.utils.DialogUtils;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.example.waive.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class SettingsFragment extends Fragment {
	
	private TabBarActivity 			mTab = null;
	private ImageButton				mFbConnectButton = null;
	private ImageButton				mTwConnectButton = null;
	private ImageButton				mInstaConnectButton = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTab = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_settings, container, false);	
		
		ImageButton signoutButton = (ImageButton)v.findViewById(R.id.signoutButton);
		signoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ParseUser.logOutInBackground(new LogOutCallback() {

					public void done(ParseException e) {
					
						if (e == null) {
							mTab.finish();
							Intent intent = new Intent(mTab, MainActivity.class);
							startActivity(intent);
							mTab.finish();
						} else {
							DialogUtils.showErrorAlert(mTab, "Error!", e.getMessage());
						}
					}
				});
			}
		});
		
		ImageButton userinfoButton = (ImageButton)v.findViewById(R.id.userinfoButton);
		userinfoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mTab.goToTab(TabBarActivity.FRAGMENT_USERINFO);
			}
		});
		
		ImageButton termsButton = (ImageButton)v.findViewById(R.id.termsButton);
		termsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTab.goToTab(TabBarActivity.FRAGMENT_TERMS);
			}
		});

		mFbConnectButton = (ImageButton)v.findViewById(R.id.fbconnectButton);
		mFbConnectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				SharedPreferences pref = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE); 
				SharedPreferences.Editor editor = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE).edit();
				
				if(!pref.getBoolean("FB", false)){
					mFbConnectButton.setBackgroundResource(R.drawable.fb_checked);
					editor.putBoolean("FB", true);
				}else{
					mFbConnectButton.setBackgroundResource(R.drawable.fb_norm);
					editor.putBoolean("FB", false);
				}

				editor.commit();
			}
		});
		
		mTwConnectButton = (ImageButton)v.findViewById(R.id.twconnectButton);
		mTwConnectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				SharedPreferences pref = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE); 
				SharedPreferences.Editor editor = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE).edit();
				
				if(!pref.getBoolean("TW", false)){
					mTwConnectButton.setBackgroundResource(R.drawable.tw_checked);
					editor.putBoolean("TW", true);
				}else{
					mTwConnectButton.setBackgroundResource(R.drawable.tw_norm);
					editor.putBoolean("TW", false);
				}

				editor.commit();
			}
		});

		mInstaConnectButton = (ImageButton)v.findViewById(R.id.instaconnectButton);
		mInstaConnectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences pref = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE); 
				SharedPreferences.Editor editor = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE).edit();
				
				if(!pref.getBoolean("INST", false)){
					mInstaConnectButton.setBackgroundResource(R.drawable.insta_checked);
					editor.putBoolean("INST", true);
				}else{
					mInstaConnectButton.setBackgroundResource(R.drawable.insta_norms);
					editor.putBoolean("INST", false);
				}

				editor.commit();
			}
		});

		SharedPreferences pref = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE); 
		
		if(!pref.getBoolean("FB", false)){
			mFbConnectButton.setBackgroundResource(R.drawable.fb_norm);
		}else{
			mFbConnectButton.setBackgroundResource(R.drawable.fb_checked);
		}
		
		if(!pref.getBoolean("TW", false)){
			mTwConnectButton.setBackgroundResource(R.drawable.tw_norm);
		}else{
			mTwConnectButton.setBackgroundResource(R.drawable.tw_checked);
		}
		
		if(!pref.getBoolean("INST", false)){
			mInstaConnectButton.setBackgroundResource(R.drawable.insta_norms);
		}else{
			mInstaConnectButton.setBackgroundResource(R.drawable.insta_checked);
		}
		
        return v;
	}
}
