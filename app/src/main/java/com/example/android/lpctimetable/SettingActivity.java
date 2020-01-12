package com.example.android.lpctimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity implements ResetTimetableDialogFragment.NoticeDialogListener {
    Button mReset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setSupportActionBar((Toolbar)findViewById(R.id.tb_setting));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Setting");

        mReset = (Button) findViewById(R.id.bt_reset);
        mReset.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ) {
                    Log.i("hi", "resetTimetable: button clicked");
                    DialogFragment newFragment = new ResetTimetableDialogFragment();
                    newFragment.show(getFragmentManager(),"reset");
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        resetTimetable();
    }

    public void resetTimetable() {
        Log.i("hi", "resetTimetable: removed");
        SharedPreferences sharedPref= getSharedPreferences("com.example.android.lpctimetable.classes", Context.MODE_PRIVATE);
        sharedPref.edit().clear().apply();
    }
}
