package com.example.waive.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.activity.NewsDetailActivity;
import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.ui.adapter.NewsAdapter;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.example.waive.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProfileFragment extends Fragment {
	
	private TabBarActivity 			mTab = null;
	private ParseUser				mUser = null;
	
	private SwipeRefreshLayout 		mSwipeRefreshLayout;
	private ImageButton 			mFollowButton;
	private ImageView				mProfileImageView = null;
	private TextView				mUsernameTextView = null;
	private TextView				mFullnameTextView = null;
	private TextView				mFollowersTextView = null;
	private TextView				mFollowingsTextView = null;
	private ListView				mListView = null;
	private int						mZoomValue = 0;
	private boolean					mIsFollow = false;
    private NewsAdapter 			mAdapter = null;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTab = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_profile, container, false);	

		mProfileImageView = (ImageView)v.findViewById(R.id.profileImageView);
		mUsernameTextView = (TextView)v.findViewById(R.id.usernameTextView);
		mFullnameTextView = (TextView)v.findViewById(R.id.fullnameTextView);
		
		mFollowersTextView = (TextView)v.findViewById(R.id.followersTextView);
		mFollowersTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mUser != null){
					DataManager.sharedInstance().mUser = mUser;
				}else{
					DataManager.sharedInstance().mUser = ParseUser.getCurrentUser();
				}
				
				mTab.goToTab(TabBarActivity.FRAGMENT_FOLLOWERS);
			}
		});
		
		mFollowingsTextView = (TextView)v.findViewById(R.id.followingTextView);
		mFollowingsTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mUser != null){
					DataManager.sharedInstance().mUser = mUser;
				}else{
					DataManager.sharedInstance().mUser = null;
				}
				
				mTab.goToTab(TabBarActivity.FRAGMENT_FINDPEOPLE);
			}
		});

		ImageButton zoominButton = (ImageButton)v.findViewById(R.id.zoominButton);
		zoominButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mZoomValue > 1){
					mZoomValue--;
					mListView.setAdapter(null);
					mAdapter.setZoom(mZoomValue);
					mListView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
				}
			}
		});
		
		ImageButton zoomoutButton = (ImageButton)v.findViewById(R.id.zoomoutButton);
		zoomoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mZoomValue < 3){
					mZoomValue++;
					mListView.setAdapter(null);
					mAdapter.setZoom(mZoomValue);
					mListView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
				}
			}
		});
		
		ImageButton blockButton = (ImageButton)v.findViewById(R.id.blockButton);
		blockButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});

		mFollowButton = (ImageButton)v.findViewById(R.id.followButton);
		mFollowButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mIsFollow){

					mFollowButton.setBackgroundResource(R.drawable.minus);
					ParseUser.getCurrentUser().addUnique("following", mUser);
					ParseUser.getCurrentUser().saveInBackground(new SaveCallback(){

						@Override
						public void done(ParseException e) {
							if(e == null){
								ParseQuery<ParseObject> followersQuery = ParseQuery.getQuery("Followers");
								followersQuery.whereEqualTo("user", mUser);
								followersQuery.findInBackground(new FindCallback<ParseObject>(){

									@Override
									public void done(List<ParseObject> objs, ParseException e) {
										
										if(e == null){
											objs.get(0).addUnique("followers", ParseUser.getCurrentUser());
											objs.get(0).saveInBackground();
										}
									}
								});
								
								ParseObject notificationObject = ParseObject.create("Notification");
								notificationObject.put("fromUser", ParseUser.getCurrentUser());
								notificationObject.put("toUser", mUser);
								notificationObject.put("type", "following");
								notificationObject.saveInBackground(new SaveCallback(){

									@Override
									public void done(ParseException arg0) {
										int followers = Integer.parseInt(mFollowersTextView.getEditableText().toString());
										followers++;
										mFollowersTextView.setText(String.valueOf(followers));
									}
								});
							}
						}
					});
					
				}else{
					
					mFollowButton.setBackgroundResource(R.drawable.plus);

					
					ParseUser.getCurrentUser().saveInBackground(new SaveCallback(){

						@Override
						public void done(ParseException e) {
							
							if(e == null){
								ParseQuery<ParseObject> followersQuery = ParseQuery.getQuery("Followers");
								followersQuery.whereEqualTo("user", mUser);
								followersQuery.findInBackground(new FindCallback<ParseObject>(){

									@Override
									public void done(List<ParseObject> objs, ParseException e) {
										
										if(e == null){
											
											objs.get(0).saveInBackground();
										}
									}
								});
								
								ParseQuery<ParseObject> notificationQuery = ParseQuery.getQuery("Notification");
								notificationQuery.whereEqualTo("fromUser", ParseUser.getCurrentUser());
								notificationQuery.whereEqualTo("toUser", mUser);
								notificationQuery.whereEqualTo("type", "following");
								notificationQuery.findInBackground(new FindCallback<ParseObject>(){

									@Override
									public void done(List<ParseObject> objs, ParseException e) {
										if(e == null && objs.size() != 0){
											ParseObject.deleteAllInBackground(objs);
											
											int followers = Integer.parseInt(mFollowersTextView.getEditableText().toString());
											followers--;
											mFollowersTextView.setText(String.valueOf(followers));
										}
									}
								});
							}
						}
					});
				}
			}
		});
		
		if(!mTab.mIsOtherUserProfile){
			
			blockButton.setVisibility(View.GONE);
		}
		
		mZoomValue = 1;
		
		
		mAdapter = new NewsAdapter(mTab, R.layout.feed_row, R.layout.feed_row1, 
				R.layout.feed_row2, R.layout.feed_row2, mZoomValue, 
				DataManager.sharedInstance().mWaivesOfProfile, 
				DataManager.sharedInstance().mWaivesWeeklyOfProfile, 
				DataManager.sharedInstance().mWaivesMonthlyOfProfile, mTab);
		
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

			@Override
			public void onRefresh() {
				refreshProfile();
			}
		});

		mListView = (ListView)v.findViewById(R.id.lv_post);
		mListView.setDivider(new ColorDrawable(android.R.color.transparent));
		mListView.setDividerHeight(0);
		mListView.setAdapter(this.mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if(mZoomValue == 1){
					Intent intent = new Intent(mTab, NewsDetailActivity.class);
					intent.putExtra("index", position);
					intent.putExtra("ownerController", "P");
					startActivity(intent);
				}
			}
		});

        return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		DataManager.sharedInstance().mWaivesOfProfile.removeAll(DataManager.sharedInstance().mWaivesOfProfile);
		DataManager.sharedInstance().mWaivesWeeklyOfProfile.removeAll(DataManager.sharedInstance().mWaivesWeeklyOfProfile);
		DataManager.sharedInstance().mWaivesMonthlyOfProfile.removeAll(DataManager.sharedInstance().mWaivesMonthlyOfProfile);
		
		refreshProfile();
	}
	
	void refreshProfile(){
		
		ParseUser forUser = null;
		
		if(mTab.mIsOtherUserProfile){
			
			forUser = mTab.mUser;
			mUser = mTab.mUser;
		}else{
			mUser = null;
			forUser = ParseUser.getCurrentUser();
		}
		
		forUser.fetchIfNeededInBackground(new GetCallback<ParseObject>(){

			@Override
			public void done(ParseObject obj, ParseException e) {
				if(e == null){
					fillUserDetails();
					
				}else{
					
				}
			}
		});
		
		getProfile(forUser);
	}
	
	void getProfile(ParseUser user){
		if(NetworkUtils.isInternetAvailable(mTab)){
			
			ParseQuery<ParseObject> newsFeedQuery = ParseQuery.getQuery("Waive");
			newsFeedQuery.orderByDescending("createdAt");
			newsFeedQuery.whereEqualTo("user", user);
			newsFeedQuery.findInBackground(new FindCallback<ParseObject>() {
			     public void done(List<ParseObject> objects, ParseException e) {
			         if (e == null) {
			        	 
			        	 for(int i = 0; i < objects.size(); i++){
			        		 ParseObject obj = objects.get(i);
			        		 try {
								obj.fetchIfNeeded();
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
			        	 }
			        	 
			     		DataManager.sharedInstance().mWaivesOfProfile.removeAll(DataManager.sharedInstance().mWaivesOfProfile);
			    		DataManager.sharedInstance().mWaivesWeeklyOfProfile.removeAll(DataManager.sharedInstance().mWaivesWeeklyOfProfile);
			    		DataManager.sharedInstance().mWaivesMonthlyOfProfile.removeAll(DataManager.sharedInstance().mWaivesMonthlyOfProfile);
			        	 
			        	 if(objects.size() > 0){
			        		 DataManager.sharedInstance().mWaivesOfProfile.addAll(objects);
				        	 fillWeeklyAndMonthlyArrays();
			        	 }
			        	 
			        	 mSwipeRefreshLayout.setRefreshing(false);
			        	 mAdapter.notifyDataSetChanged();
			        	 
			         } else {
			        	 DialogUtils.showErrorAlert(mTab, "Error", e.getMessage());
			         }
			     }
			 });
		}else{
			DialogUtils.showErrorAlert(mTab, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
	
	void fillUserDetails(){
		
		ParseUser forUser = null;
		
		if(mUser != null){
			
			forUser = mUser;
			mFollowButton.setVisibility(View.VISIBLE);
			
			if(ParseUser.getCurrentUser().getList("following").contains(forUser)){
				mIsFollow = true;
				mFollowButton.setBackgroundResource(R.drawable.minus);
			}else{
				mIsFollow = false;
				mFollowButton.setBackgroundResource(R.drawable.plus);
			}

			ParseQuery<ParseObject> followersQuery = ParseQuery.getQuery("Followers");
			followersQuery.whereEqualTo("user", mUser);
			followersQuery.findInBackground(new FindCallback<ParseObject>(){

				@Override
				public void done(List<ParseObject> objs, ParseException e) {
					
					if(e == null){
						
						if(objs.size() > 0){
							
							List<ParseObject> followers = objs.get(0).getList("followers");
							
							if(followers != null && followers.size() > 0)
								mFollowersTextView.setText(String.valueOf(followers.size()));
							else
								mFollowersTextView.setText("0");
						}
					}
				}
			});

		}else{
			forUser = ParseUser.getCurrentUser();
			mFollowButton.setVisibility(View.GONE);
			
			ParseQuery<ParseObject> followersQuery = ParseQuery.getQuery("Followers");
			followersQuery.whereEqualTo("user", ParseUser.getCurrentUser());
			followersQuery.findInBackground(new FindCallback<ParseObject>(){

				@Override
				public void done(List<ParseObject> objs, ParseException e) {
					
					if(e == null){
						
						if(objs.size() > 0){
							
							List<ParseObject> followers = objs.get(0).getList("followers");
							
							if(followers != null && followers.size() > 0)
								mFollowersTextView.setText(String.valueOf(followers.size()));
							else
								mFollowersTextView.setText("0");
						}
					}
				}
			});
		}
		
		ParseFile userImageFile = forUser.getParseFile("profileImage");
		userImageFile.getDataInBackground(new GetDataCallback() {

            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                mProfileImageView.setImageBitmap(bitpic);
            }
        });
		
		mUsernameTextView.setText(forUser.getString("userWaivelengthName"));
		mFullnameTextView.setText(forUser.getString("fullName"));
		
		List<ParseObject> followings = forUser.getList("following");
		if(followings != null && followings.size() > 0)
			mFollowingsTextView.setText(String.valueOf(followings.size()));
		else
			mFollowingsTextView.setText("0");
	}
	
	void onFollowButton(){

		if(mIsFollow){
			
			mFollowButton.setBackgroundResource(R.drawable.minus);
			
			ParseUser.getCurrentUser().addUnique("following", mUser);
			ParseUser.getCurrentUser().saveInBackground(new SaveCallback(){

				@Override
				public void done(ParseException e) {
					
					if(e == null){
						ParseQuery<ParseObject> follwersQuery = ParseQuery.getQuery("Followers");
						follwersQuery.whereEqualTo("user", mUser);
						follwersQuery.findInBackground(new FindCallback<ParseObject>(){

							@Override
							public void done(List<ParseObject> objs,
									ParseException e) {
								
								if(e == null){
									
									objs.get(0).addUnique("followers", ParseUser.getCurrentUser());
									objs.get(0).saveInBackground();
								}
							}
						});
						
						ParseObject notificationObject = ParseObject.create("Notification");
						notificationObject.put("fromUser", ParseUser.getCurrentUser());
						notificationObject.put("toUser", mUser);
						notificationObject.put("type", "following");
						notificationObject.saveInBackground(new SaveCallback(){

							@Override
							public void done(ParseException e) {
								
								if(e == null){
									int followers = Integer.parseInt(mFollowersTextView.getText().toString());
									mFollowersTextView.setText(String.valueOf(followers));
								}
							}
						});
					}
				}
			});
		}else{
			
			mFollowButton.setBackgroundResource(R.drawable.plus);
			
		}
	}
	
	void fillWeeklyAndMonthlyArrays(){
		
		DataManager.sharedInstance().mWaivesWeeklyOfProfile.removeAll(DataManager.sharedInstance().mWaivesWeeklyOfProfile);
		DataManager.sharedInstance().mWaivesMonthlyOfProfile.removeAll(DataManager.sharedInstance().mWaivesMonthlyOfProfile);
		
		Date firstWaveDate = ((ParseObject)DataManager.sharedInstance().mWaivesOfProfile.get(0)).getCreatedAt();
		Calendar cal = new GregorianCalendar();
		cal.setFirstDayOfWeek(1);
		cal.setTime(firstWaveDate);
		
		int weekOfWaive = cal.get(Calendar.WEEK_OF_YEAR);
		ArrayList<ParseObject> sameWeekWaives = new ArrayList<ParseObject>();
		String weekSectionHeaderString = this.getSectionHeaderString(firstWaveDate, false);
		Map <String,ArrayList<ParseObject>> dict =  new HashMap<String,ArrayList<ParseObject>>();
		
		for(int i = 0; i < DataManager.sharedInstance().mWaivesOfProfile.size(); i++){
			
			ParseObject waive = DataManager.sharedInstance().mWaivesOfProfile.get(i);
			cal.setTime(waive.getCreatedAt());
            
            if(cal.get(Calendar.WEEK_OF_YEAR) == weekOfWaive){
            	
            	sameWeekWaives.add(waive);
            	
            	if(i == DataManager.sharedInstance().mWaivesOfProfile.size() - 1){
            		dict.put(weekSectionHeaderString, sameWeekWaives);
            		DataManager.sharedInstance().mWaivesWeeklyOfProfile.add(dict);
            	}
            }else{

                weekOfWaive = cal.get(Calendar.WEEK_OF_YEAR);
        		dict.put(weekSectionHeaderString, sameWeekWaives);
        		DataManager.sharedInstance().mWaivesWeeklyOfProfile.add(dict);

        		weekSectionHeaderString = this.getSectionHeaderString(waive.getCreatedAt(), false);
        		dict = new HashMap<String,ArrayList<ParseObject>>();
        		sameWeekWaives = new ArrayList<ParseObject>();
        		sameWeekWaives.add(waive);
        		
        		if(i == DataManager.sharedInstance().mWaivesOfProfile.size() - 1){
        			dict.put(weekSectionHeaderString, sameWeekWaives);
        			DataManager.sharedInstance().mWaivesWeeklyOfProfile.add(dict);
        		}
            }
		}
		
		int monthOfWaive = cal.get(Calendar.MONTH);
		ArrayList<ParseObject> sameMonthWaives = new ArrayList<ParseObject>();
		String monthSectionHeaderString = this.getSectionHeaderString(firstWaveDate, true);
		Map <String,ArrayList<ParseObject>> dictMonth =  new HashMap<String,ArrayList<ParseObject>>();
		
		for(int i = 0; i < DataManager.sharedInstance().mWaivesOfProfile.size(); i++){
			
			ParseObject waive = DataManager.sharedInstance().mWaivesOfProfile.get(i);
			cal.setTime(waive.getCreatedAt());
            
            if(cal.get(Calendar.MONTH) == monthOfWaive){
            	
            	sameMonthWaives.add(waive);
            	
            	if(i == DataManager.sharedInstance().mWaivesOfProfile.size() - 1){
            		dictMonth.put(monthSectionHeaderString, sameMonthWaives);
            		DataManager.sharedInstance().mWaivesMonthlyOfProfile.add(dictMonth);
            	}
            }else{

                monthOfWaive = cal.get(Calendar.MONTH);
                dictMonth.put(monthSectionHeaderString, sameMonthWaives);
                DataManager.sharedInstance().mWaivesMonthlyOfProfile.add(dictMonth);

        		monthSectionHeaderString = this.getSectionHeaderString(waive.getCreatedAt(), true);
        		dictMonth = new HashMap<String,ArrayList<ParseObject>>();
        		sameMonthWaives = new ArrayList<ParseObject>();
        		sameMonthWaives.add(waive);
        		
        		if(i == DataManager.sharedInstance().mWaivesOfProfile.size() - 1){
        			dictMonth.put(monthSectionHeaderString, sameMonthWaives);
        			DataManager.sharedInstance().mWaivesMonthlyOfProfile.add(dictMonth);
        		}
            }
		}
		
	}
	
	String getSectionHeaderString(Date date, boolean forMonth){
		
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(1);
		
		Date now = date;
		Date startOfTheWeek;
		Date endOfWeek;

		c.setTime(now);
		
		Calendar c1 = Calendar.getInstance();
		c1.clear();
        c1.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR));
        c1.set(Calendar.YEAR, c.get(Calendar.YEAR));

        startOfTheWeek = c1.getTime();
	    c1.add(Calendar.DATE, 6);
	    endOfWeek = c1.getTime();
	    
	    SimpleDateFormat weekFormatter = null;
	    
	    if(forMonth){
	    	weekFormatter = new SimpleDateFormat("MMMM yyyy");
	    }else{
	    	weekFormatter = new SimpleDateFormat("MMMM d");
	    }
	    
	    weekFormatter.setCalendar(c);
	    
	    if(forMonth){
	    	
	    	return weekFormatter.format(date);
	    }else{
	    	String startOfWeekString = weekFormatter.format(startOfTheWeek);
	    	String endOfWeekString = weekFormatter.format(endOfWeek);
	    	String finalString = "Week Of: " + startOfWeekString + " - " + endOfWeekString;
	    	return finalString;
	    }
	}
}
