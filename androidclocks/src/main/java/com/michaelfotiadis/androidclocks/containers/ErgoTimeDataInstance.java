package com.michaelfotiadis.androidclocks.containers;

import com.michaelfotiadis.androidclocks.utils.PrimitiveConversions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Custom Object for storing data related to logged time
 * @author Michael Fotiadis
 *
 */
public class ErgoTimeDataInstance {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    // Calendar Fields
    public static final String EXTENDED_DATE_FORMAT_STRING = "yyyy MM dd HH:mm:ss";
    public static final String SIMPLE_DATE_FORMAT_STRING = "HH:mm:ss";
    public static final SimpleDateFormat EXTENDED_DATE_FORMAT = new SimpleDateFormat(
            EXTENDED_DATE_FORMAT_STRING, Locale.getDefault());

    // Used to convert milliseconds to minutes
    public static final int FACTOR_MSEC_TO_MINUTES = 3000; // TODO correct is 60000

    public static final int ALARM_NOTIFICATION_ID = 517; // random ID

	private final int mTimeElapsed;
	private final Calendar mCalendarLogged;
	
	public ErgoTimeDataInstance(final long timeStarted) {
		final Long currentTime = Calendar.getInstance().getTimeInMillis();
		mCalendarLogged = Calendar.getInstance();
		mTimeElapsed = (int) ((currentTime - timeStarted) / FACTOR_MSEC_TO_MINUTES);
	}

	public ErgoTimeDataInstance(final long timeLogged, final long timeElapsed) {
		
		mCalendarLogged = Calendar.getInstance();
		mCalendarLogged.setTimeInMillis(timeLogged);
		mTimeElapsed = (int) timeElapsed;
	}
	
	public ErgoTimeDataInstance(final int timeElapsed, final Calendar calendarLogged) {
		mTimeElapsed = timeElapsed;
		mCalendarLogged = calendarLogged;
	}

	public String getTimeString() {
		final int hours = getCalendarLogged().get(Calendar.HOUR_OF_DAY);
		final int minutes = getCalendarLogged().get(Calendar.MINUTE);
		final int seconds = getCalendarLogged().get(Calendar.SECOND);

		final int[] timeArray = new int[] {hours, minutes, seconds};

		return PrimitiveConversions.getTimeStringFromIntegerArray(timeArray);
		
		
	}

	public long getTimeLogged() {
		return mTimeElapsed;
	}

	public Calendar getCalendarLogged() {
		return mCalendarLogged;
	}
	
	public String getExtendedDateFormat() {
		SimpleDateFormat format = new SimpleDateFormat(EXTENDED_DATE_FORMAT_STRING, Locale.getDefault());
		return format.format(mCalendarLogged.getTime());
	}
	
	
	public String getSimpleDateFormat() {
		SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT_STRING, Locale.getDefault());
		return format.format(mCalendarLogged.getTime());
	}
	
	public String toOutputString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(mCalendarLogged.getTimeInMillis());
		sb.append(',');
		sb.append(mTimeElapsed);
		// add a line separator
		sb.append(LINE_SEPARATOR);
		
		return sb.toString();
	}
	
}
