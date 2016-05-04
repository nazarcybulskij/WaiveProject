package com.example.waive.utils;

import com.example.waive.ui.dialog.ProgressIndicator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Maulik.Joshi on 29-06-2015.
 */
public class DialogUtils {
    public static ProgressIndicator progressIndicator;

    public static void displayProgress(Context context){
        if(progressIndicator == null){
            progressIndicator = new ProgressIndicator(context);
        }

        if(!((Activity)context).isFinishing() && !progressIndicator.isShowing())
            progressIndicator.show();
    }

    public static void closeProgress(){
        if (progressIndicator != null && progressIndicator.isShowing()) {
            progressIndicator.dismiss();
            progressIndicator = null;
        }
    }
    
	public static void showErrorAlert(Context context, String title, String message){
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(title);
        ab.setMessage(message);
        ab.setNegativeButton("OK", null);
        ab.show();
	}
}
