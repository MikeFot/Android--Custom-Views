package com.michaelfotiadis.androidcustomviewsdemos.fragments;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.michaelfotiadis.androidclocks.FusionClock;
import com.michaelfotiadis.androidcustomviewsdemos.R;
import com.michaelfotiadis.colourpicker.ColourPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FusionClockFragment extends Fragment implements View.OnClickListener {

    private Button mButtonStart;
    private Button mButtonPause;
    private Button mButtonReset;

    private ColourPickerView mPickerView;

    // Fields for storing the Clock Interface and its implementation
    private FusionClock mClock;

    public static Fragment newInstance() {
        return new FusionClockFragment();
    }

    public FusionClockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fusion_clock, container, false);

        // instantiate the clock view
        mClock = (FusionClock) view.findViewById(R.id.fusion_clock);

        mButtonStart = (Button) view.findViewById(R.id.button_clock_start);
        mButtonStart.setOnClickListener(this);
        mButtonPause = (Button) view.findViewById(R.id.button_clock_pause);
        mButtonPause.setOnClickListener(this);
        mButtonReset = (Button) view.findViewById(R.id.button_clock_reset);
        mButtonReset.setOnClickListener(this);

        mPickerView = (ColourPickerView) view.findViewById(R.id.picker_layout);
        // pass the colour list to the view
        mPickerView.setColourList(getColourList());
        // set the OnClickListener
        mPickerView.setChildrenOnClickListener(this);

        return view;
    }

    /**
     * Generate a List of Integers for the demo
     *
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
     *
     * @param time
     */
    private void startTheClock(final long time, final int interval) {
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
            } else {
                startTheClock(0, 0);
            }
        } else if (view.hashCode() == mButtonPause.hashCode()) {
            pauseTheClock();
        } else if (view.hashCode() == mButtonReset.hashCode()) {
            stopTheClock();
        } else if (view.getParent() == mPickerView.getContentView()) {
            final int colour = getColourFromView(view);
            final String hexColor = String.format("#%06X", (0xFFFFFF & colour));
            mClock.setOverlayColor(hexColor);
        }
    }

    /**
     * Retrieve the background colour of a view
     *
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
