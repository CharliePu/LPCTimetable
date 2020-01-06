package com.example.android.lpctimetable;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private Subject[] mSubjectList = {
            new Subject("Eng A L&L", "316", "ME",R.drawable.english, 'A'),
            new Subject("Math HL", "215", "MZ", R.drawable.maths, 'B'),
            new Subject("Physics HL", "205", "MS", R.drawable.physics, 'C'),
            new Subject("Economics HL", "211", "AO", R.drawable.economics, 'G'),
            new Subject("Env Sys Soc SL", "207", "JAC", R.drawable.ess, 'E'),
            new Subject("Chin Li A SL", "212", "CC", R.drawable.chinese, 'F'),
            new Subject("TOK", "212", "KB", R.drawable.tok, 'D'),
    };

    private ArrayList<Subject> mSubjects;

    static private int[][] TIME_TABLE = {
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
        mDayOffset = 0;
        getClassesFromCalendar();
        displayClasses();

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
        mSubjects = new ArrayList<>(Arrays.asList(mSubjectList));

        mIndicator.setVisibility(View.VISIBLE);
        mIndicator.setText(R.string.loading);

        CalendarUtility.Response response = new CalendarUtility(this).listAllEvents(mDayOffset);
        mDayOther = response.mOther;
        mClassArrayList.clear();

        //////////DEBUG
        //response.mDay=1;
        /////////
        if (response.mDay != null) {
            for (int i : TIME_TABLE[response.mDay]) {
                mClassArrayList.add(mSubjects.get(i));
            }
            if (response.mPm != null) {
                mClassArrayList.add(mSubjects.get(response.mPm));
            }
        }
    }

    void displayClasses(){
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
            Log.i("Out", i.mName);
        }

        mAdapter = new ClassesAdapter(subjectArray);
        mRecyclerView.setAdapter(mAdapter);
    }
}
