package mapdisplayutil;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.LinkedList;

public class PathList extends LinkedList<LinePath> {

    //public ScreenPos GetBoundingRectangle();

    public double getMaxDiagonal(){
        return 1;
    }

    //public LLPos GetAveragePos();

    //public String toString();
    @Override
    public String toString(){
        String buffer = "";

        for(int i = 0; i<this.size(); i++){
            buffer = buffer + this.get(i) + "\n";
        }

        return buffer.trim();
    }

    //public static fromString(String s);
    public static PathList fromString(String s, double altitude, ScreenParameters sp){
        String[] spl = s.split("; ");
        PathList pl = new PathList();
        for(int i = 0; i<spl.length; i++) {
            pl.add(LinePath.fromString(s,altitude,sp));
        }
        return pl;
    }

    public void Plot(Canvas canvas, Paint paint){
        for(int i = 0; i<this.size(); i++){
            this.get(i).Plot(canvas, paint);
        }
    }

}
