package com.example.android.lpctimetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.io.Serializable;

public class Subject implements Serializable {
    private String mName;
    private String mRoom;
    private String mTeacher;
    private String mEmail;
    private char mClassCode;

    Subject(String name, String room, String teacher, String email, char classCode) {
        mName = name;
        mRoom = room;
        mTeacher = teacher;
        mEmail = email;
        mClassCode = classCode;
    }

    Subject(Context context,char classCode){
        SharedPreferences sharedPref= context.getSharedPreferences("com.example.android.lpctimetable.classes",Context.MODE_PRIVATE);
        mName = sharedPref.getString("com.example.android.lpctimetable.class_"+classCode+"_name", "Class "+classCode);
        mRoom = sharedPref.getString("com.example.android.lpctimetable.class_"+classCode+"_room", "");
        mTeacher = sharedPref.getString("com.example.android.lpctimetable.class_"+classCode+"_teacher", "");
        mEmail = sharedPref.getString("com.example.android.lpctimetable.class_" + classCode + "_email", "");
        mClassCode = classCode;
    }

    public void save(Context context){
        SharedPreferences sharedPref= context.getSharedPreferences("com.example.android.lpctimetable.classes",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("com.example.android.lpctimetable.class_"+mClassCode+"_name",mName);
        editor.putString("com.example.android.lpctimetable.class_"+mClassCode+"_room",mRoom);
        editor.putString("com.example.android.lpctimetable.class_"+mClassCode+"_teacher",mTeacher);
        editor.putString("com.example.android.lpctimetable.class_"+mClassCode+"_email",mEmail);
        editor.apply();
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmRoom() {
        return mRoom;
    }

    public void setmRoom(String mRoom) {
        this.mRoom = mRoom;
    }

    public String getmTeacher() {
        return mTeacher;
    }

    public void setmTeacher(String mTeacher) {
        this.mTeacher = mTeacher;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public char getmClassCode() {
        return mClassCode;
    }

    public void setmClassCode(char mClassCode) {
        this.mClassCode = mClassCode;
    }

    public Bitmap getmCover(Context context){
        return new CoverUtility().loadCoverFromStorage(mClassCode,context);
    }

    public void setmCover(Bitmap cover, Context context){
        new CoverUtility().saveCover(cover,mClassCode,context);
    }
}