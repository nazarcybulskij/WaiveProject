package com.example.waive.ui.activity;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.example.waive.utils.DialogUtils;
import com.example.waive.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends Activity {
	
//    private ProfileTracker profileTracker;
//	private CallbackManager callbackManager;
//    private AccessToken accessToken;
//    private AccessTokenTracker accessTokenTracker;

	CallbackManager callbackManager;

	FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
		@Override
		public void onSuccess(LoginResult loginResult) {
			Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(MainActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(FacebookException exception) {
			Toast.makeText(MainActivity.this,"Error"+exception.getMessage(),Toast.LENGTH_SHORT).show();
		}
	};

    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		  ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
			Intent intent = new Intent(this, TabBarActivity.class);
			startActivity(intent);
			finish();
		} 
        
        ImageButton termsButton = (ImageButton)findViewById(R.id.termsButton);
        termsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TermsActivity.class);
				startActivity(intent);
			}
		});
        
        ImageButton signupButton = (ImageButton)findViewById(R.id.signupButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SignupActivity.class);
				startActivity(intent);
			}
		});

        ImageButton signinButton = (ImageButton)findViewById(R.id.signinButton);
        signinButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SigninActivity.class);
				startActivity(intent);
			}
		});

        ImageButton fbButton = (ImageButton)findViewById(R.id.fbButton);
        fbButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "user_friends"));
//				ParseFacebookUtils.logInWithReadPermissionsInBackground(MainActivity.this, Arrays.asList("public_profile", "user_friends"), new LogInCallback() {
//							@Override
//							public void done(ParseUser user, ParseException err) {
//								if (user == null) {
//									Toast.makeText(MainActivity.this,"User Already logged up through Twitter!",Toast.LENGTH_LONG).show();
//								} else if (user.isNew()) {
//
//
//								} else {
//
//									Toast.makeText(MainActivity.this,"User Already logged up through Facebook!",Toast.LENGTH_LONG).show();
//
//
//								}
//							}
//
//						});

			}
		});

		initFacebook();

    }


	private void initFacebook() {
		FacebookSdk.sdkInitialize(getApplicationContext());
		//ParseFacebookUtils.initialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		LoginManager.getInstance().registerCallback(callbackManager,facebookCallback);

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

//
//	private void initializeFaceBook() {
//        FacebookSdk.sdkInitialize(this.getApplicationContext());
//        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
//        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().logOut();
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        accessToken = loginResult.getAccessToken();
//                        GetUserProfile();
//                    }
//
//                    @Override
//                    public void onCancel() {
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                    	Toast.makeText(MainActivity.this, exception.getMessage(),
//		  			              Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        setAccessTokenTracker();
//        setProfileTracker();
//    }
//	 
//	private void setAccessTokenTracker(){
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(
//                    AccessToken oldAccessToken,
//                    AccessToken currentAccessToken) {
//                // Set the access token using
//                // currentAccessToken when it's loaded or set.
//                accessToken = currentAccessToken;
//            }
//        };
//    }
//
//    private void setProfileTracker(){
//        profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
//                 LoginManager.getInstance().logOut();
//            }
//        };
//    }
//    
//	private void GetUserProfile() {
//
//		DialogUtils.displayProgress(this);
//        GraphRequest request = GraphRequest.newMeRequest(
//                accessToken,
//                new GraphRequest.GraphJSONObjectCallback() {
//
////					@Override
////					public void onCompleted(JSONObject object,
////							GraphResponse response) {
////						// TODO Auto-generated method stub
////						JSONObject obj = response.getJSONObject();
////						try {
////
////							facebookId = obj.getString("id");
////							avatar = "https://graph.facebook.com/" + facebookId + "/picture?type=large";
////							firstName = obj.getString("first_name");
////							lastName = obj.getString("last_name");
////							email = obj.getString("email");
////
////							final QBUser user = new QBUser(email, "12345678");
////        		        	user.setEmail(email);
////        		        	user.setFullName(String.format("%s %s", firstName, lastName));
////
////        		        	QBUsers.signUp(user, new QBEntityCallback<QBUser>() {
////        		        	    @Override
////        		        	    public void onSuccess(QBUser user, Bundle args) {
////
////        		        	    	pd.dismiss();
////
////        		        	    	videochat_id = user.getId().toString();
////        		        	    	getFBDetails();
////        		        	    }
////
////        		        	    public void onError(QBResponseException errors) {
////        		        	    	Log.d("error", "123");
////        		        	    }
////
////        						@Override
////        						public void onError(List<String> arg0) {
////        							// TODO Auto-generated method stub
////        							Log.d("error", "456");
////        						}
////
////        						@Override
////        						public void onSuccess() {
////        							// TODO Auto-generated method stub
////        							Log.d("error", "qweqw");
////        						}
////        		        	});
//
////						} catch (JSONException e) {
////							// TODO Auto-generated catch block
////							e.printStackTrace();
////						}
////					}
//
//					@Override
//					public void onCompleted(JSONObject arg0, GraphResponse arg1) {
//
//					}
//                });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,first_name,last_name,email");
//        request.setParameters(parameters);
//        request.executeAsync();
//    }
}
