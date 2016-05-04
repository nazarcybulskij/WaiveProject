package com.example.waive.ui.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.example.waive.ui.activity.SignupActivity;
import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.ui.view.CircularImageView;
import com.example.waive.utils.BitmapUtils;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.example.waive.R;


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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


public class UserinfoFragment extends Fragment implements ImageChooserListener {
	
	private static final int 		REQUEST_TAKE_PICTURE = 1;
	private TabBarActivity 			mTab = null;
	private ImageButton				mCloseButton = null;
	private ImageButton				mSaveButton = null;
	private CircularImageView		mAddphotoButton = null;
	private EditText				mEmailText = null;
	private EditText				mNameText = null;
	private EditText				mUsernameText = null;
	private EditText				mCurpwText = null;
	private EditText				mNewpwText = null;
	private EditText				mConfirmpwText = null;
	private boolean					mIsFbUser = false;
	private boolean 				mUserEnteredPassword = false;
	private int						mChooserType = 0;
	private Uri 					mImageUri = null;
	private File 					mPhotoFile = null;
	private Bitmap					mBitmap = null;
	private ImageChooserManager 	mImageChooserManager = null;
	private String 					mFilePath = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTab = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_userinfo, container, false);	
		
		mEmailText = (EditText)v.findViewById(R.id.emailText);
		mNameText = (EditText)v.findViewById(R.id.fullnameText);
		mUsernameText = (EditText)v.findViewById(R.id.usernameText);
		mCurpwText = (EditText)v.findViewById(R.id.pwText);
		mNewpwText = (EditText)v.findViewById(R.id.repwText);
		mConfirmpwText = (EditText)v.findViewById(R.id.confirmpwText);

		mAddphotoButton = (CircularImageView)v.findViewById(R.id.addPhotoButton);
		mAddphotoButton.setBorderWidth(0);
		mAddphotoButton.setBorderColor(Color.WHITE);
		mAddphotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickAddPhoto();
			}
		});

		mCloseButton = (ImageButton)v.findViewById(R.id.closeButton);
		mCloseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTab.pop();
				mTab.fragmentReplace(mTab.cur());
			}
		});
		
		mSaveButton = (ImageButton)v.findViewById(R.id.saveButton);
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickSave();
			}
		});
		
		getUserDetails();
		
        return v;
	}
	
	private void getUserDetails(){
		ParseUser user = ParseUser.getCurrentUser();
		
		this.mEmailText.setText(user.getEmail());
		this.mNameText.setText(user.getString("fullName"));
		this.mUsernameText.setText(user.getString("userWaivelengthName"));
		
		if(user.getString("facebookId") != null){
			
			this.mIsFbUser = true;
			
			this.mNewpwText.setEnabled(false);
			this.mNewpwText.setText(R.string.newpw_hint);
			
			this.mConfirmpwText.setEnabled(false);
			this.mConfirmpwText.setText(R.string.confirmpw_hint);
			
		}else{
			this.mIsFbUser = false;
		}
		
		ParseFile imageDataFile = user.getParseFile("profileImage");
		imageDataFile.getDataInBackground(new GetDataCallback() {

            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                mAddphotoButton.setImageBitmap(bitpic);
            }
        });
	}
	
	private void onClickSave(){
		
		if(this.mEmailText.getText().toString().isEmpty()){
			DialogUtils.showErrorAlert(mTab, "Email!", "Please enter your email.");
			return;
		}
		
		if(this.mNameText.getText().toString().isEmpty()){
			DialogUtils.showErrorAlert(mTab, "Name!", "Please enter your full name.");
			return;
		}

		if(this.mUsernameText.getText().toString().isEmpty()){
			
			if(ParseUser.getCurrentUser().getString("facebookId").isEmpty()){
				DialogUtils.showErrorAlert(mTab, "Username!", "Please enter a username.");
				return;
			}
		}

		if(!this.mIsFbUser){
			if(!this.mCurpwText.getText().toString().isEmpty()
			&& !this.mNewpwText.getText().toString().isEmpty()
			&& !this.mConfirmpwText.getText().toString().isEmpty()){
				
				this.mUserEnteredPassword = true;
				
				if(!this.mNewpwText.getText().toString().equals(this.mConfirmpwText.getText().toString())){
					DialogUtils.showErrorAlert(mTab, "Password Mismatch!", "Please check your password and try again.");
					return;
				}
			}else if(this.mConfirmpwText.getText().toString().isEmpty()){
				DialogUtils.showErrorAlert(mTab, "Confirm Password!", "Please confirm your password.");
				return;
			}else if(this.mNewpwText.getText().toString().isEmpty()){
				DialogUtils.showErrorAlert(mTab, "Password!", "Please enter your new password.");
				return;
			}else if(this.mCurpwText.getText().toString().isEmpty()){
				DialogUtils.showErrorAlert(mTab, "Password!", "Please enter your current password.");
				return;
			}
		}
		
		DialogUtils.displayProgress(mTab);
		
		ParseUser user = ParseUser.getCurrentUser();
		
		user.setUsername(this.mEmailText.getText().toString());
		user.setEmail(this.mEmailText.getText().toString());
		
		if(!this.mIsFbUser && this.mUserEnteredPassword){
			user.setPassword(this.mNewpwText.getText().toString());
		}
		
		user.add("fullName", this.mNameText.getText().toString());
		user.add("userWaivelengthName", this.mUsernameText.getText().toString());
		
		if(NetworkUtils.isInternetAvailable(this.mTab)){
			
			if(this.mUserEnteredPassword){
				ParseUser.logInInBackground(user.getEmail(), this.mCurpwText.getText().toString(), new LogInCallback() {
					
					public void done(ParseUser user, ParseException e) {
				    
						DialogUtils.closeProgress();
						
						if (e == null && user != null) {
				    	 
							user.saveInBackground(new SaveCallback(){

								@Override
								public void done(ParseException e) {
									
									if(e != null){
										DialogUtils.showErrorAlert(mTab, "Error!", e.getMessage());
									}else{
										mTab.pop();
										mTab.fragmentReplace(mTab.cur());
									}
								}
							});
						} else {
							DialogUtils.showErrorAlert(mTab, "Password!", "The current password you've entered does not match the one on record.");
						}
					}
				});
			}else{
				user.saveInBackground(new SaveCallback(){

					@Override
					public void done(ParseException e) {
						
						if(e != null){
							DialogUtils.showErrorAlert(mTab, "Error!", e.getMessage());
						}else{
							mTab.pop();
							mTab.fragmentReplace(mTab.cur());
						}
					}
				});
			}
		}else{
			
			DialogUtils.showErrorAlert(mTab, "No Network!", "You are not connected to internet. Please connect and try again.");
		}
	}
	
	private void onClickAddPhoto(){
		
		mChooserType = 0;
		final String items[] = { "Camera", "Photo Library" };
        AlertDialog.Builder ab = new AlertDialog.Builder(mTab);
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
				ChooserType.REQUEST_PICK_PICTURE, "myfolder", true);
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
            
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            //bm = Media.getBitmap(mContext.getContentResolver(), imageLoc);
            //bm = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(imgUri), null, options);
            FileOutputStream out = new FileOutputStream(imageDir);
            b.compress(Bitmap.CompressFormat.JPEG, 50, out);
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
		
		mTab.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (image != null) {
					
					mImageUri = Uri.parse(new File(image.getFileThumbnail()).toString());
					mPhotoFile = new File(image.getFileThumbnail());
					
		        	StoreImage(mTab, mImageUri, mPhotoFile);
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
}
