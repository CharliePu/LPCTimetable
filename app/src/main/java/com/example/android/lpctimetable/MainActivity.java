package com.example.android.lpctimetable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

//    private Subject[] mSubjectList = {
//            new Subject("Eng A L&L", "316", "ME",R.drawable.english,"", 'A'),//0
//            new Subject("Math HL", "215", "MZ", R.drawable.maths,"", 'B'),//1
//            new Subject("Physics HL", "205", "MS", R.drawable.physics,"", 'C'),//2
//            new Subject("TOK", "212", "KB", R.drawable.tok,"", 'D'),//3
//            new Subject("Env Sys Soc SL", "207", "JAC", R.drawable.ess,"", 'E'),//4
//            new Subject("Chin Li A SL", "212", "CC", R.drawable.chinese,"", 'F'),//5
//            new Subject("Economics HL", "211", "AO", R.drawable.economics,"", 'G'),//6
//    };

    private ArrayList<Subject> mSubjects;

    static private char[][] TIME_TABLE = {
            {'A', 'B', 'C', 'D'},//A
            {'B', 'C', 'D', 'E'},//B
            {'C', 'D', 'E', 'F'},//C
            {'D', 'E', 'F', 'G'},//D
            {'E', 'F', 'G', 'A'},//E
            {'F', 'G', 'A', 'B'},//F
            {'G', 'A', 'B', 'C'}//G
    };

    static int REQUEST_CALENDAR_READ = 233;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private TextView mIndicator;
    private MenuItem mActionToday;

    private ArrayList<Subject> mClassArrayList;
    private int mDayOffset;
    private @Nullable String mDayOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.tb_main));
        getSupportActionBar().setTitle("Today");

        mDayOffset = 0;
        mIndicator = (TextView) findViewById(R.id.tv_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_day);
        mClassArrayList = new ArrayList<>();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkPermissions();

        getClassesFromCalendar();
        displayClasses();
    }

    @Override
    protected void onRestart() {
        getClassesFromCalendar();
        displayClasses();

        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        mActionToday = menu.findItem(R.id.action_today);

        if (mDayOffset == 0){
            mActionToday.setVisible(false);
        }else {
            mActionToday.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_previous_day:
                mDayOffset--;
                getClassesFromCalendar();
                displayClasses();
                return true;

            case R.id.action_next_day:
                mDayOffset++;
                getClassesFromCalendar();
                displayClasses();
                return true;

            case R.id.action_today:
                mDayOffset = 0;
                getClassesFromCalendar();
                displayClasses();
                mActionToday.setVisible(false);
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

    void getClassesFromCalendar(){
        mIndicator.setVisibility(View.VISIBLE);
        mIndicator.setText(R.string.loading);

        CalendarUtility.Response response = new CalendarUtility(this).listAllEvents(mDayOffset);
        mDayOther = response.mOther;
        mClassArrayList.clear();

        //////DEBUG
        //response.mDay=1;
        /////
        if (response.mDay != null) {
            for (char i : TIME_TABLE[response.mDay]) {
                mClassArrayList.add(new Subject(this,i));
            }
            if (response.mPm != null) {
                mClassArrayList.add(new Subject(this,response.mPm));
            }
        }
    }

    void displayClasses(){

        invalidateOptionsMenu();

        switch (mDayOffset){
            case 0:
                getSupportActionBar().setTitle("Today");
                break;
            case 1:
                getSupportActionBar().setTitle("Tomorrow");
                break;
            case -1:
                getSupportActionBar().setTitle("Yesterday");
                break;
            default:
                getSupportActionBar().setTitle(new CalendarUtility(this).getDateString(mDayOffset));
        }

        if (mClassArrayList.isEmpty()) {
            if (mDayOther != null)
            {
                mIndicator.setText("Classes not available\n" + mDayOther);
            } else {
                mIndicator.setText("Classes not available\n Go check your timetable");
            }
            mRecyclerView.setVisibility(View.INVISIBLE);
            return;
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mIndicator.setVisibility(View.INVISIBLE);

        Subject[] subjectArray = mClassArrayList.toArray(new Subject[0]);

        for (Subject i : subjectArray){
            Log.i("Out", "test:"+i.getmName());
        }

        mAdapter = new ClassesAdapter(subjectArray, mDayOffset==0);
        mRecyclerView.setAdapter(mAdapter);
    }
}
