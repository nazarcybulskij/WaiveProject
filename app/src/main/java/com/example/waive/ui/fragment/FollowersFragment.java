package com.example.waive.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.ui.adapter.LikeAdapter;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.example.waive.R;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

public class FollowersFragment extends Fragment {

	private TabBarActivity 			mTab = null;
	private ListView				mListView = null;
	private ParseObject				mWaive = null;
    private LikeAdapter 			mAdapter = null;
    private List<ParseObject> 		mLikes = null;
    private SwipeRefreshLayout 		mSwipeRefreshLayout = null;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTab = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_followers, container, false);	

		ImageButton backButton = (ImageButton)v.findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTab.pop();
				mTab.fragmentReplace(mTab.cur());
			}
		});
		
		mLikes = new ArrayList<ParseObject>();
		mAdapter = new LikeAdapter(mTab, R.layout.row_like, mLikes);
		mListView = (ListView)v.findViewById(R.id.listView1);
		mListView.setAdapter(mAdapter);
		mListView.setDivider(new ColorDrawable(android.R.color.transparent));
		mListView.setDividerHeight(0);

		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

			@Override
			public void onRefresh() {
				refreshLikes();
			}
		});

		DialogUtils.displayProgress(mTab);
		
        return v;
	}
	
	void refreshLikes(){
		
		if(NetworkUtils.isInternetAvailable(mTab)){

			mLikes.removeAll(mLikes);
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Followers");
			query.whereEqualTo("user", DataManager.sharedInstance().mUser);
			query.findInBackground(new FindCallback<ParseObject>(){

				@Override
				public void done(List<ParseObject> objs, ParseException e) {
					
					if(e == null && objs != null && objs.size() > 0){
						ParseObject firstObject = objs.get(0);
						List<ParseObject> followers = firstObject.getList("followers");
						mLikes.addAll(followers);
						mAdapter.notifyDataSetChanged();
					}
					
					DialogUtils.closeProgress();
					mSwipeRefreshLayout.setRefreshing(false);
				}
			});
			
			mAdapter.notifyDataSetChanged();
		}else{
			DialogUtils.showErrorAlert(mTab, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
}
