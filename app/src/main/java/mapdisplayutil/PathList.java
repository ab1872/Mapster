package mapdisplayutil;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.LinkedList;

public class PathList extends LinkedList<LinePath> {

    //public ScreenPos GetBoundingRectangle();
    private double altitude;

    public double getMaxDiagonal(){
        return 1;
    }

    //public LLPos GetAveragePos();
    public LLPos getCenter(){
        return new LLPos(0,0);
    }

    //public String toString();
    @Override
    public String toString(){
        String buffer = "";

        for(int i = 0; i<this.size(); i++){
            if(i<this.size()-1) {
                buffer = buffer + this.get(i) + ".";
            }
            else{
                buffer = buffer + this.get(i);
            }
        }

        return buffer;
    }

    public double getAltitude(){
        return altitude;
    }

    public static PathList fromString(String s, double altitude, ScreenParameters sp){
        String[] spl = s.split("\\. ");
        PathList pl = new PathList();
        pl.altitude = altitude;
        for(int i = 0; i<spl.length; i++) {
            if(spl[i]!="") {
                pl.add(LinePath.fromString(spl[i], altitude, sp));
            }
        }
        return pl;
    }

    public void draw(Canvas canvas, Paint paint){
        for(int i = 0; i<this.size(); i++){
            this.get(i).draw(canvas, paint);
        }
    }

}
