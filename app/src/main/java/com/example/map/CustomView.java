package com.example.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class CustomView extends View {
    Paint paint = new Paint();

    Random rand = new Random();
    public CustomView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(6);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, 0, rand.nextInt(500), 555, paint);
        //canvas.drawARGB(255, 255,0,0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        postInvalidate();
        return super.onTouchEvent(event);
    }
}
