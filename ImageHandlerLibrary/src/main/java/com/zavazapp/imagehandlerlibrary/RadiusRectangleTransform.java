package com.zavazapp.imagehandlerlibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;



public class RadiusRectangleTransform implements Transformation {
    private int curveSize;

    public RadiusRectangleTransform(int curveSize) {
        this.curveSize = curveSize;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2; //width
        int y = (source.getHeight() - size) / 2; //height

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = 45f * curveSize;
        //canvas.drawCircle(r, r, r, paint);
        canvas.drawRoundRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), r, r, paint);
        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "rounded_corners";
    }
}
