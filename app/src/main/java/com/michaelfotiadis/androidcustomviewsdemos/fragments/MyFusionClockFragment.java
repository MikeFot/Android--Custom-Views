package com.michaelfotiadis.androidcustomviewsdemos.fragments;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.michaelfotiadis.androidcustomviews.clock.MyFusionClock;
import com.michaelfotiadis.androidcustomviews.layouts.MyColourPickerView;
import com.michaelfotiadis.androidcustomviewsdemos.R;
import com.michaelfotiadis.androidcustomviewsdemos.activity.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyFusionClockFragment extends Fragment implements View.OnClickListener {
    // fragment initialisation parameter
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int mPosition;

    private Button mButtonStart;
    private Button mButtonPause;
    private Button mButtonReset;

    private MyColourPickerView mPickerView;

    // Fields for storing the Clock Interface and its implementation
    private MyFusionClock mClock;

    public static MyFusionClockFragment newInstance(final int sectionNumber) {
        MyFusionClockFragment fragment = new MyFusionClockFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MyFusionClockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fusion_clock, container, false);

        // instantiate the clock view
        mClock = (MyFusionClock) view.findViewById(R.id.fusion_clock);

        mButtonStart = (Button) view.findViewById(R.id.button_clock_start);
        mButtonStart.setOnClickListener(this);
        mButtonPause = (Button) view.findViewById(R.id.button_clock_pause);
        mButtonPause.setOnClickListener(this);
        mButtonReset = (Button) view.findViewById(R.id.button_clock_reset);
        mButtonReset.setOnClickListener(this);

        mPickerView = (MyColourPickerView) view.findViewById(R.id.picker_layout);
        // pass the colour list to the view
        mPickerView.setColourList(getColourList());
        // set the OnClickListener
        mPickerView.setChildrenOnClickListener(this);

        return view;
    }

    /**
     * Generate a List of Integers for the demo
     * @return List of Integers representing colours
     */
    private List<Integer> getColourList() {
        final List<Integer> colourList = new ArrayList<Integer>();
        colourList.add(getResources().getColor(R.color.holo_blue_light));
        colourList.add(getResources().getColor(R.color.holo_green_light));
        colourList.add(getResources().getColor(R.color.holo_orange_light));
        colourList.add(getResources().getColor(R.color.holo_red_light));
        colourList.add(getResources().getColor(R.color.holo_purple));
        return colourList;
    }

    /**
     * @return Long value of service time started from Shared Preferences
     */
    public long getCurrentTimeInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public void onResume() {
        super.onResume();

        startTheClock(getCurrentTimeInMillis(), 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTheClock();
    }

    /**
     * Send the command to pause the clock to the interface
     */
    private void pauseTheClock() {
        mClock.pauseClock();
    }

    /**
     * Send the command to start the clock to the interface
     * @param time
     */
    private void startTheClock(long time, int interval) {
        mClock.startClock(time, interval);
    }

    /**
     * Send the command to stop the clock to the interface
     */
    private void stopTheClock() {
        mClock.stopClock();
    }

    @Override
    public void onClick(final View view) {
        if (view.hashCode() == mButtonStart.hashCode()) {
            if (mClock.getTimeRunning() == 0) {
                startTheClock(getCurrentTimeInMillis(), 0);
            } else{
                startTheClock(0, 0);
            }
        } else if (view.hashCode() == mButtonPause.hashCode()) {
            pauseTheClock();
        } else if (view.hashCode() == mButtonReset.hashCode()) {
            stopTheClock();
        } else if (view.getParent() == mPickerView.getContentView()) {
            final int colour = getColourFromView(view);
            String hexColor = String.format("#%06X", (0xFFFFFF & colour));
            mClock.setOverlayColor(hexColor);
        }
    }

    /**
     * Retrieve the background colour of a view
     * @param view View
     * @return The int colour or TRANSPARENT if it cannot be found
     */
    private int getColourFromView(final View view) {
        int colour = Color.TRANSPARENT;
        final Drawable background = view.getBackground();
        if (background instanceof ColorDrawable)
            colour = ((ColorDrawable) background).getColor();
        return colour;
    }
}
