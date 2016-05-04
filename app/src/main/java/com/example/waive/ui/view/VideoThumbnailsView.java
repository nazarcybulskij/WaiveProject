package com.example.waive.ui.view;

import com.example.waive.ui.interfaces.OnChangeListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class VideoThumbnailsView extends RelativeLayout {

	private static final int THUMBNAILS = 10;
	private MediaMetadataRetriever	mMediaRetriever = null;
	private Context	 				mContext = null;
	private RelativeLayout			mTickFrame = null;
	private ImageView 				mIvTick = null;
	private boolean 				mIsTouchTick = false;
	private float					mDeltaX = 0;
	private float					mWidth = 0;
	private long 					mTotalDurationMs = 0;
	private long 					mDurationMs = 0;
	private int 					mOldThumbIndex = 0;
	private OnChangeListener 		mOnChangeListener = null;
	private Bitmap[]				mThumbnails = null;
	
	public VideoThumbnailsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mMediaRetriever = new MediaMetadataRetriever();
		mContext = context;
		mThumbnails = new Bitmap[10];
	}
	
	public void setVideoSource(String filePath){
		mMediaRetriever.setDataSource(filePath);
	}
	
	public void setOnChangeListener(OnChangeListener listener) {
		mOnChangeListener = listener;
    }
	
	public void loadThumbnails(){
		
		float scale = getContext().getResources().getDisplayMetrics().density;
		mWidth = (int) (336 * scale + 0.5f);
		
		LinearLayout layout = new LinearLayout(mContext);
		layout.setWeightSum(10);
		layout.setBackgroundColor(Color.WHITE);
		
		int w = (int) (331 * scale + 0.5f);
		int h = (int) (40 * scale + 0.5f);
		LayoutParams layoutParams = new LayoutParams(w, h);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.setLayoutParams(layoutParams);
		
		mTotalDurationMs = Long.parseLong(mMediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)); 
		mDurationMs = mTotalDurationMs / 10;

		for(int i = 0; i < THUMBNAILS; i++){
			
			ImageView ivThumb = new ImageView(mContext);
			ivThumb.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
			mThumbnails[i] = mMediaRetriever.getFrameAtTime(mDurationMs * i * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
			
			ivThumb.setBackground(new BitmapDrawable(mThumbnails[i]));
			layout.addView(ivThumb);
		}
		
		this.addView(layout);
		
		mTickFrame = new RelativeLayout(mContext);
		mTickFrame.setPadding(6, 6, 6, 6);
		mTickFrame.setLayoutParams(new LayoutParams((int) (45 * scale + 0.5f), (int) (45 * scale + 0.5f)));
		mTickFrame.setBackgroundColor(Color.rgb(90, 190, 170));
		
		mIvTick = new ImageView(mContext);
		mIvTick.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mTickFrame.addView(mIvTick);
		mIvTick.setBackground(new BitmapDrawable(mThumbnails[mOldThumbIndex]));
		
		this.addView(mTickFrame);
		
		this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				float x1 = event.getX();
				
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:{
						if(x1 > mTickFrame.getX() && x1 < mTickFrame.getX() + mTickFrame.getWidth()){
							mIsTouchTick = true;
							mDeltaX = x1 - mTickFrame.getX();
						}
					}
					break;
				case MotionEvent.ACTION_MOVE:{
					
						if(mIsTouchTick){
							
							if(mTickFrame.getX() < 0){
								mTickFrame.setX(0);
								mIsTouchTick = false;
								mDeltaX = 0;
							}
							else if(mTickFrame.getX() + mTickFrame.getWidth() > mWidth){
								mTickFrame.setX(mWidth - mTickFrame.getWidth());
								mIsTouchTick = false;
								mDeltaX = 0;
							}
							else{
								mTickFrame.setX(x1 - mDeltaX);
							}
							
							float xxx = mTickFrame.getX()/mWidth;
							
							if(mOnChangeListener != null)
								mOnChangeListener.onChange((long)xxx * mTotalDurationMs * 1000);
							
							int i = 0;
							for(; i < 10; i++){
								
								if(xxx > i * 0.1 && xxx < (i+1) * 0.1 ){
									
									break;
								}
							}
							
							if(mOldThumbIndex != i){
								mOldThumbIndex = i;
								mIvTick.setBackground(new BitmapDrawable(mThumbnails[mOldThumbIndex]));
							}
						}
					}
					break;
				case MotionEvent.ACTION_UP:{
						mIsTouchTick = false;
						mDeltaX = 0;
					}
					break;
				}
				
				Log.i("x", String.valueOf(x1));
				return true;
			}
		});
	}
}
