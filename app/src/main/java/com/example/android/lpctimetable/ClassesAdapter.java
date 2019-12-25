package com.example.android.lpctimetable;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by charl on 1/19/2019.
 */

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ClassViewHolder>{
    private MainActivity.Subject[] mClasses;
    private static final String TIME_SLOTS[] = {
            "8:30 - 9:30",
            "10:00 - 11:10",
            "11:15 - 12:25",
            "12:35 - 13:45",
            "14:30 - 15:40"
    };

    private RecyclerView mParent;
    public static final String CLASS_ROOM = "com.example.android.lpctimetable.ClassRoom";
    public static final String CLASS_TEACHER = "com.example.android.lpctimetable.ClassTeacher";
    public static final String CLASS_NAME = "com.example.android.lpctimetable.Class";

    class myOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mParent.getContext(), ClassInfoActivity.class);
            int pos = mParent.getChildLayoutPosition(view);
            intent.putExtra(CLASS_ROOM, mClasses[pos].mRoom);
            intent.putExtra(CLASS_TEACHER, mClasses[pos].mTeacher);
            intent.putExtra(CLASS_NAME, mClasses[pos].mName);
            mParent.getContext().startActivity(intent);
        }
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        private CardView mContainer;
        private TextView mTitle;
        private ColorStateList mTitleColor;
        private TextView mTime;
        private ColorStateList mTimeColor;
        private ImageView mBackground;

        public void highLight() {
            String subjectName = mTitle.getText().toString();
            if (subjectName.contains("Eng"))
            {
                mBackground.setImageResource(R.drawable.english);
                mTitle.setTextColor(Color.WHITE);
                mTime.setTextColor(Color.WHITE);
            }
            if (subjectName.contains("Chin"))
            {
                mBackground.setImageResource(R.drawable.chinese);
            }
            if (subjectName.contains("Math"))
            {
                mBackground.setImageResource(R.drawable.maths);
                mTitle.setTextColor(Color.WHITE);
                mTime.setTextColor(Color.WHITE);
            }
            if (subjectName.contains("Env Sys Soc"))
            {
                mBackground.setImageResource(R.drawable.ess);
                mTitle.setTextColor(Color.WHITE);
                mTime.setTextColor(Color.WHITE);
            }
            if (subjectName.contains("Physics"))
            {
                mBackground.setImageResource(R.drawable.physics);
                mTitle.setTextColor(Color.WHITE);
                mTime.setTextColor(Color.WHITE);
            }
            if (subjectName.contains("Economics"))
            {
                mBackground.setImageResource(R.drawable.economics);
                mTitle.setTextColor(Color.WHITE);
                mTime.setTextColor(Color.WHITE);
            }
            if (subjectName.contains("TOK"))
            {
                mBackground.setImageResource(R.drawable.tok);
            }
            mContainer.setCardElevation(8);
            mBackground.setVisibility(View.VISIBLE);
        }

        public void unHighLight() {
            mContainer.setCardElevation(2);
            mBackground.setVisibility(View.INVISIBLE);
            mTitle.setTextColor(mTitleColor);
            mTime.setTextColor(mTimeColor);
        }

        public ClassViewHolder(CardView v) {
            super(v);
            mContainer = v;
            mTitle = (TextView) v.findViewById(R.id.tv_class_title);
            mTime = (TextView) v.findViewById(R.id.tv_class_details);
            mBackground = (ImageView) v.findViewById(R.id.iv_bg);

            mTimeColor = mTime.getTextColors();
            mTitleColor = mTitle.getTextColors();
        }
    }

    public ClassesAdapter(MainActivity.Subject[] classes) {
        mClasses = classes;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ClassesAdapter.ClassViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_elements, parent, false);
        v.setOnClickListener(new myOnClickListener());
        mParent = (RecyclerView) parent;

        return new ClassViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        holder.mTitle.setText(mClasses[position].mName);
        holder.mTime.setText(TIME_SLOTS[position % 5]);

        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

        int classOrder = -1;

        if ((currentHour == 8 && currentMinute >= 30) || (currentHour == 9 && currentMinute <= 30)) {
            classOrder = 0;
        }
        if ((currentHour == 10 && currentMinute >= 00) || (currentHour == 11 && currentMinute <= 10)) {
            classOrder = 1;
        }
        if ((currentHour == 11 && currentMinute >= 15) || (currentHour == 12 && currentMinute <= 25)) {
            classOrder = 2;
        }
        if ((currentHour == 12 && currentMinute >= 35) || (currentHour == 913&& currentMinute <= 45)) {
            classOrder = 3;
        }

        if (position == classOrder)
            holder.highLight();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mClasses.length;
    }
}