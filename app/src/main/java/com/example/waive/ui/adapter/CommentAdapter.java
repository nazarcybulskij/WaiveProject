package com.example.waive.ui.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.waive.R;
import com.example.waive.ui.view.CircularImageView;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class CommentAdapter extends ArrayAdapter<ParseObject>{

	private Context context;
	private List<ParseObject> items;
	private int rsrc;
	
	public CommentAdapter(Context context, int resource, List<ParseObject> objects) {
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
		
		ParseObject comment = items.get(position);
		ParseObject commentor = comment.getParseObject("user");
		
		if(commentor != null){
		
			try {
				commentor.fetchIfNeeded();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			final CircularImageView profileImageView = (CircularImageView)v.findViewById(R.id.profileImageView);
			ParseFile profileImageFile = commentor.getParseFile("profileImage");
			profileImageFile.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                    profileImageView.setImageBitmap(bitpic);
                }
            });
			
			TextView fullnameTextView = (TextView)v.findViewById(R.id.comment_fullnameTextview);
			fullnameTextView.setText(commentor.getString("fullName"));
			
			TextView commentTextView = (TextView)v.findViewById(R.id.comment_commentTextview);
			commentTextView.setText(comment.getString("comment"));
			
			Date d = comment.getDate("createdAt");
			
		}

		return v;
	}
}
