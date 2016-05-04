package com.example.waive.ui.adapter;

import java.util.List;

import com.example.waive.R;
import com.example.waive.ui.view.CircularImageView;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LikeAdapter extends ArrayAdapter<ParseObject> {

	private Context context;
	private List<ParseObject> items;
	private int rsrc;
	
	public LikeAdapter(Context context, int resource, List<ParseObject> objects) {
		super(context, resource, objects);
		
		this.context = context;
		this.items = objects;
		this.rsrc = resource;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {
			LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(rsrc, null);
		} 
		
		ParseObject likingUser = items.get(position);
		
		if(likingUser != null){
		
			try {
				likingUser.fetchIfNeeded();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			final CircularImageView profileImageView = (CircularImageView)v.findViewById(R.id.profileImageView);
			ParseFile thumbnailFile = likingUser.getParseFile("profileImage");
			thumbnailFile.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                    profileImageView.setImageBitmap(bitpic);
                }
            });
			
			TextView fullnameTextView = (TextView)v.findViewById(R.id.like_fullnameTextview);
			fullnameTextView.setText(likingUser.getString("fullName"));
		}

		return v;
	}
}
