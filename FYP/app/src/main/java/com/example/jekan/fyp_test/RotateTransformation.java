package com.example.jekan.fyp_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by jekan on 2018-02-21.
 */

public class RotateTransformation extends BitmapTransformation{

    private float rotateRotationAngle;
    private Matrix matrix;

    public RotateTransformation(Context context, float rotateRotationAngle) {
        super(context);
        this.rotateRotationAngle = rotateRotationAngle;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        matrix = new Matrix();

        if(rotateRotationAngle == 0 )
            matrix.postRotate(0);
        else if(rotateRotationAngle == 1)
            matrix.postRotate(90);
        else if(rotateRotationAngle == 2)
            matrix.postRotate(180);
        else if(rotateRotationAngle == 3)
            matrix.postRotate(270);
        else
            matrix.postRotate(360);


       // matrix.postRotate(rotateRotationAngle);
        return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, false);
    }

   /* @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(("rotate" + rotateRotationAngle).getBytes());
    }*/

    @Override
    public String getId() {
        return "rotate"+rotateRotationAngle;
    }
}
