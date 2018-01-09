package com.michaelfotiadis.androidcustomviewsdemos.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.michaelfotiadis.androidcustomviews.grid.TiledGridView;
import com.michaelfotiadis.androidcustomviewsdemos.R;

public class TiledGridFragment extends Fragment implements AdapterView.OnItemClickListener {

    TiledGridView mGridView;
    Spinner mSpinnerChunks;
    Switch mSwitchBorder;

    public TiledGridFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new TiledGridFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tiled_grid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialise the custom GridView
        mGridView = (TiledGridView) view.findViewById(R.id.tiled_gridview);
        mSpinnerChunks = (Spinner) view.findViewById(R.id.spinner_chunks);
        mSwitchBorder = (Switch) view.findViewById(R.id.switch_border);

        // setup the GridView
        mGridView.setDrawable(getDrawable(), 4, mSwitchBorder.isChecked());
        mGridView.setOnItemClickListener(this);

        // setup the Spinner
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.chunk_numbers, R.layout.demo_spinner);
        // apply the adapter to the spinner
        mSpinnerChunks.setAdapter(adapter);
        // add the listener
        mSpinnerChunks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int numberOfChunks = 4;
                switch (position) {
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
                mGridView.setDrawable(getDrawable(), numberOfChunks, mSwitchBorder.isChecked());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        // setup the Switch
        mSwitchBorder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // instruct the custom GridView to adjust the spacing
                mGridView.toggleSpacing(isChecked);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setVisibility(View.INVISIBLE);
    }

    private Drawable getDrawable() {
        return getResources().getDrawable(R.drawable.androidrobot);
    }
}
