package floorstructures;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.LinkedList;

import mapdisplayutil.LLPos;
import server.Server;

public class Buildings extends LinkedList<Building>  {

    private LLPos LastAskForBuildingsPos;
    private boolean HasAskForBuildings = false;

    public FloorList filterByAltitude(double altitude){
        FloorList newFl = new FloorList();
        for(Building bl : this){
            newFl.add(bl.getFloorList().getFloorFromAltitude(altitude));
        }
        return newFl;
    }

    public void askForBuildings(Server server){

    }

    public void addReceivedFloor(Floor floor){

    }

    public void draw(double altitude, Canvas can, Paint paint){
        filterByAltitude(altitude).draw(can, paint);
    }
}