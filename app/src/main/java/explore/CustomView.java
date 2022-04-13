package explore;

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
        // For all floors:
        // Draw floors:
        canvas.drawLine(0, 0, rand.nextInt(500), 555, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        postInvalidate();
        return super.onTouchEvent(event);

        // On move: set camera position.

        // On settled: 1) clean up floors (for all floors)
        // 2) If displacement from last asked.... Ask for floors (async) download when received, postInvalidate()
    }
}
