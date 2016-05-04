package com.example.waive.ui.activity;

import com.example.waive.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity {

	private static final int SPLASH_INTERVAL = 1000;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			runMain();
			super.handleMessage(msg);
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        mHandler.sendEmptyMessageDelayed(0, SPLASH_INTERVAL);
    }
    
    private void runMain(){
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    	finish();
    }
}
