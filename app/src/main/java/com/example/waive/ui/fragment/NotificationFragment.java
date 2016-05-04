package com.example.waive.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.ui.adapter.NotificationAdapter;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.example.waive.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NotificationFragment extends Fragment {
	
	private TabBarActivity 			mTab = null;
	private List<ParseObject>		mNotifications = null;
    private NotificationAdapter 	mAdapter = null;
    private ListView				mListView = null;
    private SwipeRefreshLayout 		mSwipeRefreshLayout = null;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTab = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_notification, container, false);	
		
		mNotifications = new ArrayList<ParseObject>();
		
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

			@Override
			public void onRefresh() {
				refreshNotifications();
			}
		});

		mAdapter = new NotificationAdapter(mTab, R.layout.row_notification, mNotifications);
		mListView = (ListView)v.findViewById(R.id.listView1);
		mListView.setAdapter(mAdapter);
		mListView.setDivider(new ColorDrawable(android.R.color.transparent));
		mListView.setDividerHeight(0);

		DialogUtils.displayProgress(mTab);

        return v;
	}

	@Override
	public void onResume() {
		
		refreshNotifications();
		
		super.onResume();
	}
	
	void refreshNotifications(){
		
		if(NetworkUtils.isInternetAvailable(mTab)){
		
			mNotifications.removeAll(mNotifications);
			
			ParseQuery<ParseObject> notificationQuery = ParseQuery.getQuery("Notification");
			notificationQuery.whereEqualTo("toUser", ParseUser.getCurrentUser());
			notificationQuery.orderByDescending("createdAt");
			notificationQuery.findInBackground(new FindCallback<ParseObject>(){

				@Override
				public void done(List<ParseObject> objs, ParseException e) {

					if(e == null){
						mNotifications.addAll(objs);
						DialogUtils.closeProgress();
						mAdapter.notifyDataSetChanged();
						mSwipeRefreshLayout.setRefreshing(false);
					}
				}
			});
		}else{
			DialogUtils.showErrorAlert(mTab, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
}
