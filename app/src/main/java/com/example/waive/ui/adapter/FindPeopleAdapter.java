package com.example.waive.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.waive.R;
import com.example.waive.ui.view.CircularImageView;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class FindPeopleAdapter extends BaseAdapter {

	private Context 				context;
	private ArrayList<ParseObject> 	allUsers;
	private ArrayList<ParseObject> 	fbUsers;
	private ArrayList<ParseObject> 	twUsers;
	private ArrayList<ParseObject> 	filteredUsers;
	private int 					searchPlatform;
	private int 					rsrc;
	private int						searchBarTextLength;
	
	public FindPeopleAdapter(Context context, int resource, ArrayList<ParseObject> objects,
			ArrayList<ParseObject> objects1, ArrayList<ParseObject> objects2, ArrayList<ParseObject> objects3,
			int searchPlatform) {
		
		this.context = context;
		this.allUsers = objects;
		this.fbUsers = objects1;
		this.twUsers = objects2;
		this.filteredUsers = objects3;
		this.rsrc = resource;
		this.searchPlatform = searchPlatform;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {
			LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(rsrc, null);
		} 
		
		ArrayList<ParseObject> usersArray = null;
		final ParseUser currentUser = ParseUser.getCurrentUser();
		
		if(this.searchPlatform == 1){
			usersArray = this.allUsers;
		}else if(this.searchPlatform == 2){
			usersArray = this.fbUsers;
		}else{
			usersArray = this.twUsers;
		}
		
		ParseUser user = null;
		
		if(searchBarTextLength > 0){
			user = (ParseUser)filteredUsers.get(position);
		}else{
			user = (ParseUser)usersArray.get(position);
		}
		
		if(user != null){
		
			try {
				user.fetchIfNeeded();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			final CircularImageView profileImageView = (CircularImageView)v.findViewById(R.id.profileImageView);
			ParseFile thumbnailFile = user.getParseFile("profileImage");
			thumbnailFile.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                    profileImageView.setImageBitmap(bitpic);
                }
            });
			
			TextView fullnameTextView = (TextView)v.findViewById(R.id.like_fullnameTextview);
			fullnameTextView.setText(user.getString("fullName"));
			
			final ImageButton followButton = (ImageButton)v.findViewById(R.id.followButton);
			
			if(currentUser.getList("following").contains(user)){
				followButton.setBackgroundResource(R.drawable.follow_checked);
			}else{
				followButton.setBackgroundResource(R.drawable.follow_nml);
			}
			
			final ParseUser user1 = user;
			
			followButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!currentUser.getList("following").contains(user1)){
						
						if(NetworkUtils.isInternetAvailable(context)){
							
							ParseUser user2 = null;
							
							if(searchPlatform == 1){
								user2 = (ParseUser)allUsers.get(position);
							}else if(searchPlatform == 2){
								user2 = (ParseUser)fbUsers.get(position);
							}else{
								user2 = (ParseUser)twUsers.get(position);
							}
							
							final ParseUser user3 = user2;
							followButton.setBackgroundResource(R.drawable.follow_checked);
							
							ParseUser.getCurrentUser().addUnique("following", user2);
							ParseUser.getCurrentUser().saveInBackground(new SaveCallback(){

								@Override
								public void done(ParseException e) {
									if(e == null){
										ParseQuery<ParseObject> followersQuery = ParseQuery.getQuery("Followers");
										followersQuery.whereEqualTo("user", user3);
										followersQuery.findInBackground(new FindCallback<ParseObject>(){

											@Override
											public void done(
													List<ParseObject> objs,
													ParseException e) {
												
												if(e == null){
													objs.get(0).addUnique("followers", ParseUser.getCurrentUser());
													objs.get(0).saveInBackground();
												}
											}
										});
										
										ParseObject notificationObject = ParseObject.create("Notification");
										notificationObject.put("fromUser", ParseUser.getCurrentUser());
										notificationObject.put("toUser", user3);
										notificationObject.put("type", "following");
										notificationObject.saveInBackground(new SaveCallback(){

											@Override
											public void done(ParseException e) {
												if(e == null){
													
												}
											}
										});
									}
								}
							});
						}else{
							DialogUtils.showErrorAlert(context, "No Internet", "You are not connected to internet. Please connect and try again.");
						}
						
					}else{
						
						if(NetworkUtils.isInternetAvailable(context)){
							
							ParseUser user2 = null;
							
							if(searchPlatform == 1){
								user2 = (ParseUser)allUsers.get(position);
							}else if(searchPlatform == 2){
								user2 = (ParseUser)fbUsers.get(position);
							}else{
								user2 = (ParseUser)twUsers.get(position);
							}
							
							final ParseUser user3 = user2;
							followButton.setBackgroundResource(R.drawable.follow_nml);
							
							ParseUser.getCurrentUser().addUnique("following", user2);
							ParseUser.getCurrentUser().saveInBackground(new SaveCallback(){

								@Override
								public void done(ParseException e) {
									if(e == null){
										ParseQuery<ParseObject> followersQuery = ParseQuery.getQuery("Followers");
										followersQuery.whereEqualTo("user", user3);
										followersQuery.findInBackground(new FindCallback<ParseObject>(){

											@Override
											public void done(
													List<ParseObject> objs,
													ParseException e) {
												
												if(e == null){
													objs.get(0).addUnique("followers", ParseUser.getCurrentUser());
													objs.get(0).saveInBackground();
												}
											}
										});
										
										ParseQuery<ParseObject> notificationQuery = ParseQuery.getQuery("Notification");
										notificationQuery.whereEqualTo("fromUser", ParseUser.getCurrentUser());
										notificationQuery.whereEqualTo("toUser", user3);
										notificationQuery.whereEqualTo("type", "following");
										notificationQuery.findInBackground(new FindCallback<ParseObject>(){

											@Override
											public void done(
													List<ParseObject> objs,
													ParseException e) {
												
												if(e == null && objs.size() > 0){
													ParseObject.deleteAllInBackground(objs);
												}
											}
										});
									}
								}
							});
						}else{
							DialogUtils.showErrorAlert(context, "No Internet", "You are not connected to internet. Please connect and try again.");
						}

					}
					
				}
			});
		}

		return v;
	}

	@Override
	public int getCount() {
		
		int rowCount = 0;
		
		if(this.searchPlatform == 1){
			
			if(searchBarTextLength > 0)
				rowCount = this.filteredUsers.size();
			else
				rowCount = this.allUsers.size();
			
		}else if(this.searchPlatform == 2){
			rowCount = this.fbUsers.size();
		}else{
			rowCount = this.twUsers.size();
		}
		
		return rowCount;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public void setFilteredUsers(ArrayList<ParseObject> users){
		this.filteredUsers = users;
	}
	
	public void setSearchBarTextLength(int length){
		searchBarTextLength = length;
	}
}
