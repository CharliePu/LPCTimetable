package com.example.android.lpctimetable;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

public class Subject implements Serializable {
    public String mName;
    public String mRoom;
    public String mTeacher;
    public int mCoverId;
    public char mClassCode;

    Subject(String name, String room, String teacher, int coverId, char classCode) {
        mName = name;
        mRoom = room;
        mTeacher = teacher;
        mCoverId = coverId;
        mClassCode = classCode;
    }

    Subject(Context context,char classCode){
        SharedPreferences sharedPref= context.getSharedPreferences("com.example.android.lpctimetable.classes",Context.MODE_PRIVATE);
        mName = sharedPref.getString("com.example.android.lpctimetable.class_"+classCode+"_name", "Class "+classCode);
        mRoom = sharedPref.getString("com.example.android.lpctimetable.class_"+classCode+"_room", "");
        mTeacher = sharedPref.getString("com.example.android.lpctimetable.class_"+classCode+"_teacher", "");
        mCoverId = sharedPref.getInt("com.example.android.lpctimetable.class_"+classCode+"_cover", R.drawable.ess);
        mClassCode = classCode;
    }

    public void save(Context context){
        SharedPreferences sharedPref= context.getSharedPreferences("com.example.android.lpctimetable.classes",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("com.example.android.lpctimetable.class_"+mClassCode+"_name",mName);
        editor.putString("com.example.android.lpctimetable.class_"+mClassCode+"_room",mRoom);
        editor.putString("com.example.android.lpctimetable.class_"+mClassCode+"_teacher",mTeacher);
        editor.putInt("com.example.android.lpctimetable.class_"+mClassCode+"_cover",mCoverId);
        editor.apply();
    }
}