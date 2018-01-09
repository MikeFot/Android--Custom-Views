package com.michaelfotiadis.androidcustomviewsdemos.fragments;


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

import com.michaelfotiadis.androidcustomviews.image.TiledImageView;
import com.michaelfotiadis.androidcustomviewsdemos.R;

public class TiledImageFragment extends Fragment {

    private TiledImageView mTiledImageView;
    private Spinner mSpinnerChunks;

    public static Fragment newInstance() {
        return new TiledImageFragment();
    }

    public TiledImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tiled_image, container, false);
        mTiledImageView = (TiledImageView) view.findViewById(R.id.tiled_image);
        mSpinnerChunks = (Spinner) view.findViewById(R.id.spinner_chunks);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.chunk_numbers, R.layout.demo_spinner);
        // apply the adapter to the spinner
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
