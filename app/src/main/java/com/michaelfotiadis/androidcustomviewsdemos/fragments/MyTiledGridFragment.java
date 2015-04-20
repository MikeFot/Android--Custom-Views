package com.michaelfotiadis.androidcustomviewsdemos.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.michaelfotiadis.androidcustomviews.gridview.MyTiledGridView;
import com.michaelfotiadis.androidcustomviewsdemos.R;
import com.michaelfotiadis.androidcustomviewsdemos.activity.MainActivity;

public class MyTiledGridFragment extends Fragment implements AdapterView.OnItemClickListener {
    // fragment initialisation parameter
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int mPosition;

    public static MyTiledGridFragment newInstance(final int sectionNumber) {
        MyTiledGridFragment fragment = new MyTiledGridFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MyTiledGridFragment() {
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tiled_grid, container, false);

        final Drawable drawable = getResources().getDrawable(R.drawable.androidrobot);

        MyTiledGridView grid = (MyTiledGridView) view.findViewById(R.id.grid);
        grid.init(drawable, 4);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
