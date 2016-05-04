package com.example.waive.ui.view;

import com.example.waive.R;
import com.example.waive.ui.interfaces.OnTrimChangeListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VideoTrimView extends View {

	Paint 	mPaint = null;
	Paint 	mPaint1 = null;
	int 	mWidth = 0;
	int 	mHeight = 0;
	int		mStart = 0;
	int 	mEnd = 0;
	int		mStartX = 0;
	int		mDeltaX = 0;
	int		mSingWidth = 0;
	
	Path	mSelectPortionPath = null;
	Bitmap	mLeftSing = null;
	Bitmap	mRightSing = null;
	float 	mDensity = 0;
	
	Rect	mDstRc = null;
	
	boolean	mIsLeftSing = false;
	boolean	mIsRightSing = false;
	boolean	mIsSing = false;
	
	private OnTrimChangeListener mListener = null;
	
	
	public VideoTrimView(Context context,AttributeSet attrs) {
		super(context, attrs);
		
		mLeftSing = BitmapFactory.decodeResource(getResources(), R.drawable.sing_selected_left);
		mRightSing = BitmapFactory.decodeResource(getResources(), R.drawable.sing_selected_right);
		
		mDensity = context.getResources().getDisplayMetrics().density;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawColor(Color.rgb(186, 242, 224));

		mSelectPortionPath.reset();
		mSelectPortionPath.moveTo(mStart, mHeight/2);
		mSelectPortionPath.lineTo(mEnd, mHeight/2);
		canvas.drawPath(mSelectPortionPath, mPaint);
		
		mDstRc.set(mStart, 0, mStart + mSingWidth, mHeight);
		canvas.drawBitmap(mLeftSing, null, mDstRc, mPaint1);
		
		mDstRc.set(mEnd - mSingWidth, 0, mEnd, mHeight);
		canvas.drawBitmap(mRightSing, null, mDstRc, mPaint1);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		mWidth = w;
		mHeight = h;
		
		mStart = 0;
		mEnd = mWidth;
		
		if(mPaint == null){
			mPaint = new Paint();
		}

		mPaint.setStrokeWidth(mHeight);
		mPaint.setColor(Color.rgb(90, 199, 170));
		mPaint.setStyle(Paint.Style.STROKE);
		
		if(mPaint1 == null){
			mPaint1 = new Paint();
		}
		
		if(mSelectPortionPath == null){
			mSelectPortionPath = new Path();
		}
		
		if(mDstRc == null)
			mDstRc = new Rect();
		
		mSingWidth = (int) (10 * mDensity + 0.5f);

		this.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int x = (int)event.getX();

				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:{
					
						if(x > mStart && x < mStart + mSingWidth){
							
							mIsLeftSing = true;
							mDeltaX = x - mStart;
							
						}else if(x < mEnd && x > mEnd - mSingWidth){

							mIsRightSing = true;
							mDeltaX = mEnd - x;
							
						}else if(x > mStart + mSingWidth && x < mEnd - mSingWidth){
							
							mIsSing = true;
							mStartX = x;
							mDeltaX = x - mStart;
						}
					}
					break;
				case MotionEvent.ACTION_MOVE:
					
					if(mStart < 0){
						
						mStart = 0;
						mStartX = 0;
						mDeltaX = 0;
						mIsLeftSing = false;
						mIsRightSing = false;
						mIsSing = false;
					
					}else if(mEnd > mWidth){
					
						mEnd = mWidth;
						mStartX = 0;
						mDeltaX = 0;
						mIsLeftSing = false;
						mIsRightSing = false;
						mIsSing = false;

					}else{
						if(mIsLeftSing){

							mStart = x - mDeltaX;
							invalidate();
							
						}else if(mIsRightSing){

							mEnd = x + mDeltaX;
							invalidate();
							
						}else if(mIsSing){

							mStart += -(mStartX - x);
							mEnd += -(mStartX - x);
							invalidate();
							
							mStartX = x;
						}
					}

					mListener.onChange((float)mStart/mWidth, (float)mEnd/mWidth);
					
					break;
				case MotionEvent.ACTION_UP:
					
					mListener.onChange((float)mStart/mWidth, (float)mEnd/mWidth);
					
					mStartX = 0;
					mDeltaX = 0;
					mIsLeftSing = false;
					mIsRightSing = false;
					mIsSing = false;
					
					break;
				}
				return true;
			}
		});
	}
	
	public void setOnChangeListener(OnTrimChangeListener listener) {
		mListener = listener;
    }
	
}
