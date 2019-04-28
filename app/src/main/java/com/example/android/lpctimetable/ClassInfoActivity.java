package com.example.android.lpctimetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ClassInfoActivity extends AppCompatActivity {
    private TextView mRoom;
    private TextView mTeacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);

        mRoom = (TextView)findViewById(R.id.room);
        mRoom.setText(getIntent().getStringExtra(MyAdapter.CLASS_ROOM));
        mTeacher = (TextView)findViewById(R.id.teacher);
        mTeacher.setText(getIntent().getStringExtra(MyAdapter.CLASS_TEACHER));

    }
}
