package com.example.waive;

import com.example.waive.utils.FFMPEGUtils;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseACL;
import android.app.Application;

public class WaivelengthApplication extends Application {
	
    private static WaivelengthApplication mThis;

	@Override
    public void onCreate() {
        super.onCreate();
 
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, Define.YOUR_APPLICATION_ID, Define.YOUR_CLIENT_KEY);
        ParseACL.setDefaultACL(new ParseACL(), true);
        
        mThis = this;
        FFMPEGUtils.initFFMPEG();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
	
    public static WaivelengthApplication getInstance() {
        return mThis;
    }

}
