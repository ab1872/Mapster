package explore;

import android.content.Context;
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

import floorstructures.Building;
import floorstructures.Buildings;
import floorstructures.Floor;
import floorstructures.FloorList;
import mapdisplayutil.PathList;
import mapdisplayutil.PosMapping;
import mapdisplayutil.ScreenParameters;
import mapdisplayutil.ScreenPos;
import server.Server;
import utilities.FindMainActivity;
import mapdisplayutil.LLPos;
import utilities.GetWidthHeight;

public class CustomView extends View {
    MainActivity mainac;

    Paint paint;
    Server server;
    Context ctx;


    Buildings buildings;
    ScreenParameters viewDim;

    public CustomView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        /* Get the main activity */
        ctx = context;
        mainac = FindMainActivity.Find(context);
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

        viewDim = new ScreenParameters(new LLPos(40.5227829535632, -74.43743760755362), GetWidthHeight.getWidth(this), GetWidthHeight.getHeight(this), 500);
        PosMapping pm = new PosMapping(new ScreenPos(10, 10), viewDim.getCameraAltitude(), viewDim);

        /* Initialize the buildings */

        PathList pltest = PathList.fromString("40.5227829535632, -74.43743760755362; 40.522559489342676, -74.43703173340047; 40.522204828469775, -74.43737881269966; 40.52242973558246, -74.43777899702823; 40.5227829535632, -74.43743760755362", 300, viewDim);
        FloorList fltest = new FloorList();
        Floor ftest = new Floor("first","first",pltest);
        fltest.add(ftest);
        buildings = new Buildings();
        buildings.add(new Building("floor",fltest));
    }

    @Override
    protected void onDraw(Canvas canvas) { /* Done */
        super.onDraw(canvas);
        Log.i("DRAWING","DRAWING");
        buildings.draw(viewDim.getCameraAltitude(), canvas, paint);
    }


    class ScrollListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
           try {
               LLPos pos = viewDim.getCameraLL();
               double newLatitude = pos.GetLatitude() - distanceY / (111162  * 19.2);
               double newLongitude = pos.GetLongitude() + distanceX / (76407 * 19.2);

               viewDim.updateLL(new LLPos(newLatitude, newLongitude));
               postInvalidate();
               Log.i("New cam pos", ""+viewDim.getCameraLL());

           } catch(Exception e){
               e.printStackTrace();
           }

            return true;
        }
    }
    GestureDetector detector = new GestureDetector(ctx, new ScrollListener());

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean val = detector.onTouchEvent(event);
        postInvalidate();
        return val;
    }
}
