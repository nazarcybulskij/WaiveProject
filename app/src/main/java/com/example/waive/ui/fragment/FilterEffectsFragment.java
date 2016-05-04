package com.example.waive.ui.fragment;

import java.util.ArrayList;
import com.example.waive.R;
import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.activity.AddNewWaiveActivity;
import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.ui.adapter.FindPeopleAdapter;
import com.parse.ParseObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class FilterEffectsFragment extends Fragment {
	
	private AddNewWaiveActivity mParent = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.activity_filtervideo, container, false);	
		mParent = (AddNewWaiveActivity)getActivity();

		final String videoPath = DataManager.sharedInstance().mVideoPath;
		VideoView videoView = (VideoView)v.findViewById(R.id.videoView);
		videoView.setVideoPath(videoPath);
		videoView.start();
		
		ImageButton backButton = (ImageButton)v.findViewById(R.id.closeButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mParent.goTo(AddNewWaiveActivity.FRAGMENT_TRIM);
			}
		});
		
		ImageButton nextButton = (ImageButton)v.findViewById(R.id.nextButton);
		nextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mParent.goTo(AddNewWaiveActivity.FRAGMENT_POST);
			}
		});
		
		final RelativeLayout noneButton = (RelativeLayout)v.findViewById(R.id.noneButton);
		final RelativeLayout sepiaButton = (RelativeLayout)v.findViewById(R.id.sepiaButton);
		final RelativeLayout monoButton = (RelativeLayout)v.findViewById(R.id.monoButton);
		final RelativeLayout saturationButton = (RelativeLayout)v.findViewById(R.id.saturationButton);
		final RelativeLayout gammaButton = (RelativeLayout)v.findViewById(R.id.gammaButton);
		final RelativeLayout dimButton = (RelativeLayout)v.findViewById(R.id.dimButton);
		final RelativeLayout amatrokaButton = (RelativeLayout)v.findViewById(R.id.amatrokaButton);
		final RelativeLayout softButton = (RelativeLayout)v.findViewById(R.id.softButton);
		final RelativeLayout hueButton = (RelativeLayout)v.findViewById(R.id.hueButton);

		final TextView noneTextView = (TextView)v.findViewById(R.id.noneTextView);
		final TextView sepiaTextView = (TextView)v.findViewById(R.id.sepiaTextView);
		final TextView monoTextView = (TextView)v.findViewById(R.id.monoTextView);
		final TextView saturationTextView = (TextView)v.findViewById(R.id.saturationTextView);
		final TextView gammaTextView = (TextView)v.findViewById(R.id.gammaTextView);
		final TextView dimTextView = (TextView)v.findViewById(R.id.dimTextView);
		final TextView amatrokaTextView = (TextView)v.findViewById(R.id.amatrokaTextView);
		final TextView softTextView = (TextView)v.findViewById(R.id.softTextView);
		final TextView hueTextView = (TextView)v.findViewById(R.id.hueTextView);
		
		
		noneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_select_bg_color));
				sepiaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				
				noneTextView.setTextColor(mParent.getResources().getColor(R.color.filter_select_text_color));
				sepiaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
			}
		});

		
		sepiaButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_select_bg_color));
				monoButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));

				noneTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_select_text_color));
				monoTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));

			}
		});

		
		monoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_select_bg_color));
				saturationButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));

				noneTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mParent.getResources().getColor(R.color.filter_select_text_color));
				saturationTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));

			}
		});
		
		
		saturationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_select_bg_color));
				gammaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				
				noneTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mParent.getResources().getColor(R.color.filter_select_text_color));
				gammaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));

			}
		});
		
		
		gammaButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_select_bg_color));
				dimButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				
				noneTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_select_text_color));
				dimTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));

			}
		});

		
		dimButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_select_bg_color));
				amatrokaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));

				noneTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mParent.getResources().getColor(R.color.filter_select_text_color));
				amatrokaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));

			}
		});
		
		
		amatrokaButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_select_bg_color));
				softButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				
				noneTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_select_text_color));
				softTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));

			}
		});
		
		
		softButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_select_bg_color));
				hueButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				
				noneTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mParent.getResources().getColor(R.color.filter_select_text_color));
				hueTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));

			}
		});
		
		
		hueButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mParent.getResources().getColor(R.color.filter_select_bg_color));
				
				noneTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mParent.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mParent.getResources().getColor(R.color.filter_select_text_color));

			}
		});

        return v;
	}
	
}
