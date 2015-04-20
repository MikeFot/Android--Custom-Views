package com.michaelfotiadis.androidcustomviews.gridview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MyTiledGridView extends GridView {
    private final Context mContext;
    private int mRowWidth;
    private int mRowHeight;

    public MyTiledGridView(Context context) {
        this(context, null);
    }

    public MyTiledGridView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyTiledGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void init(final Drawable drawable, final int chunkNumbers) {
        Bitmap[] bitmaps = createImageArrays(drawable, chunkNumbers);

        final Bitmap firstBitmap = bitmaps[0];

        mRowWidth = firstBitmap.getWidth();
        mRowHeight = firstBitmap.getHeight();

        final int columns = (int) Math.sqrt(chunkNumbers);

        this.setNumColumns(columns);
        this.setColumnWidth(mRowWidth);
        this.setStretchMode(NO_STRETCH);

        this.setAdapter(new CustomGridAdapter(bitmaps));
    }

    public Bitmap[] createImageArrays(final Drawable drawable, int chunkNumbers) {
        // make sure chunk numbers are at least 1
        if (chunkNumbers < 1)
            chunkNumbers = 1;

        Bitmap[] items = new Bitmap[chunkNumbers];

        //Getting the scaled bitmap of the source image
        final Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        // initialise an empty bitmap with the size of the original one
        final Bitmap cBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        final int rows = (int) Math.sqrt(chunkNumbers);
        final int cols = rows;
        final int chunkHeight = bitmap.getHeight() / rows;
        final int chunkWidth = bitmap.getWidth() / cols;

        final Paint rectPaint = new Paint();
        rectPaint.setColor(Color.BLACK);
        rectPaint.setStyle(Paint.Style.STROKE);

        Bitmap drawableBitmap;

        int i = 0;
        //xCoord and yCoord are the pixel positions of the image chunks
        int yCoord = 0;
        for (int r = 0; r < rows; r++) {
            int xCoord = 0;
            for (int c = 0; c < cols; c++) {
                // create the chunked bitmap
                drawableBitmap = Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight);

                items[i] = drawableBitmap;
                i++;
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }
        return items;
    }

    public class CustomGridAdapter extends BaseAdapter {
        // Keep all Images in array
        public Bitmap[] mThumbIds;


        // Constructor
        public CustomGridAdapter(Bitmap[] items) {
            this.mThumbIds = items;
        }

        @Override
        public int getCount() {
            return mThumbIds.length;
        }

        @Override
        public Object getItem(int position) {
            return mThumbIds[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageBitmap(mThumbIds[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(mRowWidth, mRowHeight));
            return imageView;
        }
    }
}
