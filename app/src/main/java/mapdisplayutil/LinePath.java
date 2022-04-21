package mapdisplayutil;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.LinkedList;

public class LinePath extends LinkedList<PosMapping> {
    public void draw(Canvas canvas, Paint paint){
        for (int i=0; i<this.size()-1; i++){
            PosMapping current = this.get(i);
            if(i==0){Log.i("current",current.getLLP()+"");}
            PosMapping next = this.get(i+1);
            canvas.drawLine(
                    current.getLeft(), current.getTop(),
                    next.getLeft(), next.getTop(), paint
            );
        }
    }

    // Convex hull algorithm.
    // Concave hull algorithm. Start from convex hull,

    @Override
    public String toString(){
        String buffer = "";
        for(int i = 0; i<this.size(); i++){
            if(i!=this.size()-1){
                buffer = buffer + this.get(i) + "; ";
            }
            else{
                buffer = buffer + this.get(i);
            }
        }
        return buffer;
    }

    public static LinePath fromString(String s, double altitude, ScreenParameters sp){
        String[] spl = s.split("; ");
        LinePath lp = new LinePath();
        for(int i = 0; i<spl.length; i++) {
            lp.add(PosMapping.fromString(spl[i],altitude,sp));
        }
        return lp;
    }
}
