package explore;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.map.MainActivity;

import java.util.LinkedList;
import java.util.Random;

import floorstructures.Floor;
import server.Server;
import utilities.FindMainActivity;
import worldscreenpositions.LLPos;
import worldscreenpositions.ScreenPos;
import worldscreenpositions.Seg;

public class CustomView extends View {
    MainActivity mainac;

    Paint paint;
    Cam camera;
    Server server;
    LinkedList<Floor> DownloadedFloors;
    Context ctx;

    LLPos LastAskForBuildingsPos;
    boolean HasAskForBuildings = false;

    public CustomView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        /* Get the main activity */
        ctx = context;
        mainac = FindMainActivity.Find(context);
        Log.i("Test",mainac.testString);
        /* Initialize the Paint */
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(6);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);

        /* Initialize the Camera object at New Brunswick */
        camera = new Cam(new LLPos(66.4619, 46.5653), 62);

        /* Initialize the Last Asked For Buildings Position */
        LastAskForBuildingsPos = camera.GetLL();

        /* Initialize the Floors in view */
        DownloadedFloors = new LinkedList<Floor>();
        //DownloadedFloors.add(...)

        /* Display the map initially */
        HasSettled();
    }

    /* METHOD to get GPS pos */

    /* Called when the scroll flywheel settles */
    private void HasSettled(){
        /* If it's the first time asking for buildings */
        if(HasAskForBuildings == false){
            HasAskForBuildings = true;
        }
        else{

        }
        LastAskForBuildingsPos = camera.GetLL();
    }

    @Override
    protected void onDraw(Canvas canvas) { /* Done */
        super.onDraw(canvas);
        /* For all floors, Draw it on canvas */
        for(int i = 0; i < DownloadedFloors.size(); i++){
            DownloadedFloors.get(i).Plot(canvas, paint);
        }
    }


    class ScrollListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            ScreenPos campos = camera.GetScreenP();

            double newY = campos.GetY() + distanceY;
            double newX = campos.GetX() - distanceX;
            camera.SetPos(new ScreenPos(newX,newY));
            Log.i("Camera pos", camera.GetLL()+"");

            if (e2.getAction() == MotionEvent.ACTION_UP) {
                HasSettled();
            }
            //Log.i("Camera Pos", campos.ToLL(camera)+"");


            return true;
        }
    }
    GestureDetector detector = new GestureDetector(ctx, new ScrollListener());

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
        // On move: set camera position.

        // On settled: 1) clean up floors (for all floors)
        // 2) If displacement from last asked.... or has ask Ask for floors (async) download when received, postInvalidate()
    }
}
