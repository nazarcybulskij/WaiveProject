package com.example.waive.ui.dialog;

import com.example.waive.R;



import android.app.Dialog;
import android.content.Context;
import android.view.Window;


public class ProgressIndicator extends Dialog {
	public ProgressIndicator(Context context)
	{
		super(context, android.R.style.Theme_Translucent);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.progress_indicator_dialog);
		setCancelable(true);
	}
}