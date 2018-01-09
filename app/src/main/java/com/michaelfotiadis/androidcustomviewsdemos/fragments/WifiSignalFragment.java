package com.michaelfotiadis.androidcustomviewsdemos.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.pwittchen.reactivewifi.WifiSignalLevel;
import com.michaelfotiadis.androidcustomviews.image.WifiImageView;
import com.michaelfotiadis.androidcustomviewsdemos.R;

public class WifiSignalFragment extends Fragment {

    private WifiImageView imageView;
    private TextView textView;

    public static Fragment newInstance() {
        return new WifiSignalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.image);
        textView = view.findViewById(R.id.content);
    }

    @Override
    public void onResume() {
        super.onResume();
        imageView.setCallbacks(this::setText);
    }

    @Override
    public void onPause() {
        super.onPause();
        imageView.removeCallbacks();
    }

    private void setText(final WifiSignalLevel level) {
        final String description = String.format("<b>%s</b>", level.description.toUpperCase());
        final Spanned formattedDescription = Html.fromHtml(description);
        textView.setText(formattedDescription);
    }

}
