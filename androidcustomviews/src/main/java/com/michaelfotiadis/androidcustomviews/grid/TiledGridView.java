package com.michaelfotiadis.androidcustomviews.grid;

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

public class TiledGridView extends GridView {
    private final Context mContext;
    private int mRowWidth;
    private int mRowHeight;

    public TiledGridView(Context context) {
        this(context, null);
    }

    public TiledGridView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TiledGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    /**
     * Populates the gridview with a drawable
     * @param drawable Drawable to be inserted into the gridview
     * @param chunkNumbers integer number of chunks to be split into
     */
    public void setDrawable(final Drawable drawable, final int chunkNumbers, final boolean showBorders) {
        final Bitmap[] bitmaps = createImageArrays(drawable, chunkNumbers);

        final Bitmap firstBitmap = bitmaps[0];

        mRowWidth = firstBitmap.getWidth();
        mRowHeight = firstBitmap.getHeight();

        final int columns = (int) Math.sqrt(chunkNumbers);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = mRowWidth * columns;
        params.height = mRowHeight * columns;

        this.setNumColumns(columns);
        this.setColumnWidth(mRowWidth);

        this.setStretchMode(NO_STRETCH);

        toggleSpacing(showBorders);

        this.setAdapter(new CustomGridAdapter(bitmaps));
    }

    public void toggleSpacing(final boolean showBorders) {
        int spacing = 0;
        if (showBorders)
            spacing = 1;

        this.setVerticalSpacing(spacing);
        this.setHorizontalSpacing(spacing);
    }

    /**
     * Split a drawable into chunks and return the chunk array
     * @param drawable Drawable to be split
     * @param chunkNumbers Number of chunks
     * @return Bitmap array
     */
    public Bitmap[] createImageArrays(final Drawable drawable, int chunkNumbers) {
        // make sure chunk numbers are at least 1
        if (chunkNumbers < 1)
            chunkNumbers = 1;

        final Bitmap[] bitmapArray = new Bitmap[chunkNumbers];

        //Getting the scaled bitmap of the source image
        final Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

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
                // create the tiled bitmap
                drawableBitmap = Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight);

                bitmapArray[i] = drawableBitmap;
                i++;
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }
        return bitmapArray;
    }

    public class CustomGridAdapter extends BaseAdapter {
        // Keep all Images in array
        public Bitmap[] mBitmapItem;

        // Constructor
        public CustomGridAdapter(final Bitmap[] bitmapArray) {
            this.mBitmapItem = bitmapArray;
        }

        @Override
        public int getCount() {
            return mBitmapItem.length;
        }

        @Override
        public Object getItem(int position) {
            return mBitmapItem[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageBitmap(mBitmapItem[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(mRowWidth, mRowHeight));
            return imageView;
        }
    }
}