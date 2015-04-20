package com.michaelfotiadis.androidcustomviewsdemos.fragments;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.michaelfotiadis.androidcustomviews.imageview.MyTiledImageView;
import com.michaelfotiadis.androidcustomviewsdemos.R;
import com.michaelfotiadis.androidcustomviewsdemos.activity.MainActivity;

public class MyTiledImageFragment extends Fragment {
    // fragment initialisation parameter
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int mPosition;

    private MyTiledImageView mTiledImageView;
    private Spinner mSpinnerChunks;

    public static MyTiledImageFragment newInstance(final int sectionNumber) {
        MyTiledImageFragment fragment = new MyTiledImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MyTiledImageFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_tiled_image, container, false);
        mTiledImageView = (MyTiledImageView) view.findViewById(R.id.tiled_image);
        mSpinnerChunks = (Spinner) view.findViewById(R.id.spinner_chunks);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.chunk_numbers, R.layout.demo_spinner);
        // Specify the layout to use when the list of choices appears
        // Apply the adapter to the spinner
        mSpinnerChunks.setAdapter(adapter);
        mSpinnerChunks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int numberOfChunks = 4;
                switch(position) {
                    case 0:
                        numberOfChunks = 4;
                        break;
                    case 1:
                        numberOfChunks = 9;
                        break;
                    case 2:
                        numberOfChunks = 16;
                        break;
                    case 3:
                        numberOfChunks = 64;
                        break;
                    default:
                        numberOfChunks = 4;
                        break;
                }
                mTiledImageView.createImageArrays(getDrawable(), numberOfChunks, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTiledImageView.createImageArrays(getDrawable(), 9, true);
    }

    private Drawable getDrawable() {
        return getResources().getDrawable(R.drawable.androidrobot);
    }
}
