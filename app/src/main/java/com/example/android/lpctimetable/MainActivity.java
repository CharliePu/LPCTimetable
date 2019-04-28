package com.example.android.lpctimetable;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Map;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    public class Subject {
        public String mName;
        public String mRoom;
        public String mTeacher;

        Subject(String name, String room, String teacher) {
            mName = name;
            mRoom = room;
            mTeacher = teacher;
        }
    }

    private Subject mSubjects[] = {
            new Subject("Eng A L&L", "104", "ME"),
            new Subject("Math HL", "214", "MZ"),
            new Subject("Physics HL", "205", "MS"),
            new Subject("TOK", "212", "KB"),
            new Subject("Env Sys Soc SL", "207", "JAC"),
            new Subject("Chin Li A SL", "212", "CC"),
            new Subject("Economics HL", "210", "AO")};

    static private int TIME_TABLE[][] = {
            {0, 1, 2, 3},//A
            {1, 2, 3, 4},//B
            {2, 3, 4, 5},//C
            {3, 4, 5, 6},//D
            {4, 5, 6, 0},//E
            {5, 6, 0, 1},//F
            {6, 0, 1, 2}//G
    };

    static int REQUEST_CALENDAR_READ = 233;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private TextView mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);

        mIndicator = (TextView) findViewById(R.id.tv_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_classes);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkPermissions();

        mIndicator.setVisibility(View.VISIBLE);
        mIndicator.setText("Loading");

        if (loadClasses()) {
            mIndicator.setVisibility(View.INVISIBLE);
        } else {
            mIndicator.setText("Class info not available\n Please check your calendar");
        }
    }

    @Override
    protected void onRestart() {
        mIndicator.setVisibility(View.VISIBLE);
        mIndicator.setText("Loading");

        if (loadClasses()) {
            mIndicator.setVisibility(View.INVISIBLE);
        } else {
            mIndicator.setText("Classes not available\n Please check your calendar");
        }

        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    void checkPermissions(){
        if (checkSelfPermission(android.Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_DENIED) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CALENDAR)) {
                Toast.makeText(this, "Calendar Permission is Required", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{android.Manifest.permission.READ_CALENDAR},
                    REQUEST_CALENDAR_READ);
        }
    }

    boolean loadClasses(){
        ArrayList<Subject> subjectArrayList = new ArrayList<>();
        CalendarUtility.Response response = new CalendarUtility(this).listAllEvents();
        Integer day = response.mDay;
        Integer pm = response.mPm;

        day = 0;
        if (day != null) {
            for (int i : TIME_TABLE[day]) {
                subjectArrayList.add(mSubjects[i]);
            }
        }
        if (pm != null) {
            subjectArrayList.add(mSubjects[pm]);
        }

        Subject subjectArray[] = subjectArrayList.toArray(new Subject[subjectArrayList.size()]);

        for (Subject i : subjectArray){
            Log.i("Out", i.mName);
        }

        mAdapter = new MyAdapter(subjectArray);
        mRecyclerView.setAdapter(mAdapter);

        return day != null;
    }
}
