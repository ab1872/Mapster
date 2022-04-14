package floorstructures;


import mapdisplayutil.LLPos;
import mapdisplayutil.PathList;

public class Floor {
    private final PathList pathList;
    private final long FloorID, BuildingID;
    private final LLPos center;
    private final double altitude;

    private double UndrawRadius;

    public Floor(long FloorIDg, long BuildingIDg, LLPos centerg, double altitudeg, PathList plg){

        FloorID = FloorIDg;
        BuildingID = BuildingIDg;
        center = centerg;
        altitude = altitudeg;
        // Start parsing...
        pathList = plg;
        UndrawRadius = 2 * plg.getMaxDiagonal();
    }

    private double GetUndrawRadius(){
        return UndrawRadius;
    }


    public PathList GetPathList(){
        return pathList;
    }

    public long GetFloorID(){
        return FloorID;
    }

    public long GetBuildingID(){
        return BuildingID;
    }
}