package floorstructures;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.LinkedList;

import mapdisplayutil.PathList;

public class FloorList extends LinkedList<Floor> {
    public Floor getFloorFromAltitude(double altitude){
        Floor maxBelow = this.getFirst();
        for(Floor fl : this){
            PathList pl = fl.getPathList();
            double alt = pl.getAltitude();
            if(alt < altitude && alt > maxBelow.getPathList().getAltitude()){
                maxBelow = fl;
            }
        }
        return maxBelow;
    }

    public void draw(Canvas canvas, Paint paint){
        for(Floor fl : this){
            fl.getPathList().draw(canvas, paint);
        }
    }
}
