package com.example.waive.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import com.example.waive.datamodel.BlockedList;
import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.activity.NewsDetailActivity;
import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.ui.adapter.NewsAdapter;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.example.waive.R;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class NewsfeedFragment extends Fragment {
	
	private TabBarActivity 			mTab = null;
	private ListView 				mListView = null;
    private NewsAdapter 			mAdapter = null;
    private SwipeRefreshLayout 		mSwipeRefreshLayout;
    private boolean					mLikeLock = false;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTab = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_newsfeed, container, false);	

		mAdapter = new NewsAdapter(mTab, R.layout.feed_row, R.layout.feed_row1, R.layout.feed_row2, R.layout.feed_row2,
				1, DataManager.sharedInstance().mWaives, null, null, mTab);

		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

			@Override
			public void onRefresh() {
				refreshNewsFeed();
			}
		});
		
		mListView = (ListView)v.findViewById(R.id.lv_post);
		mListView.setDivider(new ColorDrawable(android.R.color.transparent));
		mListView.setDividerHeight(0);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(mTab, NewsDetailActivity.class);
				intent.putExtra("index", position);
				intent.putExtra("ownerController", "N");
				startActivity(intent);
			}
		});
		
		ImageButton settingsButton = (ImageButton)v.findViewById(R.id.settingsButton);
		settingsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTab.goToTab(TabBarActivity.FRAGMENT_SETTINGS);
			}
		});
		
		DialogUtils.displayProgress(mTab);
		
        return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		refreshNewsFeed();
	}

	void refreshNewsFeed(){

		mTab.runOnUiThread(new Runnable() {
		    public void run() {
				if(NetworkUtils.isInternetAvailable(mTab)){
					
					ArrayList<ParseObject> blockedIDs = BlockedList.getBlockedList();
					
					ParseQuery<ParseObject> newsFeedQuery = ParseQuery.getQuery("Waive");
					newsFeedQuery.orderByDescending("createdAt");
					newsFeedQuery.whereNotContainedIn("user", blockedIDs);
					
					newsFeedQuery.findInBackground(new FindCallback<ParseObject>() {
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
									
									DataManager.sharedInstance().mWaives.removeAll(DataManager.sharedInstance().mWaives);
									DataManager.sharedInstance().mWaives.addAll(objs);
								}
								
								mAdapter.notifyDataSetChanged();
								mSwipeRefreshLayout.setRefreshing(false);
								DialogUtils.closeProgress();
								
							}else{

								DialogUtils.showErrorAlert(mTab, "Error", e.getMessage());
							}
						}}
					);
				}else{
					DialogUtils.showErrorAlert(mTab, "No Internet", "You are not connected to internet. Please connect and try again.");
				}
		    }
		});
		
	}
}
