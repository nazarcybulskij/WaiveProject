package com.example.waive.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class WaiveView extends View {

	private Paint 	mBackBgPaint = null;
	private Paint 	mSineWaivePaint = null;
	private Paint	mFrontBgPaint = null;
	private Paint	mFrontLinePaint = null;
	
	private Path 	mBgPath = null;
	private Path 	mFrontBgPath = null;
	private Path 	mFrontLinePath = null;
	private Path 	mSineWaivePath = null;
	
	private int 	mWidth = 0;
	private int 	mHeight = 0;
	
	private float	mWaiveScale = 0;
	
	public WaiveView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mBgPath.reset();
		mBgPath.moveTo(0, mHeight/2);
		mBgPath.lineTo((float)mWidth * mWaiveScale, mHeight/2);

		mFrontBgPath.reset();
		mFrontBgPath.moveTo((float)mWidth * mWaiveScale, mHeight/2);
		mFrontBgPath.lineTo(mWidth, mHeight/2);

		canvas.drawPath(mBgPath, mBackBgPaint);
		canvas.drawPath(mSineWaivePath, mSineWaivePaint);
		canvas.drawPath(mFrontBgPath, mFrontBgPaint);
		canvas.drawPath(mFrontLinePath, mFrontLinePaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		mWidth = w;
		mHeight = h;

		if(mBackBgPaint == null){
			mBackBgPaint = new Paint();
		}

		mBackBgPaint.setColor(Color.rgb(128, 195, 190));
		//mBackBgPaint.setPathEffect(new DashPathEffect(new float[] {mWidth/36,mWidth/360}, 1));
		mBackBgPaint.setStrokeWidth(mHeight);
		mBackBgPaint.setStyle(Paint.Style.STROKE);
		
		if(mFrontBgPaint == null){
			mFrontBgPaint = new Paint();
		}

		mFrontBgPaint.setColor(Color.rgb(191, 229, 210));
		//mFrontBgPaint.setPathEffect(new DashPathEffect(new float[] {mWidth/36,mWidth/360}, 1));
		mFrontBgPaint.setStrokeWidth(mHeight);
		mFrontBgPaint.setStyle(Paint.Style.STROKE);

		if(mFrontLinePaint == null){
			mFrontLinePaint = new Paint();
		}

		mFrontLinePaint.setARGB(128, 255, 255, 255);
		mFrontLinePaint.setPathEffect(new DashPathEffect(new float[] {mWidth/300,mWidth/30}, 1));
		mFrontLinePaint.setStrokeWidth(mHeight);
		mFrontLinePaint.setStyle(Paint.Style.STROKE);

		if(mFrontBgPath == null)
			mFrontBgPath = new Path();

		if(mBgPath == null)
			mBgPath = new Path();

		if(mFrontLinePath == null)
			mFrontLinePath = new Path();

		mFrontLinePath.moveTo(0, mHeight/2);
		mFrontLinePath.lineTo(mWidth, mHeight/2);

		mBgPath.moveTo(0, mHeight/2);
		mBgPath.lineTo(mWidth, mHeight/2);
		
		if(mSineWaivePath == null)
			mSineWaivePath = new Path();
		
		mSineWaivePath.moveTo(0, h/2);
		
		Point fp = new Point(0, h/2);
		
		for(int i = 0; i < mWidth/16; i++){
			
			Point cp = null;
			
			if(i % 2 == 0){
				cp = new Point(fp.x + mWidth/16, 0);
			}else{
				cp = new Point(fp.x + mWidth/16, mHeight);
			}
			
			mSineWaivePath.quadTo(cp.x, cp.y, fp.x + mWidth/8, fp.y);
			fp.set(fp.x + mWidth/8,  fp.y);
		}
		
		if(mSineWaivePaint == null)
			mSineWaivePaint = new Paint();
		
		mSineWaivePaint.setColor(Color.WHITE);
		mSineWaivePaint.setPathEffect(new DashPathEffect(new float[] {mWidth/48,mWidth/96}, 1));
		mSineWaivePaint.setStrokeWidth(4);
		mSineWaivePaint.setStyle(Paint.Style.STROKE);
	}
	
	public void setWaiveScale(float waiveScale){
		mWaiveScale = waiveScale;

	}
}
