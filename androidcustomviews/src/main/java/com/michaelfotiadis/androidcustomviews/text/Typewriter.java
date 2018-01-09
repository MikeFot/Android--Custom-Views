package com.michaelfotiadis.androidcustomviews.text;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class Typewriter extends TextView {

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 500; //Default 500ms delay
    private long mStoredDelay = 500;

    private INTERACTION_MODE mMode = INTERACTION_MODE.NONE;

    private Handler mHandler = new Handler();

    public Typewriter(final Context context) {
        super(context);
    }

    public Typewriter(final Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public enum INTERACTION_MODE {
        NONE, DOWN_TO_SPEED_UP, DOWN_TO_SHOW_ALL
    }

    public INTERACTION_MODE getInteractionMode() {
        return mMode;
    }

    public void setInteractionMode(final INTERACTION_MODE mode) {
        this.mMode = mode;
    }

    /**
     * Method which starts the text animation using a handler
     * @param text CharSequence content of the View
     */
    public void animateText(final CharSequence text) {
        mText = text;
        mIndex = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    /**
     * Runnable for the Handler
     */
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if(mMode == INTERACTION_MODE.NONE) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mMode == INTERACTION_MODE.DOWN_TO_SPEED_UP) {
                    // speed it up on action down
                    mStoredDelay = mDelay;
                    mDelay = 1;
                } else if (mMode == INTERACTION_MODE.DOWN_TO_SHOW_ALL) {
                    // show the entire text
                    setText(mText);
                    mHandler.removeCallbacks(characterAdder);
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (mMode == INTERACTION_MODE.DOWN_TO_SPEED_UP) {
                    // restore the previous delay on release
                    mDelay = mStoredDelay;
                    return true;
                }
            default:
                return true;
        }
    }

    /**
     * Setter for the delay in millis between the appearance of new characters
     * @param millis long delay
     */
    public void setCharacterDelay(final long millis) {
        mDelay = millis;
        mStoredDelay = millis;
    }
}