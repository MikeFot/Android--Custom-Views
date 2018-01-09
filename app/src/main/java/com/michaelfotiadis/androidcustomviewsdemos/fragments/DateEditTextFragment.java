package com.michaelfotiadis.androidcustomviewsdemos.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.androidcustomviewsdemos.R;

public class DateEditTextFragment extends Fragment {


    public static Fragment newInstance() {
        return new DateEditTextFragment();
    }

    public DateEditTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_datepicker, container, false);
    }

}
