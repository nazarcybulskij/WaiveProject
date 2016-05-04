package com.example.waive.ui.activity;

import java.util.ArrayList;

import com.example.waive.R;
import com.example.waive.ui.fragment.FilterEffectsFragment;
import com.example.waive.ui.fragment.PostWaiveFragment;
import com.example.waive.ui.fragment.RecordNewWaiveFragment;
import com.example.waive.ui.fragment.TrimAndThumbnailFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class AddNewWaiveActivity extends FragmentActivity {

    public final static int 		FRAGMENT_RECORD = 0;
    public final static int 		FRAGMENT_TRIM = 1;
    public final static int 		FRAGMENT_FILTER = 2;
    public final static int 		FRAGMENT_POST = 3;

	int 							mCurIndex;
    int 							mOldFragmentIndex;
    ArrayList<Integer> 				mArrayPath = null;
    RecordNewWaiveFragment 			mRecordFragment = null;
    TrimAndThumbnailFragment 		mTrimFragment = null;
    FilterEffectsFragment 			mFilterFragment = null;
    PostWaiveFragment 				mPostFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_addnewwaive);
		
		mArrayPath = new ArrayList<Integer>();

		mRecordFragment = new RecordNewWaiveFragment();
		mTrimFragment = new TrimAndThumbnailFragment();
		mFilterFragment = new FilterEffectsFragment();
		mPostFragment = new PostWaiveFragment();
		
        push(FRAGMENT_RECORD);
        fragmentReplace(cur());
	}
	
    public void goTo(int whichtab){
    	switch(whichtab){
    	case FRAGMENT_RECORD:
			push(FRAGMENT_RECORD);
	        fragmentReplace(cur());
    		break;
    	case FRAGMENT_TRIM:
			push(FRAGMENT_TRIM);
	        fragmentReplace(cur());
    		break;
    	case FRAGMENT_FILTER:
			push(FRAGMENT_FILTER);
	        fragmentReplace(cur());
    		break;
    	case FRAGMENT_POST:
			push(FRAGMENT_POST);
	        fragmentReplace(cur());
    		break;
    	}
    }
    
	public void push(int index){
		mArrayPath.add(Integer.valueOf(index));
		mCurIndex = index;
	}
	
	public void pop(){
		mArrayPath.remove(mArrayPath.size() - 1);
		mCurIndex = mArrayPath.get(mArrayPath.size() - 1);
	}
	
	public int cur(){
		return mCurIndex;
	}
	
	public void free(){
		mArrayPath.removeAll(mArrayPath);
	}
	
	public void fragmentReplace(int reqNewFragmentIndex) {
		 
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (reqNewFragmentIndex) {
        case FRAGMENT_RECORD:
        	transaction.replace(R.id.layout_fragment_content, this.mRecordFragment);
        	break;
        case FRAGMENT_TRIM:
        	transaction.replace(R.id.layout_fragment_content, this.mTrimFragment);
        	break;
        case FRAGMENT_FILTER:
        	transaction.replace(R.id.layout_fragment_content, this.mFilterFragment);
        	break;
        case FRAGMENT_POST:
        	transaction.replace(R.id.layout_fragment_content, this.mPostFragment);
            break;
        }

        transaction.commit(); 
    }

}
