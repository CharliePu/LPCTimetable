package com.example.android.lpctimetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ClassInfoActivity extends AppCompatActivity {
    private TextView mRoom;
    private TextView mTeacher;
    private ImageView mCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);
        mRoom = (TextView)findViewById(R.id.tv_room);
        mTeacher = (TextView)findViewById(R.id.tv_teacher);
        mCover = (ImageView) findViewById(R.id.iv_cover);
        setSupportActionBar((Toolbar)findViewById(R.id.tb_info));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Subject mClassCurrent = (Subject) getIntent().getSerializableExtra(ClassesAdapter.CLASS_CURRENT);

        mRoom.setText(mClassCurrent.mRoom);
        mTeacher.setText(mClassCurrent.mTeacher);
        getSupportActionBar().setTitle(mClassCurrent.mName);
        mCover.setImageResource(mClassCurrent.mCoverId);

//        if (ClassesAdapter.CLASS_NAME.contains("Eng"))
//        {
//            mBackground.setImageResource(R.drawable.english);
//        }
//        if (ClassesAdapter.CLASS_NAME.contains("Chin"))
//        {
//            mBackground.setImageResource(R.drawable.chinese);
//        }
//        if (ClassesAdapter.CLASS_NAME.contains("Math"))
//        {
//            mBackground.setImageResource(R.drawable.maths);
//        }
//        if (ClassesAdapter.CLASS_NAME.contains("Env Sys Soc"))
//        {
//            mBackground.setImageResource(R.drawable.ess);
//        }
//        if (ClassesAdapter.CLASS_NAME.contains("Physics"))
//        {
//            mBackground.setImageResource(R.drawable.physics);
//        }
//        if (ClassesAdapter.CLASS_NAME.contains("Economics"))
//        {
//            mBackground.setImageResource(R.drawable.economics);
//        }
//        if (ClassesAdapter.CLASS_NAME.contains("TOK"))
//        {
//            mBackground.setImageResource(R.drawable.tok);
//        }
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
