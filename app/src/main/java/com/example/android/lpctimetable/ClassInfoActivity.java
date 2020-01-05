package com.example.android.lpctimetable;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
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
    private int currentMenu = R.menu.class_info;

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
        getMenuInflater().inflate(currentMenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit:
                openEditingInterface();
                return true;
            case R.id.action_edit_done:
                closeEditingInterface();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (currentMenu == R.menu.class_info_edit)
        {
            closeEditingInterface();
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void closeEditingInterface()
    {
        hideKeyboard(this);

        mRoom.setText(mRoomEdit.getText());
        mTeacher.setText(mTeacherEdit.getText());

        mRoom.setVisibility(View.VISIBLE);
        mTeacher.setVisibility(View.VISIBLE);
        mRoomEdit.setVisibility(View.INVISIBLE);
        mTeacherEdit.setVisibility(View.INVISIBLE);

        mLayout.setExpanded(true,true);
        currentMenu = R.menu.class_info;
        invalidateOptionsMenu();
    }

    private void openEditingInterface()
    {
        mRoomEdit.setText(mRoom.getText());
        mTeacherEdit.setText(mTeacher.getText());

        mRoom.setVisibility(View.INVISIBLE);
        mTeacher.setVisibility(View.INVISIBLE);
        mRoomEdit.setVisibility(View.VISIBLE);
        mTeacherEdit.setVisibility(View.VISIBLE);

        mLayout.setExpanded(false,true);
        currentMenu = R.menu.class_info_edit;
        invalidateOptionsMenu();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
