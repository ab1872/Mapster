package floorstructures;


import mapdisplayutil.LLPos;
import mapdisplayutil.PathList;

public class Floor {
    private final PathList pathList;
    private final String FloorID, BuildingID;
    private final LLPos center;
    private final double UndrawRadius;

    public Floor(String FloorIDg, String BuildingIDg, PathList plg){
        FloorID = FloorIDg;
        BuildingID = BuildingIDg;
        center = plg.getCenter();
        pathList = plg;
        UndrawRadius = 2 * plg.getMaxDiagonal();
    }

    private double getUndrawRadius(){
        return UndrawRadius;
    }


    public PathList getPathList(){
        return pathList;
    }

    public String getFloorID(){
        return FloorID;
    }

    public String getBuildingID(){
        return BuildingID;
    }
}