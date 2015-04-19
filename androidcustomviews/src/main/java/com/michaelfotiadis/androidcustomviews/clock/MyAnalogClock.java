package com.michaelfotiadis.androidcustomviews.clock;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RemoteViews.RemoteView;

import com.michaelfotiadis.androidcustomviews.R;
import com.michaelfotiadis.androidcustomviews.containers.ErgoClockInstance;
import com.michaelfotiadis.androidcustomviews.utils.ColorUtils;
import com.michaelfotiadis.androidcustomviews.utils.Logger;
import com.michaelfotiadis.androidcustomviews.utils.PrimitiveConversions;

import java.util.Calendar;

/**
 * This widget display an analogue clock with two hands for hours and
 * minutes.
 */
@RemoteView
public class MyAnalogClock extends View implements MyClockInterface {

	private final long HANDLER_UPDATE_INTERVAL = 1000;

	// drawable fields
	private Drawable mHourHand;
	private Drawable mMinuteHand;
	private Drawable mSecondHand;
	private Drawable mDial;
	private Drawable mAlarmHandMinutes;
	private Drawable mAlarmHandHours;


	private int mDialWidth;
	private int mDialHeight;

	private boolean mChanged;

    // overlay color for color filter
    private int mOverlayColor = Integer.MIN_VALUE;
    private int mLighterOverlayColor = Integer.MIN_VALUE;
    private int mShiftedOverlayColor = Integer.MIN_VALUE;
	
	private final Handler mHandler = new Handler();

	private final String TAG = "My Analog Clock";

	private final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mHandler.postDelayed(this, HANDLER_UPDATE_INTERVAL);
			updateTime();
		}
	};

	private long mTimeRunning;

	private ErgoClockInstance mClockInstance = new ErgoClockInstance();

	private long mStartTime= 0;

	private int mInterval;

	public MyAnalogClock(Context context) {
		super(context);
	}

	public MyAnalogClock(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	private MyAnalogClock(Context context, AttributeSet attrs,
                          int defStyle) {
		super(context, attrs, defStyle);
		Resources resources = context.getResources();
		TypedArray typedArray =	context.obtainStyledAttributes(attrs, R.styleable.AnalogClock, defStyle, 0);
		mDial = resources.getDrawable(R.drawable.clock_dial);
		mHourHand = resources.getDrawable(R.drawable.clock_hand_hour);
		mMinuteHand = resources.getDrawable(R.drawable.clock_hand_minute);
		mSecondHand = resources.getDrawable(R.drawable.clock_hand_second);

		// set the 2 alarm hands
		mAlarmHandHours = resources.getDrawable(R.drawable.clock_hand_alarm_hours);
		mAlarmHandMinutes = resources.getDrawable(R.drawable.clock_hand_alarm_minutes);
		
		mDialWidth = mDial.getIntrinsicWidth();
		mDialHeight = mDial.getIntrinsicHeight();
		typedArray.recycle();

        setOverlayColor("#ff0099cc");
	}

    public void setOverlayColor(final String hexColour) {
        try {
            this.mOverlayColor = Color.parseColor(hexColour);
            mShiftedOverlayColor = new ColorUtils().getRightBitShiftedColor(mOverlayColor);
            mLighterOverlayColor = new ColorUtils().getLighterColor(mOverlayColor);
            this.invalidate();
        } catch (Exception e) {
            this.mOverlayColor = Color.parseColor("#ff0099cc");
            mShiftedOverlayColor = new ColorUtils().getRightBitShiftedColor(mOverlayColor);
            mLighterOverlayColor = new ColorUtils().getLighterColor(mOverlayColor);
            Logger.e(TAG, e.getLocalizedMessage());
        }
    }

	@Override
	public long getTimeRunning() {
		return mTimeRunning;
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
		Logger.d(TAG, "Analogue Clock Attached to Window");
		mChanged = true;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stopClock();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		boolean changed = mChanged;
		if (changed) {
			mChanged = false;
		}

		//Here you can set the size of your clock
		int availableWidth = getRight() - getLeft();
		int availableHeight = getBottom() - getTop();
		//Actual size
		int actualX = availableWidth / 2;
		int actualY = availableHeight / 2;

		final Drawable dial = mDial;
		int width = dial.getIntrinsicWidth();
		int height = dial.getIntrinsicHeight();
		boolean scaled = false;

		if (availableWidth < width || availableHeight < height) {
			scaled = true;
			float scale = Math.min((float) availableWidth / (float) width,
					(float) availableHeight / (float) height);
			canvas.save();
			canvas.scale(scale, scale, actualX, actualY);
		}
		if (changed) {
			dial.setBounds(actualX - (width / 2), actualY - (height / 2), actualX + (width / 2), actualY + (height / 2));
		}
		// dial.setColorFilter(mOverlayColor, android.graphics.PorterDuff.Mode.MULTIPLY);
		dial.draw(canvas);
		canvas.save();
		
		// draw the minutes hand
		canvas.save();
		canvas.rotate(mClockInstance.getMinutes() / 60.0f * 360.0f, actualX, actualY);
		width = mMinuteHand.getIntrinsicWidth();
		height = mMinuteHand.getIntrinsicHeight();
		mMinuteHand.setBounds(actualX - (width / 2), actualY - (height / 2), actualX + (width / 2), actualY + (height / 2));
		mMinuteHand.draw(canvas);
		canvas.restore();
		
		// draw the hours hand
		canvas.save();
		canvas.rotate(mClockInstance.getHour() / 12.0f * 360.0f, actualX, actualY);
		width = mHourHand.getIntrinsicWidth();
		height = mHourHand.getIntrinsicHeight();
		mHourHand.setBounds(actualX - (width / 2), actualY - (height / 2), actualX + (width / 2), actualY + (height / 2));
		mHourHand.draw(canvas);
		canvas.restore();

		// draw the hours alarm indication
		canvas.save();
		canvas.rotate((mInterval / 60) / 12.0f * 360.0f, actualX, actualY);
		width = mAlarmHandHours.getIntrinsicWidth();
		height = mAlarmHandHours.getIntrinsicHeight();
		mAlarmHandHours.setColorFilter(mLighterOverlayColor, android.graphics.PorterDuff.Mode.MULTIPLY);
		mAlarmHandHours.setAlpha(200);
		mAlarmHandHours.setBounds(actualX - (width / 2), actualY - (height / 2), actualX + (width / 2), actualY + (height / 2));
		mAlarmHandHours.draw(canvas);
		canvas.restore();
		
		// draw the minutes alarm indication
		canvas.save();
		canvas.rotate(mInterval / 60.0f * 360.0f, actualX, actualY);
		width = mAlarmHandMinutes.getIntrinsicWidth();
		height = mAlarmHandMinutes.getIntrinsicHeight();
		mAlarmHandMinutes.setColorFilter(mLighterOverlayColor, PorterDuff.Mode.MULTIPLY);
		mAlarmHandMinutes.setAlpha(200);
		mAlarmHandMinutes.setBounds(actualX - (width / 2), actualY - (height / 2), actualX + (width / 2), actualY + (height / 2));
		mAlarmHandMinutes.draw(canvas);
		canvas.restore();
		
		// draw seconds
		canvas.save();
		canvas.rotate(mClockInstance.getSeconds() * 6.0f, actualX, actualY);
		width = mSecondHand.getIntrinsicWidth();
		height = mSecondHand.getIntrinsicHeight();
		mSecondHand.setBounds(actualX - (width / 2), actualY - (height / 2), actualX + (width / 2), actualY + (height / 2));
		mSecondHand.setColorFilter(mShiftedOverlayColor, android.graphics.PorterDuff.Mode.MULTIPLY);
		mSecondHand.draw(canvas);
		canvas.restore();
		
		if (scaled) {
			canvas.restore();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize =  MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize =  MeasureSpec.getSize(heightMeasureSpec);

		float hScale = 1.0f;
		float vScale = 1.0f;

		if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
			hScale = (float) widthSize / (float) mDialWidth;
		}

		if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
			vScale = (float )heightSize / (float) mDialHeight;
		}

		float scale = Math.min(hScale, vScale);
		setMeasuredDimension(200, 200);
		setMeasuredDimension(resolveSize((int) (mDialWidth * scale), widthMeasureSpec),
				resolveSize((int) (mDialHeight * scale), heightMeasureSpec));
	}

	@Override
	public void pauseClock() {
		mHandler.removeCallbacks(mRunnable);
	}

	@Override
	public void setMinutesToAlarm(int minutesToAlarm) {
		mInterval = minutesToAlarm;
	}

	@Override
	public void setTime(final int hours, final int minutes, final int seconds)
	{
		//		Logger.d(TAG, "Setting time to " + hours + " " + minutes + " " + seconds);
		mClockInstance.setSeconds(6.0f*seconds);
		mClockInstance.setMinutes(minutes + seconds / 60.0f);
		mClockInstance.setHour(hours + minutes / 60.0f);
		invalidate();
	}
	
	private void setTimeRunning(final long timeRunning) {
		this.mTimeRunning = timeRunning;
	}

	@Override
	public void startClock(final long startTime, final int minutesToAlarm) {
        if (startTime == 0) {
            resumeClock();
            return;
        }

		mStartTime = startTime;
		mInterval = minutesToAlarm;
		mHandler.post(mRunnable);
	}

    public void resumeClock() {
        startClock(Calendar.getInstance().getTimeInMillis() - getTimeRunning()*1000, 0);
    }

	@Override
	public void stopClock() {
        setTimeRunning(0);
        mStartTime = 0;
		mClockInstance.reset();
		invalidate();
		mHandler.removeCallbacks(mRunnable);
	}

	/**
	 * Method to process Handler ticks
	 */
	@Override
	public void updateTime() {
		setTimeRunning((Calendar.getInstance().getTimeInMillis() - 
				mStartTime) / 1000);

		if (mStartTime == 0) {
			mClockInstance.reset();
			invalidate();
			return;
		}

		// convert time into an array
		final float[] timeIntArray = PrimitiveConversions.getHourMinuteFloatTimeArrayFromSeconds(getTimeRunning());

		// set time for the clock instance
		mClockInstance.setTime(timeIntArray[0], timeIntArray[1], timeIntArray[2]);

		mChanged = true;
		invalidate();
	}

	@Override
	public void setSystemTime() {
        // TODO Auto-generated method stub
	}
}