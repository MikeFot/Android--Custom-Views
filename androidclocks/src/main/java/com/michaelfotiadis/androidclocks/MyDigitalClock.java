package com.michaelfotiadis.androidclocks;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.michaelfotiadis.androidclocks.containers.ErgoClockInstance;
import com.michaelfotiadis.androidclocks.utils.PrimitiveConversions;

import java.util.Calendar;


public class MyDigitalClock extends TextView implements MyClockInterface {

	private final String TAG = "My Digital Clock";

	private final String TIME_ZERO_VALUE = "00:00:00";

	private long mStartTime = 0;

	private int prefFontSize = 12;

	private ErgoClockInstance mClockInstance;

	private final Handler mHandler = new Handler();

	private final long _updateInterval = 1000;

    private int mFontColour = Integer.MIN_VALUE;
    private String mFontName;

	private final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mHandler.postDelayed(this, _updateInterval);
			updateTime();
		}
	};

	public MyDigitalClock(Context context) {
		super(context);
		mClockInstance = new ErgoClockInstance();
	}

	public MyDigitalClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
		mClockInstance = new ErgoClockInstance();

	}

	public MyDigitalClock(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
		mClockInstance = new ErgoClockInstance();
	}

    private void setFontName(final String fontName) {
        this.mFontName = fontName;
    }

	private void init(AttributeSet attrs) {
		prefFontSize = (int) getResources().getDimension(R.dimen.digital_clock_font_size);

        if (mFontName == null)
            mFontName = "digital_7_mono.ttf";

		final Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/" + mFontName);
		try {
			setTypeface(typeface);
		} catch (Exception e1) {
            Log.e(TAG, e1.getLocalizedMessage());
        }
		
		setTextSize(prefFontSize);

		try {
            if (mFontColour == Integer.MIN_VALUE)
                setFontColour("#ff0099cc");

			this.setTextColor(mFontColour);
		} catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
		}
		
		// set the time initially to 00:00:00
		this.setText(TIME_ZERO_VALUE);
	}

    public void setFontColour(final String colour) {
        try {
            this.mFontColour = Color.parseColor(colour);
        } catch (Exception e) {
            this.mFontColour = Color.parseColor("#ff0099cc");
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

	@Override
	public boolean isVisible() {
		if (this.getVisibility() == View.VISIBLE) {
			return true;
		}
		return false;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	public void pauseClock() {
		mHandler.removeCallbacks(mRunnable);
	}

	@Override
	public void setTime(final int hours, final int minutes, final int seconds) {
		mClockInstance.setSeconds(6.0f*seconds);
		mClockInstance.setMinutes(minutes + seconds / 60.0f);
		mClockInstance.setHour(hours + minutes / 60.0f);
		updateTime();
	}

	@Override
	public void startClock(final long startTime, final int minutesToAlarm) {
		mStartTime = startTime;
		mHandler.post(mRunnable);
		invalidate();
	}

	@Override
	public void stopClock() {
		mClockInstance.reset();
		this.setText(TIME_ZERO_VALUE);
		mHandler.removeCallbacks(mRunnable);
	}

	private long mTimeRunning;

	@Override
	public void updateTime() {
		setTimeRunning((Calendar.getInstance().getTimeInMillis() - 
				mStartTime) / 1000);

		if (mStartTime == 0) {
			mClockInstance.reset();
			this.setText(TIME_ZERO_VALUE);
			return;
		}

		int[] timeIntArray = PrimitiveConversions.getIntTimeArrayFromSeconds(getTimeRunning());
		mClockInstance.setTime(timeIntArray[0], timeIntArray[1], timeIntArray[2]);

		this.setText(mClockInstance.getString());
		invalidate();
	}

	@Override
	public long getTimeRunning() {
		return mTimeRunning;
	}

	private void setTimeRunning(long timeRunning) {
		this.mTimeRunning = timeRunning;
	}

	@Override
	public void setMinutesToAlarm(int minutesToAlarm) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setSystemTime() {
		// TODO Auto-generated method stub
	}
}
