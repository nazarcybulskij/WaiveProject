package com.example.waive.ui.activity;

import java.util.ArrayList;
import java.util.List;
import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.adapter.CommentAdapter;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.example.waive.R;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class CommentsActivity extends Activity {

	private List<ParseObject> 		mComments = null;
	private ListView				mListView = null;
	private SwipeRefreshLayout 		mSwipeRefreshLayout = null;
	private ParseObject				mWaive = null;
    private CommentAdapter 			mAdapter = null;
    private EditText				mCommentText = null;
    private boolean					mIsLive = false;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_comments);
		
		int index = getIntent().getIntExtra("index", 0);

		mComments = new ArrayList<ParseObject>();
		mWaive = DataManager.sharedInstance().mWaives.get(index);

		ImageButton backButton = (ImageButton)findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	
		ImageButton commentButton = (ImageButton)findViewById(R.id.commentButton);
		commentButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onCommentButton();
			}
		});
		
		mCommentText = (EditText)findViewById(R.id.commentText);
		
		mAdapter = new CommentAdapter(this, R.layout.row_comment, mComments);
		mListView = (ListView)findViewById(R.id.listView1);
		mListView.setAdapter(mAdapter);
		mListView.setDivider(new ColorDrawable(android.R.color.transparent));
		mListView.setDividerHeight(0);

		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

			@Override
			public void onRefresh() {
				refreshComments();
			}
		});

		DialogUtils.displayProgress(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();

		refreshComments();

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
	
	void refreshComments(){
		
		if(NetworkUtils.isInternetAvailable(this)){
			
			ParseQuery<ParseObject> commentsQuery = ParseQuery.getQuery("Comment");
			commentsQuery.orderByDescending("createdAt");
			commentsQuery.whereEqualTo("waive", mWaive);
			
			commentsQuery.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> objs, ParseException e) {

					if(e == null){
						
						for(int i = 0; i < objs.size(); i++){
							ParseObject obj = objs.get(i);

							try {
								obj.fetchIfNeeded();
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
						}
						
						if(objs.size() > 0){
							
							mComments.removeAll(mComments);
							mComments.addAll(objs);
							mAdapter.notifyDataSetChanged();
							mSwipeRefreshLayout.setRefreshing(false);
						}

					}else{
						DialogUtils.showErrorAlert(CommentsActivity.this, "Error", e.getMessage());
					}
					
					DialogUtils.closeProgress();
				}}
			);
		}else{
			DialogUtils.showErrorAlert(this, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
	
	void onCommentButton(){
		
		if(NetworkUtils.isInternetAvailable(this)){
		
			DialogUtils.displayProgress(this);
			
			if(!mCommentText.getText().toString().isEmpty()){
				
				final ParseObject comment = ParseObject.create("Comment");
				comment.put("waive", mWaive);
				comment.put("user", ParseUser.getCurrentUser());
				comment.put("comment", mCommentText.getText().toString());
				
				comment.saveInBackground(new SaveCallback(){

					@Override
					public void done(ParseException e) {
						
						if(e == null){
							ParseObject notificationObj = ParseObject.create("Notification");
							notificationObj.put("fromUser", ParseUser.getCurrentUser());
							notificationObj.put("forWaive", mWaive);
							notificationObj.put("toUser", mWaive.getParseObject("user"));
							notificationObj.put("type", "comment");
							notificationObj.saveInBackground(new SaveCallback(){

								@Override
								public void done(ParseException arg0) {
									
								}
							});
							
							DialogUtils.closeProgress();
							mComments.add(0, comment);
							mAdapter.notifyDataSetChanged();
							mCommentText.setText("");
						}
					}
				});
			}
		}else{
			DialogUtils.showErrorAlert(this, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
}
