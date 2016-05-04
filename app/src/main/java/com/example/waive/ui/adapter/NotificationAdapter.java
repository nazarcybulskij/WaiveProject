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
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationAdapter extends ArrayAdapter<ParseObject> {

	private List<ParseObject> items;
	private int rsrc;
	private Context context;
	
	public NotificationAdapter(Context context, int resource, List<ParseObject> objects) {
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
		
		RelativeLayout videoThumbLayout = (RelativeLayout)v.findViewById(R.id.videoThumbnailLayout);
		final CircularImageView videoImageView = (CircularImageView)v.findViewById(R.id.videoImageView);

		ParseObject notification = items.get(position);
		final ParseObject fromUser = notification.getParseObject("fromUser");
		ParseObject forWaive = notification.getParseObject("forWaive");

		try {
			fromUser.fetchIfNeeded();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		if(forWaive != null){
			
			ParseObject checkIfFound = null;
			
			try {
				checkIfFound = forWaive.fetchIfNeeded();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
			if(checkIfFound != null){
			
				ParseFile thumbnailFile = forWaive.getParseFile("thumbnail");
				thumbnailFile.getDataInBackground(new GetDataCallback() {

		            @Override
		            public void done(byte[] data, ParseException e) {
		                Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
		                videoImageView.setImageBitmap(bitpic);
		            }
		        });
			}
		}else{
			videoThumbLayout.setVisibility(View.GONE);
		}
		
		final CircularImageView profileImageView = (CircularImageView)v.findViewById(R.id.profileImageView);
		ParseFile profileImageFile = fromUser.getParseFile("profileImage");
		profileImageFile.getDataInBackground(new GetDataCallback() {

            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                profileImageView.setImageBitmap(bitpic);
            }
        });
		
		TextView notificationTextView = (TextView)v.findViewById(R.id.notificationTextview);
		String name = fromUser.getString("fullName");
		String str = null;
		
		if(forWaive != null){
		
			if(notification.getString("type").equals("comment")){
				str = fromUser.getString("fullName") + " commented on your waive.";
			}else{
				str = fromUser.getString("fullName") + " liked your waive.";
			}
		}else{
			str = fromUser.getString("fullName") + " started following you.";
		}
		
		SpannableString mutstr = new SpannableString(str);
		mutstr.setSpan(new RelativeSizeSpan(0.8f), 0, str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mutstr.setSpan(new ForegroundColorSpan(Color.BLACK), 0, name.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mutstr.setSpan(new ForegroundColorSpan(Color.GRAY), name.length(), str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		notificationTextView.setText(mutstr);
		
		return v;
	}
}
