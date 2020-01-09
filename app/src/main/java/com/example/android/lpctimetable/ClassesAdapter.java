package com.example.android.lpctimetable;

import android.content.Context;
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
    private Subject[] mClasses;
    private static final String TIME_SLOTS[] = {
            "8:30 - 9:30",
            "10:00 - 11:10",
            "11:15 - 12:25",
            "12:35 - 13:45",
            "14:30 - 15:40"
    };

    private RecyclerView mParent;
    private boolean mIsClassesToday;
    public static final String CLASS_CURRENT = "com.example.android.lpctimetable.ClassCurrent";

    class myOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mParent.getContext(), ClassInfoActivity.class);
            int pos = mParent.getChildLayoutPosition(view);
            intent.putExtra(CLASS_CURRENT, mClasses[pos]);
            mParent.getContext().startActivity(intent);
        }
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        private CardView mContainer;
        private TextView mTitle;
        private TextView mTime;
        private ImageView mBackground;
        private Subject mSubject;


        public void highLight(Context context) {
            String subjectName = mTitle.getText().toString();
            mTitle.setTextColor(Color.WHITE);
            mTime.setTextColor(Color.WHITE);
            mContainer.setCardElevation(8);
            mBackground.setVisibility(View.VISIBLE);

            if (mSubject.getmCover(context) != null) {
                mBackground.setColorFilter(R.color.colorPrimaryDark);
                mBackground.setImageBitmap(mSubject.getmCover(context));
            } else {
                mBackground.clearColorFilter();
                mBackground.setImageBitmap(null);
                mBackground.setBackgroundResource(R.color.colorPrimary);
            }
        }

        public void unHighLight() {
            mContainer.setCardElevation(2);
            mBackground.clearColorFilter();
            mBackground.setVisibility(View.INVISIBLE);
            mTitle.setTextColor(Color.BLACK);
            mTime.setTextColor(Color.BLACK);
        }

        public ClassViewHolder(CardView v) {
            super(v);
            mContainer = v;
            mTitle = (TextView) v.findViewById(R.id.tv_class_title);
            mTime = (TextView) v.findViewById(R.id.tv_class_details);
            mBackground = (ImageView) v.findViewById(R.id.iv_bg);

            unHighLight();
        }
    }

    public ClassesAdapter(Subject[] classes, boolean isClassesToday) {
        mClasses = classes;
        mIsClassesToday = isClassesToday;
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
        holder.mTitle.setText(mClasses[position].getmName());
        holder.mTime.setText(TIME_SLOTS[position % 5]);
        holder.mSubject = mClasses[position];

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
        if ((currentHour == 12 && currentMinute >= 35) || (currentHour == 13&& currentMinute <= 45)) {
            classOrder = 3;
        }
        if ((currentHour == 14 && currentMinute >= 30) || (currentHour == 15&& currentMinute <= 40)) {
            classOrder = 4;
        }

        if (position == classOrder && mIsClassesToday)
            holder.highLight(mParent.getContext());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mClasses.length;
    }
}