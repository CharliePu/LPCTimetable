package com.example.android.lpctimetable;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ClassInfoActivity extends AppCompatActivity {
    private ImageView mCover;
    private TextView mTeacher;
    private TextView mRoom;
    private TextView mSubjectLabel;
    private TextView mEmailLabel;
    private EditText mTeacherEdit;
    private EditText mRoomEdit;
    private EditText mSubjectEdit;
    private EditText mEmailEdit;
    private AppBarLayout mAppBarLayout;
    private ConstraintLayout mConstraintLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Subject mClassCurrent;
    private FloatingActionButton mEmailFAB;

    private int currentMenu = R.menu.class_info;
    private String emailAddress;
    private char classCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);
        mRoom = (TextView)findViewById(R.id.tv_room);
        mRoomEdit = (EditText) findViewById(R.id.et_room);
        mTeacher = (TextView)findViewById(R.id.tv_teacher);
        mTeacherEdit = (EditText) findViewById(R.id.et_teacher);
        mSubjectLabel = (TextView) findViewById(R.id.tv_subject_label);
        mSubjectEdit = (EditText) findViewById(R.id.et_subject);
        mEmailLabel = (TextView) findViewById(R.id.tv_email_label);
        mEmailEdit = (EditText) findViewById(R.id.et_email);
        mEmailFAB = (FloatingActionButton) findViewById(R.id.fab_email);
        mCover = (ImageView) findViewById(R.id.iv_cover);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.ab_class_info);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.cl_class_info);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_class_info);

        setSupportActionBar((Toolbar)findViewById(R.id.tb_class_info));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mClassCurrent = (Subject) getIntent().getSerializableExtra(ClassesAdapter.CLASS_CURRENT);
        classCode = mClassCurrent.mClassCode;
        emailAddress = mClassCurrent.mEmail;

        mEmailFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
                startActivity(Intent.createChooser(intent, "Send to"));
            }
        });

        getSupportActionBar().setTitle(mClassCurrent.mName);
        mRoom.setText(mClassCurrent.mRoom);
        mTeacher.setText(mClassCurrent.mTeacher);

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
                closeEditingInterface(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (currentMenu == R.menu.class_info_edit)
        {
            closeEditingInterface(false);
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void closeEditingInterface(Boolean willSave)
    {
        hideKeyboard(this);

        if (willSave)
        {
            mCollapsingToolbarLayout.setTitle(mSubjectEdit.getText().toString());
            mRoom.setText(mRoomEdit.getText().toString());
            mTeacher.setText(mTeacherEdit.getText().toString());
            emailAddress = mEmailEdit.getText().toString();

            mClassCurrent.mRoom = mRoomEdit.getText().toString();
            mClassCurrent.mTeacher = mTeacherEdit.getText().toString();
            mClassCurrent.mName = mSubjectEdit.getText().toString();
            mClassCurrent.mEmail = mEmailEdit.getText().toString();
            mClassCurrent.save(this);
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mConstraintLayout);
        constraintSet.connect(R.id.tv_teacher_label, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.applyTo(mConstraintLayout);
        mSubjectLabel.setVisibility(View.INVISIBLE);
        mSubjectEdit.setVisibility(View.INVISIBLE);
        mRoom.setVisibility(View.VISIBLE);
        mEmailLabel.setVisibility(View.INVISIBLE);
        mEmailEdit.setVisibility(View.INVISIBLE);
        mTeacher.setVisibility(View.VISIBLE);
        mRoomEdit.setVisibility(View.INVISIBLE);
        mTeacherEdit.setVisibility(View.INVISIBLE);

        mAppBarLayout.setExpanded(true,true);
        currentMenu = R.menu.class_info;
        invalidateOptionsMenu();
    }

    private void openEditingInterface()
    {
        mRoomEdit.setText(mRoom.getText());
        mTeacherEdit.setText(mTeacher.getText());

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mConstraintLayout);
        constraintSet.connect(R.id.tv_teacher_label, ConstraintSet.TOP, R.id.et_subject, ConstraintSet.BOTTOM);
        constraintSet.applyTo(mConstraintLayout);

        mSubjectEdit.setText(mCollapsingToolbarLayout.getTitle());
        mTeacherEdit.setText(mTeacher.getText());
        mRoomEdit.setText(mRoom.getText());
        mEmailEdit.setText(emailAddress);
        mCollapsingToolbarLayout.setTitle("Class "+classCode);

        mSubjectEdit.setVisibility(View.VISIBLE);
        mSubjectLabel.setVisibility(View.VISIBLE);
        mRoom.setVisibility(View.INVISIBLE);
        mEmailLabel.setVisibility(View.VISIBLE);
        mEmailEdit.setVisibility(View.VISIBLE);
        mTeacher.setVisibility(View.INVISIBLE);
        mRoomEdit.setVisibility(View.VISIBLE);
        mTeacherEdit.setVisibility(View.VISIBLE);

        mAppBarLayout.setExpanded(false,true);
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
