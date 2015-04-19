package com.michaelfotiadis.androidcustomviews.layouts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.michaelfotiadis.androidcustomviews.R;
import com.michaelfotiadis.androidcustomviews.utils.Logger;

import java.util.List;

public class MyColourPickerView extends LinearLayout{

    private Context mContext;

    private LinearLayout mContentView;

    public MyColourPickerView(Context context) {
        this(context, null);
    }

    public MyColourPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyColourPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        //Inflate and attach your child XML
        LayoutInflater.from(context).inflate(R.layout.colour_picker_layout, this);
        //Get a reference to the layout where you want children to be placed
        mContentView = (LinearLayout) findViewById(R.id.content);

        mContentView.requestDisallowInterceptTouchEvent(true);
    }

    public LinearLayout getContentView() {
        return mContentView;
    }

    public void setColourList(final List<Integer> colourList) {
        mContentView.removeAllViews();

        if (colourList == null || colourList.isEmpty())
            return;

        int i = 0;
        for (int colour : colourList) {
            constructImageView(i, colour);
            i++;
        }
    }

    private void constructImageView(final int position, final int colour) {
        ImageView image = new ImageView(mContext);
        image.setBackgroundColor(colour);

        final int width = (int) mContext.getResources().getDimension(R.dimen.box_layout_height);
        final int height = (int) mContext.getResources().getDimension(R.dimen.box_layout_height);

        final int marginLeft = (int) mContext.getResources().getDimension(R.dimen.box_layout_margin_left);
        final int marginBottom = (int) mContext.getResources().getDimension(R.dimen.box_layout_margin_bottom);

        LayoutParams params = new LayoutParams(width, height);
        params.weight = 1;
        params.setMargins(marginLeft, marginBottom, marginLeft, marginBottom);

        // set the tag
        image.setTag(colour, this);

        addView(image, position, params);
    }

    /**
     * Given an OnClickListener, assign it to each child
     * @param listener View.OnClickListener
     */
    public void setChildrenOnClickListener(final OnClickListener listener) {
        // add the OnClickListener to each child of the ContentView
        final int childCount = getContentView().getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = getContentView().getChildAt(i);
            childView.setOnClickListener(listener);
        }

    }

    @Override
    public void addView(final View child,final int index,final ViewGroup.LayoutParams params) {
        if (mContentView == null) {
            super.addView(child, index, params);
        } else {
            mContentView.addView(child, index, params);
        }
    }
}
