package com.example.waive.ui.activity;

import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.example.waive.R;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class SigninActivity extends Activity {

	private EditText 	mEmailText = null;
	private EditText	mPwText = null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        
        mEmailText = (EditText)findViewById(R.id.emailText);
        mPwText = (EditText)findViewById(R.id.pwText);
        
        ImageButton backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        ImageButton doneButton = (ImageButton)findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClicDone();
			}
		});
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		
		overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
	}

	private void onClicDone(){

		String email = this.mEmailText.getText().toString();
		String password = this.mPwText.getText().toString();
		
		if(email.isEmpty()){

			DialogUtils.showErrorAlert(this, "Email!", "Please enter your email.");
			return;
		}
		
		if(password.isEmpty()){

			DialogUtils.showErrorAlert(this, "Password!", "Please enter a password.");
			return;
		}
		
		if(NetworkUtils.isInternetAvailable(this)){
			
			DialogUtils.displayProgress(this);
			ParseUser.logInInBackground(email, 
					password, 
					new LogInCallback() {
				public void done(ParseUser user, ParseException e) {
					
					DialogUtils.closeProgress();

					if (e == null && user != null) {
				    	 
						Intent intent = new Intent(SigninActivity.this, TabBarActivity.class);
						startActivity(intent);
						finish();
					} else {
						DialogUtils.showErrorAlert(SigninActivity.this, "Error!", e.getMessage());
					}
			   }
			 });
			
		}else{
			
			DialogUtils.showErrorAlert(SigninActivity.this, "No Network!", "You are not connected to internet. Please connect and try again.");
		}
	}
	

}
