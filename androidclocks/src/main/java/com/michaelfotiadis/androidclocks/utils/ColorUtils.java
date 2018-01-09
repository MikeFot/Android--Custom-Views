package com.michaelfotiadis.androidclocks.utils;

import android.graphics.Color;

public class ColorUtils {

	public int getLighterColor(final int color) {

		float[] hsv = new float[3];
		
		Color.colorToHSV(color, hsv);
		hsv[2] = 1.5f * hsv[2];
		
		if (hsv[2] > 1) {
			hsv[2] = 1;
		}
		
		return Color.HSVToColor(hsv);
	}
	
	public int getDarkerColor(final int color) {

		float[] hsv = new float[3];
		
		Color.colorToHSV(color, hsv);
		hsv[2] = 0.5f * hsv[2];
		
		if (hsv[2] > 1) {
			hsv[2] = 1;
		}
		
		return Color.HSVToColor(hsv);
	}
	
	public int getRightBitShiftedColor(final int color) {
		return color >> 16;
	}
	
}
