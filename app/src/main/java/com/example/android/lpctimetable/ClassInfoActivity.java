package com.example.android.lpctimetable;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ClassInfoActivity extends AppCompatActivity {
    private TextView mRoom;
    private TextView mTeacher;
    private ImageView mCover;
    private EditText mRoomEdit;
    private EditText mTeacherEdit;
    private AppBarLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);
        mRoom = (TextView)findViewById(R.id.tv_room);
        mRoomEdit = (EditText) findViewById(R.id.et_room);
        mTeacher = (TextView)findViewById(R.id.tv_teacher);
        mTeacherEdit = (EditText) findViewById(R.id.et_teacher);
        mCover = (ImageView) findViewById(R.id.iv_cover);
        mLayout = (AppBarLayout) findViewById(R.id.ab_info);

        setSupportActionBar((Toolbar)findViewById(R.id.tb_info));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Subject mClassCurrent = (Subject) getIntent().getSerializableExtra(ClassesAdapter.CLASS_CURRENT);

        mRoom.setText(mClassCurrent.mRoom);
        mTeacher.setText(mClassCurrent.mTeacher);
        getSupportActionBar().setTitle(mClassCurrent.mName);
        mCover.setImageResource(mClassCurrent.mCoverId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.class_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit:
                mRoom.setVisibility(View.INVISIBLE);
                mTeacher.setVisibility(View.INVISIBLE);
                mRoomEdit.setVisibility(View.VISIBLE);
                mTeacherEdit.setVisibility(View.VISIBLE);
                mLayout.setExpanded(false,true);
                this.startActionMode(actionModeCallback);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.class_info_edit, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit_done:
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };
}
