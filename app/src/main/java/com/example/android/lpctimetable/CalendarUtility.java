package com.example.android.lpctimetable;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;



/**
 * Created by charl on 1/27/2019.
 */

public class CalendarUtility{

    private Calendar mCalendar;
    private ContentResolver mContentResolver;
    private long mStartDate, mEndDate;

    private static final String[] EVENT_PROJECTION = new String[] { CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART };
    private static final String TAG = "CalendarUtility";

    public class Response {
        @Nullable public Integer mDay;
        @Nullable public Integer mPm;
        @Nullable public String mOther;
        Response(@Nullable Integer day, @Nullable Integer pm, @Nullable String other) {
            mDay = day;
            mPm = pm;
            mOther = other;
        }
        Response(){
            mDay = null;
            mPm = null;
            mOther = null;
        }
    }

    CalendarUtility(Context context){
        mCalendar = Calendar.getInstance();
        mContentResolver = context.getContentResolver();
    }

    public Response listAllEvents(int dayOffset) {
        //Setup query time
        mCalendar.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        mCalendar.add(Calendar.DATE,dayOffset);
        mStartDate = mCalendar.getTimeInMillis();
        mCalendar.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        mCalendar.add(Calendar.DATE,dayOffset);
        mEndDate = mCalendar.getTimeInMillis();

        //Setup query time interval
        String selection = "" + CalendarContract.Events.DTSTART + " > ? AND ("
                + CalendarContract.Events.DTSTART + " < ?) AND ("
                + CalendarContract.Events.TITLE + " LIKE ?) AND (" + CalendarContract.Events.ALL_DAY +"= 1)";

        //Query any calendar event containing "day"
        String selectionArgs[] = { Long.toString(mStartDate), Long.toString(mEndDate), "%day _%"};
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION, selection, selectionArgs, null);
        } catch(SecurityException e){
            e.printStackTrace();
        }

        String event = null;
        String dayLetter = null;

        Integer day = null;
        Integer pm = null;
        String other = null;

        //Extract the string from the query cursor
        if (cursor != null && cursor.moveToFirst()) {
            //Get the first string from the cursor
            event = cursor.getString(0);
            //Cut the string before "day", extract the first letter and convert it to an index
            dayLetter = event.toLowerCase().split("(?<=day) ")[1];
            day = (int)dayLetter.charAt(0) - 97;
            Log.i(TAG, "listAllEvents: Day Letter " + dayLetter);
        }else {
            Log.e(TAG, "listAllEvents: failed to grab event");
        }

        if (event != null){
            //Treat event that does not meet the condition as "other"
            if ((dayLetter.length() != 1 && dayLetter.length() != 8) && (
                    dayLetter.length() > 8 || day < 0 || day > 7)){
                day = null;
                pm = null;
                other = event;
            }else {
                //Store pm block value if available
                if (dayLetter.length() == 8) {
                    Log.i(TAG, "listAllEvents: PM Block " + dayLetter.charAt(7));
                    pm = (int) dayLetter.charAt(7) - 97;
                }
            }
        }
        Log.d(TAG, "listAllEvents: ("+day+", "+pm+", "+other+")");

        return new Response(day, pm, other);
    }

    public String getDateString(int dayOffset){
        mCalendar.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        mCalendar.add(Calendar.DATE, dayOffset);
        return new SimpleDateFormat("MMM d").format(mCalendar.getTime());
    }
}
