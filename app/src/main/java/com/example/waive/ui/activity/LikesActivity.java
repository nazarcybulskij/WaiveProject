package com.example.waive.ui.activity;


import java.util.ArrayList;
import java.util.List;
import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.adapter.LikeAdapter;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.example.waive.R;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class LikesActivity extends Activity {

	private List<ParseObject> 		mLikes = null;
	private ListView				mListView = null;
	private ParseObject				mWaive = null;
    private LikeAdapter 			mAdapter = null;
    private SwipeRefreshLayout 		mSwipeRefreshLayout = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_likes);
		
		int index = getIntent().getIntExtra("index", 0);

		mLikes = new ArrayList<ParseObject>();
		mWaive = DataManager.sharedInstance().mWaives.get(index);

		ImageButton backButton = (ImageButton)findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mAdapter = new LikeAdapter(this, R.layout.row_like, mLikes);
		mListView = (ListView)findViewById(R.id.listView1);
		mListView.setAdapter(mAdapter);
		mListView.setDivider(new ColorDrawable(android.R.color.transparent));
		mListView.setDividerHeight(0);

		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

			@Override
			public void onRefresh() {
				refreshLikes();
			}
		});

		DialogUtils.displayProgress(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();

		refreshLikes();

		overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
	}

	void refreshLikes(){
		
		if(NetworkUtils.isInternetAvailable(this)){
			
			mLikes.removeAll(mLikes);

			try {
				mWaive.fetchIfNeeded();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			List<ParseObject> objs = mWaive.getList("likingUsers");
			
			if(objs != null && objs.size() > 0)
				mLikes.addAll(objs);

			mAdapter.notifyDataSetChanged();
			DialogUtils.closeProgress();
			mSwipeRefreshLayout.setRefreshing(false);
			
		}else{
			DialogUtils.showErrorAlert(this, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
}
