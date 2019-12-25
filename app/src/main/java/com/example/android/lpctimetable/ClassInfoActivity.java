package com.example.android.lpctimetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class ClassInfoActivity extends AppCompatActivity {
    private TextView mRoom;
    private TextView mTeacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);
        setSupportActionBar((Toolbar)findViewById(R.id.tb_info));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRoom = (TextView)findViewById(R.id.room);
        mRoom.setText(getIntent().getStringExtra(ClassesAdapter.CLASS_ROOM));
        mTeacher = (TextView)findViewById(R.id.teacher);
        mTeacher.setText(getIntent().getStringExtra(ClassesAdapter.CLASS_TEACHER));

        getSupportActionBar().setTitle(getIntent().getStringExtra(ClassesAdapter.CLASS_NAME));
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
}
