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

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by charl on 1/27/2019.
 */

public class CalendarUtility{

    private Calendar mCalendar;
    private ContentResolver mContentResolver;
    private long mStartDate, mEndDate;

    private static final String[] EVENT_PROJECTION = new String[] { CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART };

    public class Response {
        @Nullable public Integer mDay;
        @Nullable public Integer mPm;
        Response(@Nullable Integer day, @Nullable Integer pm) {
            mDay = day;
            mPm = pm;
        }
    }

    CalendarUtility(Context context){
        mCalendar = Calendar.getInstance();
        mContentResolver = context.getContentResolver();

        mCalendar.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        mStartDate = mCalendar.getTimeInMillis();
        mCalendar.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        mEndDate = mCalendar.getTimeInMillis();
    }

    private ArrayList<String> queryEvents() throws SecurityException {
        String selection = "" + CalendarContract.Events.DTSTART + " > ? AND ("
                + CalendarContract.Events.DTSTART + " < ?) AND ("
                + CalendarContract.Events.TITLE + " LIKE ?)";
        String selectionArgs[] = { Long.toString(mStartDate), Long.toString(mEndDate), "Day%"};

        Cursor cursor = mContentResolver.query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION, selection, selectionArgs, null);

        Log.i(TAG, "queryEvents: counts: "+cursor.getCount());
        ArrayList<String> events = new ArrayList<>();
        while (cursor.moveToNext()) {
            events.add(cursor.getString(0));
            Log.i(TAG, "queryEvents: "+cursor.getString(0)+ " "+cursor.getString(1));
        }
        Log.i(TAG, "queryEvents: date: "+Long.toString(mStartDate));
        return events;
    }

    public Response listAllEvents() {
        Integer day = null;
        Integer pm = null;
        try {
            String event = queryEvents().get(0);
            day = (int)event.charAt(4) - 65;
            if (day < 0 || day > 6) {
                day = null;
            }
            if (event.length() > 5){
                pm = (int)event.charAt(11) - 65;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new Response(day, pm);
    }
}
