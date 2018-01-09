package com.michaelfotiadis.androidclocks.containers;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.util.Arrays;

public class ClockInstance implements Parcelable {

	float mHour;
	float mMinutes;
	float mSeconds;

	public ClockInstance() {
		// Set all to 0 on initial creation
		this.reset();
	}

	/**
	 * Sets the values of the Clock Instance's parameters
	 * @param hours float
	 * @param minutes float
	 * @param seconds float
	 */
	public void setTime(float hours, float minutes, float seconds) {
		this.mHour = hours;
		this.mMinutes = minutes;
		this.mSeconds = seconds;
	}
	
	@Override
	public int describeContents() {
		// Auto generate Parcelable method
		return 0;
	}
	
	public float getHour() {
		return mHour;
	}

	public float getMinutes() {
		return mMinutes;
	}

	public float getSeconds() {
		return mSeconds;
	}

	/**
	 * Sets all values back to zero
	 */
	public void reset() {
		this.mHour = 0;
		this.mMinutes = 0;
		this.mSeconds = 0;
	}

	public void setHour(float mHour) {
		this.mHour = mHour;
	}

	public void setMinutes(float mMinutes) {
		this.mMinutes = mMinutes;
	}

	public void setSeconds(float mSeconds) {
		this.mSeconds = mSeconds;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeFloat(mHour);
		out.writeFloat(mMinutes);
		out.writeFloat(mSeconds);
	}

	/**
	 * Converts Clock Instance to Digital Clock representation
	 * @return String representation
	 */
	public String getString() {
		StringBuilder timeBuilder = new StringBuilder();
		timeBuilder.append(floatToString(mHour, 2));
		timeBuilder.append(":");
		timeBuilder.append(floatToString(mMinutes, 2));
		timeBuilder.append(":");
		timeBuilder.append(floatToString(mSeconds, 2));
		return timeBuilder.toString();
	}
	
	/**
	 * Converts a float to a String with the requested number of digits
	 * @param num Float to be converted
	 * @param digits Integer number of digits requested
	 * @return String value of the float
	 */
	private static String floatToString(float num, int digits) {
	    // create variable length array of zeros
	    char[] zeros = new char[digits];
	    Arrays.fill(zeros, '0');
	    // format number as String
	    DecimalFormat df = new DecimalFormat(String.valueOf(zeros));

	    return df.format(num);
	}
	
}
