package com.michaelfotiadis.androidclocks;



public interface MyClockInterface {
	abstract void updateTime();

	public void pauseClock();
	
	public void setTime(final int hours, final int minutes, final int seconds);
	
	public void startClock(final long startTime, final int minutesToAlarm);
	
	public void stopClock();
	
	public boolean isVisible();
	
	public long getTimeRunning();
	
	public void setMinutesToAlarm(final int minutesToAlarm);
	
	public void setSystemTime();
}
