package com.michaelfotiadis.androidclocks;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews.RemoteView;

import com.michaelfotiadis.androidclocks.containers.ErgoClockInstance;
import com.michaelfotiadis.androidclocks.utils.ColorUtils;
import com.michaelfotiadis.androidclocks.utils.PrimitiveConversions;

import java.util.Calendar;


/**
 * This widget display an analogue clock with two hands for hours and
 * minutes.
 */
@RemoteView
public class MyFusionClock extends View implements MyClockInterface {

	private final long HANDLER_UPDATE_INTERVAL = 10;

	private Drawable mHourHand;
	private Drawable mMinuteHand;
	private Drawable mSecondHand;
	private Drawable mDial;
	private Drawable mAlarmHandMinutes;
	private Drawable mAlarmHandHours;
	
	private int mDialWidth;
	private int mDialHeight;

	private boolean mChanged;
	
	private boolean isClockRunning;

	// overlay color for color filter
	private int mOverlayColor = Integer.MIN_VALUE;
	private int mLighterOverlayColor = Integer.MIN_VALUE;
	private int mShiftedOverlayColor = Integer.MIN_VALUE;

	private int mInterval;
	
	private final Handler mHandler = new Handler();

	private final String TAG = "My Fusion Clock";

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

	public MyFusionClock(Context context) {
		super(context);
	}

	public MyFusionClock(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	private MyFusionClock(Context context, AttributeSet attrs,
                          int defStyle) {
		super(context, attrs, defStyle);
		Resources resources = context.getResources();
		mDial = resources.getDrawable(R.drawable.clock_fusion_dial_r_three);
		mHourHand = resources.getDrawable(R.drawable.clock_fusion_hour_hand);
		mMinuteHand = resources.getDrawable(R.drawable.clock_fusion_hand_minutes);
		mSecondHand = resources.getDrawable(R.drawable.clock_fusion_second);
		mAlarmHandMinutes = resources.getDrawable(R.drawable.clock_fusion_minute_back);
		mAlarmHandHours = resources.getDrawable(R.drawable.clock_fusion_hour_back);
		
		mDialWidth = mDial.getIntrinsicWidth();
		mDialHeight = mDial.getIntrinsicHeight();

        if (mOverlayColor == Integer.MIN_VALUE)
            setOverlayColor("#ff0099cc");

		mLighterOverlayColor = new ColorUtils().getLighterColor(mOverlayColor);
		mShiftedOverlayColor = new ColorUtils().getRightBitShiftedColor(mOverlayColor);
		
	}

    public void setOverlayColor(final String hexColour) {
        try {
            this.mOverlayColor = Color.parseColor(hexColour);
        } catch (Exception e) {
            this.mOverlayColor = Color.parseColor("#ff0099cc");
            Log.e(TAG, e.getLocalizedMessage());
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
		Log.d(TAG, "Analogue Clock Attached to Window");
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
		canvas.save();
		
		// draw the hours alarm indication
		canvas.save();
		canvas.rotate((int)((mInterval / 60) / 12.0f * 360.0f), actualX, actualY);
		width = mAlarmHandHours.getIntrinsicWidth();
		height = mAlarmHandHours.getIntrinsicHeight();
		mAlarmHandHours.setColorFilter(mOverlayColor, android.graphics.PorterDuff.Mode.MULTIPLY);
		mAlarmHandHours.setBounds(actualX - (width / 2), actualY - (height / 2), actualX + (width / 2), actualY + (height / 2));
		mAlarmHandHours.draw(canvas);
		canvas.restore();
		
		// draw the clock hour hand on top of the hours indication
		canvas.rotate(mClockInstance.getHour() / 12.0f * 360.0f, actualX, actualY);
		width = mHourHand.getIntrinsicWidth();
		height = mHourHand.getIntrinsicHeight();
		mHourHand.setColorFilter(mShiftedOverlayColor, android.graphics.PorterDuff.Mode.MULTIPLY);
		mHourHand.setBounds(actualX - (width / 2), actualY - (height / 2), actualX + (width / 2), actualY + (height / 2));
		mHourHand.draw(canvas);
		canvas.restore();
		
		// draw the minutes alarm indication which includes the backface of the minutes drawable
		canvas.save();
		// rotate effectively once
		canvas.rotate(mInterval / 60.0f * 360.0f, actualX, actualY);
		width = mAlarmHandMinutes.getIntrinsicWidth();
		height = mAlarmHandMinutes.getIntrinsicHeight();
		mAlarmHandMinutes.setColorFilter(mShiftedOverlayColor, android.graphics.PorterDuff.Mode.MULTIPLY);
		mAlarmHandMinutes.setBounds(actualX - (width / 2), actualY - (height / 2), actualX + (width / 2), actualY + (height / 2));
		mAlarmHandMinutes.draw(canvas);
		canvas.restore();
		
		// draw minutes
		canvas.save();
		canvas.rotate(mClockInstance.getMinutes() / 60.0f * 360.0f, actualX, actualY);
		width = mMinuteHand.getIntrinsicWidth();
		height = mMinuteHand.getIntrinsicHeight();
		mMinuteHand.setColorFilter(mLighterOverlayColor, android.graphics.PorterDuff.Mode.MULTIPLY);
		mMinuteHand.setBounds(actualX - (width / 2), actualY - (height / 2), actualX + (width / 2), actualY + (height / 2));
		mMinuteHand.draw(canvas);
		canvas.restore();
		
		// draw seconds
		canvas.save();
		canvas.rotate(mClockInstance.getSeconds() * 6.0f, actualX, actualY);
		width = mSecondHand.getIntrinsicWidth();
		height = mSecondHand.getIntrinsicHeight();
		mSecondHand.setBounds(actualX - (width / 2), actualY - (height / 2), actualX + (width / 2), actualY + (height / 2));
		// set a color filter
		mSecondHand.setColorFilter(mOverlayColor, android.graphics.PorterDuff.Mode.MULTIPLY);
		mSecondHand.draw(canvas);
		canvas.restore();

		// handle the rotating graphic - use a boolean to enable draw
		if (isClockRunning) {
			canvas.rotate(rotation, actualX, actualY);
			dial.setColorFilter(mLighterOverlayColor, android.graphics.PorterDuff.Mode.MULTIPLY);
//			dial.setAlpha(20);
			dial.draw(canvas);
		}
		
		if (scaled) {
			canvas.restore();
		}
		rotation = (rotation + 2f) % 360;
		this.postInvalidate();

	}

	private float rotation = 0;

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
	public void setTime(final int hours, final int minutes, final int seconds)
	{
		//		Logger.d(TAG, "Setting time to " + hours + " " + minutes + " " + seconds);
		mClockInstance.setSeconds(6.0f*seconds);
		mClockInstance.setMinutes(minutes + seconds / 60.0f);
		mClockInstance.setHour(hours + minutes / 60.0f);
		invalidate();
	}

	private void setTimeRunning(long timeRunning) {
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
		isClockRunning = true;
		mHandler.post(mRunnable);
	}

	@Override
	public void stopClock() {
		isClockRunning = false;
		mClockInstance.reset();
        setTimeRunning(0);
        mStartTime = 0;
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
		float[] timeIntArray = PrimitiveConversions.getHourFloatTimeArrayFromSeconds(getTimeRunning());

		mClockInstance.setTime(timeIntArray[0], timeIntArray[1], timeIntArray[2]);

		mChanged = true;
		invalidate();
	}

	@Override
	public void setMinutesToAlarm(int minutesToAlarm) {
		mInterval = minutesToAlarm;
	}

    public void resumeClock() {
        // create a false start point using time running
        startClock(Calendar.getInstance().getTimeInMillis() - getTimeRunning()*1000, 0);
    }

	@Override
	public void setSystemTime() {
		// TODO Auto-generated method stub
	}
}