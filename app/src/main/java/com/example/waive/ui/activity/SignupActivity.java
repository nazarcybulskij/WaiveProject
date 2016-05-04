package com.example.waive.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.example.waive.ui.view.CircularImageView;
import com.example.waive.utils.BitmapUtils;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.example.waive.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class SignupActivity extends Activity implements ImageChooserListener{

	private static final int REQUEST_TAKE_PICTURE = 1;  
	
	private CircularImageView 	mAddphotoButton = null;
	private EditText 			mEmailText = null;
	private EditText			mFullnameText = null;
	private EditText			mUsernameText = null;
	private EditText			mPwText = null;
	private EditText			mRepwText = null;
	private int					mChooserType = 0;
	private Uri 				mImageUri = null;
	private File 				mPhotoFile = null;
	private Bitmap				mBitmap = null;
	private ImageChooserManager mImageChooserManager = null;
	private String 				mFilePath = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        mEmailText = (EditText)findViewById(R.id.emailText);
        mFullnameText = (EditText)findViewById(R.id.fullnameText);
        mUsernameText = (EditText)findViewById(R.id.usernameText);
        mPwText = (EditText)findViewById(R.id.pwText);
        mRepwText = (EditText)findViewById(R.id.repwText);
        
//        mEmailText.setText("alexandrov1987@outlook.com");
//        mFullnameText.setText("Nazar Alexandrov");
//        mUsernameText.setText("alexandrov1987");
//        mPwText.setText("1");
//        mRepwText.setText("1");
        
        mAddphotoButton = (CircularImageView)findViewById(R.id.addPhotoButton);
        mAddphotoButton.setBorderColor(Color.WHITE);
        mAddphotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickAddPhoto();
			}
		});
        
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


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
	    case REQUEST_TAKE_PICTURE:
	        if (resultCode == Activity.RESULT_OK) {
	            
	        	StoreImage(this, mImageUri, mPhotoFile);
	        	mBitmap = BitmapUtils.decodeFile(mPhotoFile);
	        	mAddphotoButton.setImageBitmap(this.mBitmap);
	        }
	        break;
	    case ChooserType.REQUEST_PICK_PICTURE:
	    	if (mImageChooserManager == null) {
				reinitializeImageChooser();
			}
	    	mImageChooserManager.submit(requestCode, data);
			break;
	    }
	}

	
	private void onClicDone(){
		
		if(this.mEmailText.getText().toString().isEmpty()){

			DialogUtils.showErrorAlert(this, "Email!", "Please enter your email.");
			return;
		}
		
		if(this.mFullnameText.getText().toString().isEmpty()){

			DialogUtils.showErrorAlert(this, "Full Name!", "Please enter your full name.");
			return;
		}
		
		if(this.mUsernameText.getText().toString().isEmpty()){

			DialogUtils.showErrorAlert(this, "Username!", "Please enter a username.");
			return;
		}

		if(this.mPwText.getText().toString().isEmpty()){

			DialogUtils.showErrorAlert(this, "Password!", "Please enter a password.");
			return;
		}
		
		if(this.mRepwText.getText().toString().isEmpty()){

			DialogUtils.showErrorAlert(this, "Confirm Password!", "Please confirm your password.");
			return;
		}

		if(!this.mPwText.getText().toString().equals(this.mRepwText.getText().toString())){

			DialogUtils.showErrorAlert(this, "Password Mismatch", "Please check your password and try again.");
			return;
		}
		
		if(mBitmap == null){
			DialogUtils.showErrorAlert(this, "Profile Picture!", "Please add a profile picture.");
			return;
		}
		
		final ParseUser user = new ParseUser();
		
		user.setUsername(this.mEmailText.getText().toString());
		user.setEmail(this.mEmailText.getText().toString());
		user.setPassword(this.mPwText.getText().toString());
		user.put("fullName", this.mFullnameText.getText().toString());
		user.put("userWaivelengthName", this.mUsernameText.getText().toString());
		
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		this.mBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
		byte[] imageByte = byteArrayOutputStream.toByteArray();
		ParseFile parseFile = new ParseFile("profileImage",imageByte);
		user.put("profileImage", parseFile);

		DialogUtils.displayProgress(SignupActivity.this);
		
		parseFile.saveInBackground(new SaveCallback() {
		    public void done(ParseException e) {
		       // If successful add file to user and signUpInBackground
		       if(null == e){
			   		if(NetworkUtils.isInternetAvailable(SignupActivity.this)){
						
						user.signUpInBackground(new SignUpCallback() {
							public void done(ParseException e) {
								
								DialogUtils.closeProgress();
								
								if (e == null) {
									ParseObject followersObject = new ParseObject("Followers");
									followersObject.add("user", user);
									followersObject.saveInBackground();
									
									Intent intent = new Intent(SignupActivity.this, TabBarActivity.class);
									startActivity(intent);
									finish();
								} else {
									DialogUtils.showErrorAlert(SignupActivity.this, "Error!", e.getMessage());
								}
							}
						});
					}else{
						DialogUtils.showErrorAlert(SignupActivity.this, "No Network!", "You are not connected to internet. Please connect and try again.");
					}
		       }
		    }
		});
	}
	
	private void onClickAddPhoto(){
		
		mChooserType = 0;
		final String items[] = { "Camera", "Photo Library" };
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("Add Profile Image");
        ab.setSingleChoiceItems(items, mChooserType,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	mChooserType = whichButton;
                    }
                }).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    	switch(mChooserType){
                    	case 0:
                    		takePhoto();
                    		break;
                    	case 1:
                    		chooseImage();
                    		break;
                    	}
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        ab.show();
	}
	
	private void chooseImage() {
		mChooserType = ChooserType.REQUEST_PICK_PICTURE;
		mImageChooserManager = new ImageChooserManager(this,
				ChooserType.REQUEST_PICK_PICTURE, "", true);
		mImageChooserManager.setImageChooserListener(this);
		try {
			mFilePath = mImageChooserManager.choose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void takePhoto() {
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    mPhotoFile = new File(Environment.getExternalStorageDirectory(),  "waivelength_profile.jpg");
	    mImageUri = Uri.fromFile(mPhotoFile);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
	    startActivityForResult(intent, REQUEST_TAKE_PICTURE);
	}

	public void StoreImage(Context mContext, Uri imgUri, File imageDir)
    {
		// ***********************
		String imagePath = imageDir.getAbsolutePath();
		int degree = 0;
		try {
            ExifInterface ei = new ExifInterface(imagePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		// ***********************
		
		//Bitmap bm = null;
        try
        {
        	//****************Add
        	Bitmap b = BitmapFactory.decodeFile(imagePath);

            Matrix matrix = new Matrix();
            if(b.getWidth() > b.getHeight()){
                matrix.setRotate(degree);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                        matrix, true);
            }
            //******************
            
            //BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 2;
            //bm = Media.getBitmap(mContext.getContentResolver(), imageLoc);
            //bm = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(imgUri), null, options);
            FileOutputStream out = new FileOutputStream(imageDir);
            b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            b.recycle();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	@Override
	public void onError(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onImageChosen(final ChosenImage image) {
		
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (image != null) {
					
					mImageUri = Uri.parse(new File(image.getFileThumbnail()).toString());
					mPhotoFile = new File(image.getFileThumbnail());
					
		        	StoreImage(SignupActivity.this, mImageUri, mPhotoFile);
		        	mBitmap = BitmapUtils.decodeFile(mPhotoFile);
		        	mAddphotoButton.setImageBitmap(mBitmap);
				}
			}
		});
	}
	
	private void reinitializeImageChooser() {
		mImageChooserManager = new ImageChooserManager(this, mChooserType,
				"myfolder", true);
		mImageChooserManager.setImageChooserListener(this);
		mImageChooserManager.reinitialize(mFilePath);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("chooser_type", mChooserType);
		outState.putString("media_path", mFilePath);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("chooser_type")) {
				mChooserType = savedInstanceState.getInt("chooser_type");
			}

			if (savedInstanceState.containsKey("media_path")) {
				mFilePath = savedInstanceState.getString("media_path");
			}
		}
		super.onRestoreInstanceState(savedInstanceState);
	}
}
