package com.melnykov.callmeback.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.makeramen.RoundedTransformationBuilder;
import com.melnykov.callmeback.R;
import com.squareup.picasso.Transformation;

public class ShortcutIconTransformation implements Transformation {

    private final Context mContext;
    private final Transformation mRoundedTransformation;

    public ShortcutIconTransformation(Context context) {
        mContext = context;
        mRoundedTransformation = new RoundedTransformationBuilder()
            .oval(true)
            .build();
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int bitmapSize = mContext.getResources().getDimensionPixelSize(R.dimen.launcher_icon_size);
        Bitmap scaledPhotoBitmap = Bitmap.createScaledBitmap(source, bitmapSize, bitmapSize, false);
        if (!source.equals(scaledPhotoBitmap)) {
            source.recycle();
        }

        Bitmap roundedPhotoBitmap = mRoundedTransformation.transform(scaledPhotoBitmap);
        if (!scaledPhotoBitmap.equals(roundedPhotoBitmap)) {
            scaledPhotoBitmap.recycle();
        }

        Bitmap iconBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
        int iconSize = (int) (roundedPhotoBitmap.getWidth() * 0.4);
        Bitmap scaledIconBitmap = Bitmap.createScaledBitmap(iconBitmap,
            iconSize, iconSize, false);
        if (!iconBitmap.equals(scaledIconBitmap)) {
            iconBitmap.recycle();
        }

        Bitmap resultBitmap = Bitmap.createBitmap(roundedPhotoBitmap.getWidth(),
            roundedPhotoBitmap.getHeight(), roundedPhotoBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(roundedPhotoBitmap, new Matrix(), null);
        canvas.drawBitmap(scaledIconBitmap, resultBitmap.getWidth() - scaledIconBitmap.getWidth(),
            resultBitmap.getHeight() - scaledIconBitmap.getHeight(), null);

        roundedPhotoBitmap.recycle();
        scaledIconBitmap.recycle();

        return resultBitmap;
    }

    @Override
    public String key() {
        return "ShortcutIconTransformation";
    }
}