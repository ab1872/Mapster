package floorstructures;


import android.graphics.Canvas;
import android.graphics.Paint;

import explore.Cam;
import worldscreenpositions.LLPos;
import worldscreenpositions.ScreenPos;
import worldscreenpositions.Seg;

import java.util.LinkedList;

public class Floor {
    private String floortext;
    private LinkedList<Seg> PathList;
    private Cam camera;
    private long FloorID, BuildingID;
    private double UndrawRadius;
    private LLPos center;

    public Floor(Cam camerag, long FloorIDg, long BuildingIDg, LLPos centerg, String floortextg){
        camera = camerag;
        floortext = floortextg;
        FloorID = FloorIDg;
        BuildingID = BuildingIDg;
        center = centerg;
        // Start parsing...

        PathList = Floor.textToSeglist(floortextg, camera);

        // Done Parsing.

        /* Set Undraw Radius */
        SetUndrawRadius();
    }
    public static LinkedList<Seg> textToSeglist(String floortext, Cam camera){
        String[] split1 = floortext.split(" ");
        LinkedList<Seg> PathList = new LinkedList<Seg>();

        for(int i=0; i<split1.length; i++){
            String[] split2 = split1[i].split(",");
            double longitudea = Double.parseDouble(split2[0]);
            double latitudea = Double.parseDouble(split2[1]);
            double longitudeb = Double.parseDouble(split2[2]);
            double latitudeb = Double.parseDouble(split2[3]);
            LLPos LLa = new LLPos(longitudea, latitudea);
            LLPos LLb = new LLPos(longitudeb, latitudeb);
            Seg path = new Seg(LLa, LLb, camera);
            PathList.add(path);
        }
        return PathList;
    }
    public static String seglistToText(LinkedList<Seg> seglist){
        String buffer = "";
        for(int i=0; i<seglist.size(); i++){
            Seg path = seglist.get(i);
            LLPos LLa = path.GetA();
            LLPos LLb = path.GetB();
            double longitudea = LLa.GetLongitude();
            double latitudea = LLa.GetLatitude();
            double longitudeb = LLb.GetLongitude();
            double latitudeb = LLb.GetLatitude();
            buffer = buffer.concat(String.valueOf(longitudea)).concat(",").concat(String.valueOf(longitudea)).concat(",").concat(String.valueOf(longitudeb)).concat(",").concat(String.valueOf(latitudeb)).concat(" ");
        }
        return buffer.trim();
    }

    private void SetUndrawRadius(){
        // Get max X, Y extend
        // Constant times that.
        double minleft = 0, maxleft = 0;
        double mintop = 0, maxtop = 0;

        for(int i=0; i<PathList.size(); i++){
            Seg gotseg = PathList.get(i);
            ScreenPos spa = gotseg.GetA().ToScreen(camera);
            ScreenPos spb = gotseg.GetB().ToScreen(camera);
            double aleft = (double)spa.GetLeft(camera);
            double atop = (double)spa.GetTop(camera);
            double bleft = (double)spb.GetLeft(camera);
            double btop = (double)spb.GetTop(camera);

            if(i==0){
                if(btop > atop){
                    maxtop = btop;
                    mintop = atop;
                }
                else{
                    maxtop = atop;
                    mintop = btop;
                }
                if(btop > atop){
                    maxleft = bleft;
                    minleft = aleft;
                }
                else{
                    maxleft = aleft;
                    minleft = bleft;
                }
            }
            else{
                if(atop > maxtop){
                    maxtop = atop;
                }
                if(btop > maxtop){
                    maxtop = btop;
                }
                if(aleft > maxleft){
                    maxleft = aleft;
                }
                if(bleft > maxleft){
                    maxleft = bleft;
                }

                if(atop < mintop){
                    mintop = atop;
                }
                if(btop < mintop){
                    mintop = btop;
                }
                if(aleft < minleft){
                    minleft = aleft;
                }
                if(bleft < minleft){
                    minleft = bleft;
                }
            }
        }


        double topextent = maxtop - mintop;
        double leftextent = maxleft - minleft;
        double radius = Math.sqrt(topextent*topextent + leftextent*leftextent);
        UndrawRadius = radius * 2;
    }

    private double GetUndrawRadius(){
        return UndrawRadius;
    }

    private boolean CheckUndrawRadius() {
        ScreenPos cp = camera.GetScreenP();
        ScreenPos fp = center.ToScreen(camera);

        double cpt = cp.GetTop(camera);
        double cpl = cp.GetLeft(camera);

        double fpt = fp.GetTop(camera);
        double fpl = fp.GetLeft(camera);

        double tdif = fpt-cpt;
        double ldif = fpl-cpl;

        return Math.sqrt(tdif*tdif + ldif*ldif)> UndrawRadius;
    }

    public String GetText(){
        return floortext;
    }

    public LinkedList<Seg> GetPathList(){
        return PathList;
    }

    public long GetFloorID(){
        return FloorID;
    }

    public long GetBuildingID(){
        return BuildingID;
    }

    public void Plot(Canvas canvas, Paint paint){
        for(int i=0; i<PathList.size(); i++){
            PathList.get(i).Plot(canvas, paint);
        }
    }
}

/*
Floor--> Path
     |-> ...
     '-> Path-->From(LL), To(LL). Path.draw(canvas){canvas.drawLine(From.XScreen, From.YScreen, To.XScreen, To.YScreen, createdPaint)}
         toString(), fromString()
     |-> FloorID, BuildingID
     |-> LL() (average position)
     |-> undrawRadius
     |-> checkOutbounds(cam)
     |-> (delete)
     |-> draw(canvas)


 */