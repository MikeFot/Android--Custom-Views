package com.michaelfotiadis.androidcustomviews.edittext;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class DateEditText extends AppCompatEditText {
    private String current = "";
    private boolean override = false;

    public DateEditText(final Context context) {
        super(context);
    }

    public DateEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public DateEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_NUMBER);
        addTextChangedListener(new TextWatcher() {

            private static final String DATE_FORMAT = "MMYY";
            private final Calendar cal = Calendar.getInstance();
            int sel;


            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, final int start, final int before, final int count) {
                Log.d("CustomView", "Input " + s);
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    final String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    final int cl = clean.length();
                    sel = cl;
                    for (int i = 2; i <= cl && i < 4; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC) && clean.length() > 0) {
                        //sel--;
                        setText(clean.substring(0, clean.length() - 1));
                        return;
                    }

                    if (clean.length() < 4) {
                        clean = clean + DATE_FORMAT.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon = Integer.parseInt(clean.substring(0, 2));
                        int year = Integer.parseInt(clean.substring(2, 4));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, mon - 1);
                        clean = String.format(Locale.UK, "%02d%02d", mon, year);
                    }

                    clean = String.format("%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    setText(current);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (sel >= s.length()) {
                    sel = s.length();
                }
                setSelection(sel);
            }
        });

        setHint("MM/YY");
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }
}
