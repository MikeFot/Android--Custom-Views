package com.michaelfotiadis.androidcustomviews.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.github.pwittchen.reactivewifi.ReactiveWifi;
import com.github.pwittchen.reactivewifi.WifiSignalLevel;
import com.michaelfotiadis.androidcustomviews.R;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WifiImageView extends AppCompatImageView {

    private int tintColor;
    private Disposable signalLevelSubscription;
    @Nullable
    private WeakReference<WifiLevelChangedCallbacks> callbacks;

    public WifiImageView(final Context context) {
        super(context);
    }

    public WifiImageView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WifiImageView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WifiImageView, defStyleAttr, 0);
        tintColor = typedArray.getColor(R.styleable.WifiImageView_tint, 0);
        typedArray.recycle();
    }

    /**
     * Callbacks for being notified whenever signal changes
     */
    public interface WifiLevelChangedCallbacks {

        void onLevelChanged(WifiSignalLevel level);

    }

    public void setCallbacks(@NonNull final WifiLevelChangedCallbacks callbacks) {
        this.callbacks = new WeakReference<>(callbacks);
    }

    public void removeCallbacks() {
        if (callbacks != null) {
            callbacks.clear();
        }
    }

    public void setLevel(final WifiSignalLevel level) {

        if (callbacks != null && callbacks.get() != null) {
            callbacks.get().onLevelChanged(level);
        }

        @DrawableRes final int drawableRes;
        switch (level) {
            case NO_SIGNAL:
                drawableRes = R.drawable.ic_signal_wifi_0_bar_white_24dp;
                break;
            case POOR:
                drawableRes = R.drawable.ic_signal_wifi_1_bar_white_24dp;
                break;
            case FAIR:
                drawableRes = R.drawable.ic_signal_wifi_2_bar_white_24dp;
                break;
            case GOOD:
                drawableRes = R.drawable.ic_signal_wifi_3_bar_white_24dp;
                break;
            case EXCELLENT:
                drawableRes = R.drawable.ic_signal_wifi_4_bar_white_24dp;
                break;
            default:
                drawableRes = R.drawable.ic_signal_wifi_0_bar_white_24dp;
        }

        final Drawable drawable = ContextCompat.getDrawable(getContext(), drawableRes);
        if (drawable != null) {
            final Drawable modifiedDrawable;
            if (tintColor > 0) {
                modifiedDrawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(modifiedDrawable.mutate(), ContextCompat.getColor(getContext(), tintColor));
            } else {
                modifiedDrawable = drawable;
            }
            setImageDrawable(modifiedDrawable);
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startWifiSignalLevelSubscription();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        callbacks = null;
        if (signalLevelSubscription != null && !signalLevelSubscription.isDisposed()) {
            signalLevelSubscription.dispose();
        }
    }

    private void startWifiSignalLevelSubscription() {
        signalLevelSubscription = ReactiveWifi.observeWifiSignalLevel(getContext().getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setLevel);
    }

}
