package com.michaelfotiadis.androidclocks.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PrimitiveConversions {

	/**
	 * Return date in specified format.
	 * @param milliSeconds Date in milliseconds
	 * @param dateFormat Date format 
	 * @return String representing date in specified format
	 */
	public static String getDate(final long milliSeconds, final String dateFormat)
	{
		// Create a DateFormatter object for displaying date in specified format.
		final SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
		
		Date date = new Date(milliSeconds);
		return formatter.format(date);
	}

	/**
	 * Converts time passed in seconds to an integer array of hours:minutes:seconds
	 * @param timeSeconds
	 * @return
	 */
	public static int[] getIntTimeArrayFromSeconds(final long timeSeconds) {
		
		final int hours = (int) timeSeconds / 3600;
		int remainder = (int) timeSeconds - hours * 3600;
		final int mins = remainder / 60;
		remainder = remainder - mins * 60;
		final int secs = remainder;

		final int[] ints = {hours , mins , secs};
		return ints;
	}
	
	/**
	 * Converts time passed in seconds to an Hour-Minute float array of hours:minutes:seconds
	 * @param timeSeconds
	 * @return
	 */
	public static float[] getHourMinuteFloatTimeArrayFromSeconds(final long timeSeconds) {
		final float hours = timeSeconds / 3600.0f;
		int remainder = (int) (timeSeconds - (int)(timeSeconds / 3600) * 3600);
		final float mins = remainder / 60.0f;
		remainder = remainder - ((int) remainder / 60) * 60;
		final int secs = remainder;
		return new float[]{hours , mins , secs};
	}
	
	
	/**
	 * Converts time passed in seconds to an HOUR float array only
	 * @param timeSeconds
	 * @return
	 */
	public static float[] getHourFloatTimeArrayFromSeconds(final long timeSeconds) {
		final float hours = timeSeconds / 3600.0f;
		int remainder = (int) (timeSeconds - (int)(timeSeconds / 3600) * 3600);
		final int mins = remainder / 60;
		remainder = remainder - mins * 60;
		final int secs = remainder;
		return new float[]{hours , mins , secs};
	}
	
	/**
	 * Creates a printable time String of format HH:MM:SS
	 * @param timeArray integer array of length 3
	 * @return String representation
	 */
	public static String getTimeStringFromIntegerArray(final int[] timeArray) {
		StringBuilder sb = new StringBuilder();
		sb.append(integerToTwoDigitString(timeArray[0]));
		sb.append(":");
		sb.append(integerToTwoDigitString(timeArray[1]));
		sb.append(":");
		sb.append(integerToTwoDigitString(timeArray[2]));
		return sb.toString();
	}
	
	public static String getTimeStringFromSeconds(final long timeSeconds) {
		return (getTimeStringFromIntegerArray(getIntTimeArrayFromSeconds(timeSeconds)));
	}
	
	/**
	 * Converts an integer to 2 digit time String
	 * @param value integer to be converted
	 * @return length 2 String with leading "0"
	 */
	public static String integerToTwoDigitString(final int value) {
		
		if (value < 10) {
			return ("0" + value);
		} else {
			return String.valueOf(value);
		}
	}
	
	public static boolean tryBoolean(String value, boolean defaultValue) {
		try{
			return Boolean.parseBoolean(value);
		}catch(Exception e){
			return defaultValue;
		}
	}

	public static double tryDouble(String value, Double defaultValue){
		try{
			return Double.parseDouble(value);
		}catch(Exception e){
			return defaultValue;
		}
	}

	public static float tryDouble(String value, float defaultValue){ 
		try{
			return Float.parseFloat(value);
		}catch(Exception e){
			return defaultValue;
		}
	}

	public static int tryInteger(String value, Integer defaultValue){ 
		try{
			return Integer.parseInt(value);
		}catch(Exception e){
			return defaultValue;
		}
	}

	public static long tryLong(String value, Long defaultValue) {
		try{
			return Long.parseLong(value);
		}catch(Exception e){
			return defaultValue;
		}
	}
	
	public static String tryString(String suspectString, String defaultString){
		if(suspectString==null){
			return defaultString;
		}

		if(suspectString.trim().length()>0){
			return suspectString;
		} else {
			return defaultString;
		}
	} 

	public int safeLongToInt(long l) {
		if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		return (int) l;
	}

}