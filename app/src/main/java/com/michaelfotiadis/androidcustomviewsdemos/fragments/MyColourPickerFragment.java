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
import android.widget.LinearLayout;

import com.michaelfotiadis.androidcustomviews.layouts.MyColourPickerView;
import com.michaelfotiadis.androidcustomviewsdemos.R;
import com.michaelfotiadis.androidcustomviewsdemos.activity.MainActivity;
import com.michaelfotiadis.androidcustomviewsdemos.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class MyColourPickerFragment extends Fragment implements View.OnClickListener{
    // fragment initialisation parameter
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int mPosition;

    private MyColourPickerView mPickerView;
    private LinearLayout mColourLayout;
    private Button mButtonAddColour;
    private Button mButtonRemoveColour;

    /**
     * Static factory method for the Colour Picker Fragment
     * @param sectionNumber Position in the activity
     * @return Instance of the Fragment
     */
    public static MyColourPickerFragment newInstance(final int sectionNumber) {
        MyColourPickerFragment fragment = new MyColourPickerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MyColourPickerFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_colour_picker, container, false);

        mPickerView = (MyColourPickerView) view.findViewById(R.id.picker_layout);
        mColourLayout = (LinearLayout) view.findViewById(R.id.colour_layout);

        mButtonAddColour = (Button) view.findViewById(R.id.button_add_colour);
        mButtonAddColour.setOnClickListener(this);
        mButtonRemoveColour = (Button) view.findViewById(R.id.button_remove_colour);
        mButtonRemoveColour.setOnClickListener(this);

        // initialise the Picker ImageViews
        generateColourImageViews(getColourList().subList(0, 4), this);

        return view;
    }

    /**
     * Passes the colour list and the OnClickListener to the Picker view so that it may generate the children ImageViews
     * @param colourList List of Integer colours
     * @param listener View.OnClickListener
     */
    private void generateColourImageViews(final List<Integer> colourList, final View.OnClickListener listener) {
        // pass the colour list to the view
        mPickerView.setColourList(colourList);
        // set the OnClickListener
        mPickerView.setChildrenOnClickListener(listener);
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
        colourList.add(getResources().getColor(R.color.holo_blue_dark));
        colourList.add(getResources().getColor(R.color.holo_green_dark));
        return colourList;
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        // pass the arguments back to the activity
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onClick(final View view) {
        if (view.hashCode() == mButtonAddColour.hashCode()) {
            // handle the button that adds colour widgets to the picker view
            final int currentSize = mPickerView.getContentView().getChildCount();
            if (currentSize < getColourList().size())
                generateColourImageViews(getColourList().subList(0, currentSize + 1), this);
        } else if (view.hashCode() == mButtonRemoveColour.hashCode()){
            // handle the button that removes colour widgets from the picker view
                final int currentSize = mPickerView.getContentView().getChildCount();
                if (currentSize > 1)
                    generateColourImageViews(getColourList().subList(0, currentSize - 1), this);
        } else if (view.getParent() == mPickerView.getContentView()) {
            // handle the picker view children
            final int widgetColour = getColourFromView(view);
            final int layoutColour = getColourFromView(mColourLayout);

            // revert to white if it's the same
            if (widgetColour == layoutColour) {
                mColourLayout.setBackgroundColor(getResources().getColor(R.color.background_holo_light));
            } else {
                mColourLayout.setBackgroundColor(getColourFromView(view));
            }
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
