package com.example.waive.datamodel;

import java.util.ArrayList;
import java.util.Map;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class DataManager {

	private static DataManager 								mInstance = null;
	
	public ArrayList<ParseObject> 							mWaives = null;
	public ArrayList<ParseObject>							mWaivesOfProfile = null;
	public ArrayList<Map <String,ArrayList<ParseObject>>>	mWaivesWeeklyOfProfile = null;
	public ArrayList<Map <String,ArrayList<ParseObject>>>	mWaivesMonthlyOfProfile = null;
	
	public ParseUser										mUser = null;
	public String											mVideoPath = null;
	
	public static DataManager sharedInstance(){
		if(mInstance == null){
			mInstance = new DataManager();
		}
		
		return mInstance;
	}
	
	DataManager(){
		init();
	}

	void init(){
		
		mWaives = new ArrayList<ParseObject>();
		mWaivesOfProfile = new ArrayList<ParseObject>();
		mWaivesWeeklyOfProfile = new ArrayList<Map <String,ArrayList<ParseObject>>>();
		mWaivesMonthlyOfProfile = new ArrayList<Map <String,ArrayList<ParseObject>>>();
	}
	
	public void free(){
		mWaives.removeAll(mWaives);
	}
	
}

