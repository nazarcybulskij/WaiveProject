package com.example.waive.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.waive.R;
import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.activity.NewsDetailActivity;
import com.example.waive.ui.activity.MultipleWaivesDetailActivity;
import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.ui.view.CircularImageView;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {
	
	private ArrayList<ParseObject> 	items;
	private ArrayList<Map <String,ArrayList<ParseObject>>>	items1;
	private ArrayList<Map <String,ArrayList<ParseObject>>>	items2;
	private int 					rsrc;
	private int 					rsrc1;
	private int 					rsrc2;
	private int 					rsrc3;
	private Context 				context;
	private TabBarActivity 			tab;
	private int 					zoom;
	private boolean					likeLock;
	
	private class ViewHolder{

	    RelativeLayout body;
	    int position;

	}
	
	public NewsAdapter(Context context, int resource, int resource1, int resource2, 
			int resource3, int zoom, ArrayList<ParseObject> objects, ArrayList<Map <String,ArrayList<ParseObject>>> objects1, 
			ArrayList<Map <String,ArrayList<ParseObject>>> objects2, TabBarActivity tab) {
		
		this.items = objects;
		this.items1 = objects1;
		this.items2 = objects2;
		this.rsrc = resource;
		this.rsrc1 = resource1;
		this.rsrc2 = resource2;
		this.rsrc3 = resource3;
		this.context = context;
		this.tab = tab;
		this.zoom = zoom;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
		
		if (convertView == null) {
			
			LayoutInflater li = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();

			if(zoom == 1){
				if(position%2==0)
					convertView = li.inflate(rsrc, null);
				else
					convertView = li.inflate(rsrc1, null);
				
			}else if(zoom == 2){
				convertView = li.inflate(rsrc2, null);
			}else if(zoom == 3){
				convertView = li.inflate(rsrc3, null);
			}
			
		     holder.body = (RelativeLayout)convertView.findViewById(R.id.numberBody);
		     convertView.setTag(holder);
		}else{
	        holder = (ViewHolder)convertView.getTag();
	    } 

		holder.position = position;
		
		switch(zoom){
		case 1:
			getZoomLevelOneView(holder.body, holder.position);
			break;
		case 2:
			getZoomLevelTwoView(holder.body, holder.position);
			break;
		case 3:
			getZoomLevelThreeView(holder.body, holder.position);
			break;
		}
		
		return convertView;
	}

	@Override
	public int getCount() {
		
		int count = 0;
		
		switch(this.zoom){
		case 1:
			count = this.items.size();
			break;
		case 2:
			count = this.items1.size();
			break;
		case 3:
			count = this.items2.size();
			break;
		}
		
		return count;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setZoom(int zoom){
		this.zoom = zoom;
	}
	
	void getZoomLevelOneView(View v, final int position){
		ParseObject waive = items.get(position);
		ParseObject waiver = waive.getParseObject("user");
		
		if(waive != null){
			
			try {
				waiver.fetchIfNeeded();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			TextView fullnameTextView = (TextView)v.findViewById(R.id.fullnameTextView);
			fullnameTextView.setText(waiver.getString("fullName"));
			fullnameTextView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ParseObject waive = DataManager.sharedInstance().mWaives.get(position);
					ParseObject user = waive.getParseObject("user");
					
					if(user.equals(ParseUser.getCurrentUser())){
						tab.mIsOtherUserProfile = false;
					}else{
						tab.mIsOtherUserProfile = true;
						tab.mUser = (ParseUser)user;
					}

					tab.goToTab(TabBarActivity.FRAGMENT_PROFIEL);
				}
			});
			
			final CircularImageView videoThumbnail = (CircularImageView)v.findViewById(R.id.video_thumbnail);
			ParseFile thumbnailFile = waive.getParseFile("thumbnail");
			thumbnailFile.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                    videoThumbnail.setImageBitmap(bitpic);
                }
            });
			
			TextView timeTextView = (TextView)v.findViewById(R.id.timeTextView);
			timeTextView.setText(String.valueOf(waive.getInt("duration")) + " sec");

			TextView captionTextView = (TextView)v.findViewById(R.id.captionsTextView);
			captionTextView.setText(waive.getString("caption"));
			
			final TextView commentCntTextView = (TextView)v.findViewById(R.id.commentCntTextView);
			ParseQuery<ParseObject> commentsQuery = ParseQuery.getQuery("Comment");
			commentsQuery.whereEqualTo("waive", waive);
			commentsQuery.countInBackground(new CountCallback() {
				@Override
				public void done(int count, ParseException e) {
					commentCntTextView.setText(String.valueOf(count));
				}
			 });
			
			final TextView likeCntTextView = (TextView)v.findViewById(R.id.likeCntTextView);
			final TextView likeThisTextView = (TextView)v.findViewById(R.id.likeThisLabel);
			
			List<ParseObject> likingUsers = waive.getList("likingUsers");
			
			if(likingUsers != null && likingUsers.size() > 0){
				likeCntTextView.setText(String.valueOf(likingUsers.size()));
				
				if(likingUsers.contains(ParseUser.getCurrentUser())){
					likeThisTextView.setText("Unlike");
				}else{
					likeThisTextView.setText("Like This");
				}
			}
			else{
				likeCntTextView.setText("0");
				likeThisTextView.setText("Like This");
			}
			
			likeCntTextView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					if(NetworkUtils.isInternetAvailable(context)){
						
						if(!likeLock){
							likeLock = true;
							
							
							String strNumLikes = likeCntTextView.getText().toString();
							int numLikes = Integer.parseInt(strNumLikes);
							
							if(likeThisTextView.getText().equals("Like This")){
								
								likeThisTextView.setText("Unlike");
								likeCntTextView.setText(String.format("%d", numLikes+1));
								
								final ParseObject waive = DataManager.sharedInstance().mWaives.get(position);
								waive.addUnique("likingUsers", ParseUser.getCurrentUser());
								waive.saveInBackground(new SaveCallback(){

									@Override
									public void done(ParseException e) {

										if(e == null){
											
											ParseObject notificationObject = ParseObject.create("Notification");
											notificationObject.add("fromUser", ParseUser.getCurrentUser());
											notificationObject.add("toUser", waive.getParseUser("user"));
											notificationObject.add("type", "like");
											notificationObject.saveInBackground(new SaveCallback(){

												@Override
												public void done(ParseException e) {
													
													likeLock = false;
												}
											});
										}
									}
								});
							}else{
								
								likeThisTextView.setText("Like This");
								likeCntTextView.setText(String.format("%d", numLikes-1));

								final ParseObject waive = DataManager.sharedInstance().mWaives.get(position);
								
								ParseQuery<ParseObject> query = ParseQuery.getQuery("Waive");
							    query.whereEqualTo("objectId", waive.getObjectId());
							    query.whereEqualTo("likingUsers", ParseUser.getCurrentUser());
							    query.getFirstInBackground(new GetCallback<ParseObject>(){

									@Override
									public void done(ParseObject obj,
											ParseException e) {
										
										if(e == null){
											try {
												obj.delete();
												
												waive.saveInBackground(new SaveCallback(){

													@Override
													public void done(ParseException e) {

														if(e == null){
															
															ParseObject notificationObject = ParseObject.create("Notification");
															notificationObject.add("fromUser", ParseUser.getCurrentUser());
															notificationObject.add("toUser", waive.getParseUser("user"));
															notificationObject.add("type", "like");
															notificationObject.saveInBackground(new SaveCallback(){

																@Override
																public void done(ParseException e) {
																	
																	likeLock = false;
																}
															});
														}
													}
												});
												
											} catch (ParseException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
										}
									}
							    	
							    });
							    

							}
						}
					}else{
						DialogUtils.showErrorAlert(context, "No Internet", "You are not connected to internet. Please connect and try again.");
					}
				}
			});
		}
	}
	
	void getZoomLevelTwoView(View v, final int position){

		Map <String,ArrayList<ParseObject>> waivesForWeekDict = this.items1.get(position);
		final String headerString = (String)waivesForWeekDict.keySet().toArray()[0];
		ArrayList<ParseObject> waivesForWeekArr = waivesForWeekDict.get(headerString);

		TextView headerTextView = (TextView)v.findViewById(R.id.headerTitleTextView);
		headerTextView.setText(headerString);
		
		ImageButton videoButton = (ImageButton)v.findViewById(R.id.videoButton);
		videoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(context, MultipleWaivesDetailActivity.class);
				intent.putExtra("ownerController", "W");
				intent.putExtra("index", position);
				intent.putExtra("wavetime", headerString.replace("Week Of: ", ""));
				context.startActivity(intent);
			}
		});
		
		int counter = 0;
		int lines = 0;
		LinearLayout layout1 = (LinearLayout)v.findViewById(R.id.feedsLayout);
		layout1.setWeightSum((float)this.items1.size());
		
		LinearLayout layout2 = null;
		
		for(int i = 0; i < waivesForWeekArr.size(); i++){
			
			ParseObject waive = waivesForWeekArr.get(i);
			
			if(counter%3 == 0){
				lines++;
				
				layout2 = new LinearLayout(this.context);
				layout2.setWeightSum(3.0f);
				layout2.setOrientation(LinearLayout.HORIZONTAL);
				layout1.addView(layout2, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
			}
			
			LayoutInflater li = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View content = li.inflate(R.layout.feed_video_cell1, null);
			final int index = i;
			
			content.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, NewsDetailActivity.class);
					intent.putExtra("weekIndex", position);
					intent.putExtra("index", index);
					intent.putExtra("ownerController", "W");
					context.startActivity(intent);					
				}
			});
			
			final CircularImageView videoThumbnail = (CircularImageView)content.findViewById(R.id.thumbnailImageView);
			ParseFile thumbnailFile = waive.getParseFile("thumbnail");
			thumbnailFile.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                    videoThumbnail.setImageBitmap(bitpic);
                }
            });
			
			TextView viewsTextView = (TextView)content.findViewById(R.id.viewsTextView);
			
			List<ParseObject> views = waive.getList("numberOfViews");
			if(views == null)
				viewsTextView.setText("0 views");
			else
				viewsTextView.setText(String.valueOf(views.size()) + " views");
			
			layout2.addView(content, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
			
			int remainSpaces = lines * 3 - waivesForWeekArr.size(); 
			if( remainSpaces != 0 && i == waivesForWeekArr.size() - 1){
				
				for(int j = 0; j < remainSpaces; j++){
					RelativeLayout layout3 = new RelativeLayout(this.context);
					layout2.addView(layout3, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
				}
			}
			
			counter++;
		}
	}
	
	void getZoomLevelThreeView(View v, final int position){

		
		Map <String,ArrayList<ParseObject>> waivesForMonthDict = this.items2.get(position);
		final String headerString = (String)waivesForMonthDict.keySet().toArray()[0];
		ArrayList<ParseObject> waivesForMonthArr = waivesForMonthDict.get(headerString);
		int counter = 0;
		int lines = 0;
		
		ImageButton videoButton = (ImageButton)v.findViewById(R.id.videoButton);
		videoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(context, MultipleWaivesDetailActivity.class);
				intent.putExtra("ownerController", "M");
				intent.putExtra("index", position);
				intent.putExtra("wavetime", headerString);
				context.startActivity(intent);
			}
		});

		LinearLayout layout1 = (LinearLayout)v.findViewById(R.id.feedsLayout);
		layout1.setWeightSum((float)this.items1.size());

		TextView headerTextView = (TextView)v.findViewById(R.id.headerTitleTextView);
		headerTextView.setText(headerString);
		
		LinearLayout layout2 = null;
		
		for(int i = 0; i < waivesForMonthArr.size(); i++){
			
			ParseObject waive = waivesForMonthArr.get(i);
			
			if(counter%5 == 0){
				
				lines++;
				
				layout2 = new LinearLayout(this.context);
				layout2.setWeightSum(5.0f);
				layout2.setOrientation(LinearLayout.HORIZONTAL);
				layout1.addView(layout2, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
			}
			
			LayoutInflater li = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View content = li.inflate(R.layout.feed_video_cell2, null);
			
			final int index = i;
			
			content.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, NewsDetailActivity.class);
					intent.putExtra("monthIndex", position);
					intent.putExtra("index", index);
					intent.putExtra("ownerController", "M");
					context.startActivity(intent);					
				}
			});
			
			final CircularImageView videoThumbnail = (CircularImageView)content.findViewById(R.id.thumbnailImageView);
			ParseFile thumbnailFile = waive.getParseFile("thumbnail");
			thumbnailFile.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                    videoThumbnail.setImageBitmap(bitpic);
                }
            });
			
			TextView viewsTextView = (TextView)content.findViewById(R.id.viewsTextView);
			
			List<ParseObject> views = waive.getList("numberOfViews");
			if(views == null)
				viewsTextView.setText("0 views");
			else
				viewsTextView.setText(String.valueOf(views.size()) + " views");

			layout2.addView(content, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
			
			int remainSpaces = lines * 5 - waivesForMonthArr.size(); 
			if( remainSpaces != 0 && i == waivesForMonthArr.size() - 1){
				
				for(int j = 0; j < remainSpaces; j++){
					RelativeLayout layout3 = new RelativeLayout(this.context);
					layout2.addView(layout3, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
				}
			}

			counter++;
		}
	}
}
