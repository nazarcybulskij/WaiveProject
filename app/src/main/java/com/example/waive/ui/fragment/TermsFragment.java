package com.example.waive.ui.fragment;


import com.example.waive.ui.activity.TabBarActivity;
import com.joanzapata.pdfview.PDFView;
import com.example.waive.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class TermsFragment extends Fragment {

	private TabBarActivity mTabBar = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTabBar = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.activity_terms, container, false);	

		ImageButton backButton = (ImageButton)v.findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTabBar.pop();
				mTabBar.fragmentReplace(mTabBar.cur());
			}
		});
		
		PDFView pdfView = (PDFView)v.findViewById(R.id.pdfView);
        pdfView.fromAsset("Terms.pdf")
	    .pages(0, 2, 1, 3, 3, 3)
	    .defaultPage(1)
	    .swipeVertical(true)
	    .showMinimap(false)
	    .enableSwipe(true)
	    .load();
		
        return v;
	}
}
