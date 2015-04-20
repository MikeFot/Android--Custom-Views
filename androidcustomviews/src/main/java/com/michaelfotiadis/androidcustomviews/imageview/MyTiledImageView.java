package com.michaelfotiadis.androidcustomviews.imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Map on 19/04/2015.
 */
public class MyTiledImageView extends ImageView {


    public MyTiledImageView(Context context) {
        this(context, null);
    }

    public MyTiledImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTiledImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    public void createImageArrays(final Drawable drawable, int chunkNumbers, final boolean showGrid) {
        this.setImageDrawable(null);

        // make sure chunk numbers are at least 1
        if (chunkNumbers < 1)
            chunkNumbers = 1;

        //Getting the scaled bitmap of the source image
        final Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        // initialise an empty bitmap with the size of the original one
        final Bitmap cBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        // initialise a canvas based on the empty bitmap
        final Canvas canvas = new Canvas(cBitmap);

        final int rows = (int) Math.sqrt(chunkNumbers);
        final int cols = rows;
        final int chunkHeight = bitmap.getHeight() / rows;
        final int chunkWidth = bitmap.getWidth() / cols;

        final Paint rectPaint = new Paint();
        rectPaint.setColor(Color.BLACK);
        rectPaint.setStyle(Paint.Style.STROKE);

        Bitmap drawableBitmap;

        //xCoord and yCoord are the pixel positions of the image chunks
        int yCoord = 0;
        for (int r = 0; r < rows; r++) {
            int xCoord = 0;
            for (int c = 0; c < cols; c++) {
                // create the chunked bitmap
                drawableBitmap = Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight);
                // add it to the canvas
                canvas.drawBitmap(drawableBitmap, xCoord, yCoord, new Paint());

                if (showGrid)
                canvas.drawRect((float)xCoord, (float)yCoord,
                        (float) (xCoord + chunkWidth), (float) (yCoord + chunkHeight)-1, rectPaint);

                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }

        //Attach the canvas to the ImageView
        this.setImageDrawable(new BitmapDrawable(getResources(), cBitmap));
//        bitmap.recycle();
    }
}
