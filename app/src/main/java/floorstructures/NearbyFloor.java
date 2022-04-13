package floorstructures;

import java.util.LinkedList;

import explore.Cam;
import server.Server;
import worldscreenpositions.LLPos;
import worldscreenpositions.ScreenPos;

public class NearbyFloor {

    long BuildingID, FloorID;

    LLPos center;
    Cam camera;

    public NearbyFloor(long BuildingIDg, long FloorIDg, LLPos centerg, Cam camerag){
        BuildingID = BuildingIDg;
        FloorID = FloorIDg;
        center = centerg;
        camera = camerag;
    }

    public boolean CheckInBounds(){

        ScreenPos screenp = camera.GetScreenP();
        ScreenPos screenp2 = center.ToScreen(camera);

        int left1 = screenp.GetLeft(camera);
        int top1 = screenp.GetTop(camera);

        int left2 = screenp.GetLeft(camera);
        int top2 = screenp.GetTop(camera);

        int difleft = left2 - left1;
        int diftop = top2 - top1;

        int h = (int)camera.getHeight();
        return difleft * difleft + diftop * diftop < 3 * h * 3 * h;
    }

    public void Promote(LinkedList<Floor> DownloadedFloors){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    String recstring = Server.Download(new double[]{FloorID});
                    Floor newfloor = new Floor(camera,FloorID,BuildingID,center,recstring);
                    DownloadedFloors.add(newfloor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}

/*

NearbyFloor .-> LL
            |-> drawRadius
            |-> receive()
            |-> checkInbounds()
            '-> promote()


 */