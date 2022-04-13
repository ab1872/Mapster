package explore;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.Random;

import floorstructures.Floor;
import server.Server;
import worldscreenpositions.LLPos;
import worldscreenpositions.Seg;

public class CustomView extends View {
    Paint paint;
    Cam camera;
    Server server;
    LinkedList<Floor> DownloadedFloors;

    LLPos LastAskForBuildingsPos;

    public CustomView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    private void init() {
        /* Initialize the Paint */
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(6);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);

        /* Initialize the Camera object */
        camera = new Cam(new LLPos(66.4619, 46.5653));

        /* Initialize the Last Asked For Buildings Position */
        LastAskForBuildingsPos = camera.GetLL();

        /* Initialize the Floors in view */
        DownloadedFloors = new LinkedList<Floor>();
        DownloadedFloors.add(new Floor(camera,1,1,"242"));
    }

    @Override
    protected void onDraw(Canvas canvas) { /* Done */
        super.onDraw(canvas);
        /* For all floors, Draw it on canvas */
        for(int i = 0; i < DownloadedFloors.size(); i++){
            DownloadedFloors.get(i).Plot(canvas, paint);
        }
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
