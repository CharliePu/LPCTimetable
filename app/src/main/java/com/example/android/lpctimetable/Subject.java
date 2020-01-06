package com.example.android.lpctimetable;

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
}