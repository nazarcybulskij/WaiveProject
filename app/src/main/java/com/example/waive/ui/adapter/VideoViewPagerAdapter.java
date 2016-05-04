package com.example.waive.ui.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import com.example.waive.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoViewPagerAdapter extends PagerAdapter {
	
	private Context mContext;
	private ArrayList<ParseObject> mWaives;
	private int mPosition;
	
	public VideoViewPagerAdapter(Context context, ArrayList<ParseObject> waives) {
        this.mContext = context;
        this.mWaives = waives;
    }
	
	@Override
	public int getCount() {
		return this.mWaives.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);
        
        mPosition = position;
        
        final VideoView videoView = (VideoView)itemView.findViewById(R.id.videoView);
        final TextView progressTextView = (TextView)itemView.findViewById(R.id.progressTextView);
        final ImageView thumbnailImageView = (ImageView)itemView.findViewById(R.id.thumbnailImageView);
        
        ParseFile thumbnailImageFile = this.mWaives.get(mPosition).getParseFile("thumbnail");
        thumbnailImageFile.getDataInBackground(new GetDataCallback() {

            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                thumbnailImageView.setImageBitmap(bitpic);
            }
        });
        
		ParseFile videoFile = this.mWaives.get(mPosition).getParseFile("video");
		videoFile.getDataInBackground(new GetDataCallback(){

			@Override
			public void done(byte[] data, ParseException e) {
				
		        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
		                Environment.DIRECTORY_MOVIES), "waivelength");

				File file = new File(mediaStorageDir.getPath() + File.separator + String.format("waivelength_temp_video%d.mp4",position));
				FileOutputStream stream = null;
				
				try {
					stream = new FileOutputStream(file);
				    stream.write(data);
				    stream.close();

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				videoView.setVideoPath(file.getAbsolutePath());
				videoView.start();  
			}
			
		}, new ProgressCallback(){
			@Override
			public void done(Integer progress) {
				
				progressTextView.setText(String.valueOf(progress) + " %");
				
				if(progress == 100){
					
					progressTextView.setVisibility(View.GONE);
					thumbnailImageView.setVisibility(View.GONE);
					
					mWaives.get(mPosition).addUnique("numberOfViews", ParseUser.getCurrentUser());
					mWaives.get(mPosition).saveInBackground();
				}
			}
		});

		container.addView(itemView);
        return itemView;
    }
	
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
