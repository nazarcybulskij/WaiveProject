package com.example.waive.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Maulik.Joshi on 13-07-2015.
 */
public class DateUtils {

    /**
     * To change format of date in yyyy-MM-dd format
     * @param date date to convert
     * @param dateFormat current date format
     * @return
     */
    public static String convertDateToServerFormat(String date, SimpleDateFormat dateFormat){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = new Date();
        try {
            currentDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(currentDate);
    }

    /**
     * To change format of date in yyyy-MM-dd HH:mm:ss format
     * @param date date to convert
     * @param dateFormat current date format
     * @return
     */
    public static String convertDateTimeToServerFormat(String date, SimpleDateFormat dateFormat){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        try {
            currentDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(currentDate);
    }

//    /**
//     * To change format of the date
//     * @param date
//     * @param currentFormat format of current date
//     * @param outputFormat format of output date
//     * @return
//     */
//    public static String convertDateFormat(String date, SimpleDateFormat currentFormat, SimpleDateFormat outputFormat){
//        Date currentDate = new Date();
//        try {
//            if(Validate.hasString(date)) {
//                currentDate = currentFormat.parse(date);
//                return outputFormat.format(currentDate);
//            }else{
//                return "";
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }

    /**
     * To get difference in date
     * @param fromDate
     * @param toDate
     * @param df
     * @param isHour
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static int DateDifference(String fromDate, String toDate,
                                     SimpleDateFormat df, boolean isHour) {
        long diff = 0;
        try {
            // Convert to Date
            Date startDate = df.parse(fromDate);
            Calendar c1 = Calendar.getInstance();
            // Change to Calendar Date
            c1.setTime(startDate);

            // Convert to Date
            Date endDate = df.parse(toDate);
            Calendar c2 = Calendar.getInstance();
            // Change to Calendar Date
            c2.setTime(endDate);
            // get Time in milli seconds
            long ms1 = c1.getTimeInMillis();
            long ms2 = c2.getTimeInMillis();
            // get difference in milli seconds
            if (isHour) {
                diff = ms2 - ms1;
            } else {
                diff = ms1 - ms2;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (isHour) {
            // Find number of days by dividing the mili seconds
            int diffInHours = (int) (diff / (1000 * 60 * 60));
            
            return diffInHours;
        } else {
            // Find number of days by dividing the mili seconds
            int diffInDays = (int) (diff / (24 * 60 * 60 * 1000));
            return diffInDays;
        }
        // To get number of seconds diff/1000
        // To get number of minutes diff/(1000 * 60)
        // To get number of hours diff/(1000 * 60 * 60)
    }

    /**
     * To get difference in minute from date
     * @param fromDate
     * @param toDate
     * @param df
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static int MinuteDifference(String fromDate, String toDate,
                                       SimpleDateFormat df) {
        long diff = 0;
        try {
            // Convert to Date
            Date startDate = df.parse(fromDate);
            Calendar c1 = Calendar.getInstance();
            // Change to Calendar Date
            c1.setTime(startDate);

            // Convert to Date
            Date endDate = df.parse(toDate);
            Calendar c2 = Calendar.getInstance();
            // Change to Calendar Date
            c2.setTime(endDate);
            // get Time in milli seconds
            long ms1 = c1.getTimeInMillis();
            long ms2 = c2.getTimeInMillis();
            // get difference in milli seconds
            diff = ms2 - ms1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Find number of days by dividing the mili seconds
        int diffInHours = (int) (diff / (1000 * 60));
        return diffInHours;

        // To get number of seconds diff/1000
        // To get number of minutes diff/(1000 * 60)
        // To get number of hours diff/(1000 * 60 * 60)
    }
    
    public static int calculate(Date fromDate, Date toDate) {
		long diff = 0;
		// Convert to Date
		Calendar c1 = Calendar.getInstance();
		// Change to Calendar Date
		c1.setTime(fromDate);
		
		Calendar c2 = Calendar.getInstance();
		// Change to Calendar Date
		c2.setTime(toDate);
		
		// get Time in milli seconds
		long ms1 = c1.getTimeInMillis();
		long ms2 = c2.getTimeInMillis();
		// get difference in milli seconds
		diff = ms2 - ms1;
		
		// Find number of days by dividing the mili seconds
		int diffInHours = (int) (diff / (1000 * 60));
		return diffInHours;
		
		// To get number of seconds diff/1000
		// To get number of minutes diff/(1000 * 60)
		// To get number of hours diff/(1000 * 60 * 60)
		// To get number of days diff/(1000 * 60 * 60)

    }
    
}