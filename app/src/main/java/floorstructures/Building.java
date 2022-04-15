package floorstructures;

public class Building {
    private final String buildingID;
    private final FloorList floorList;

    public Building(String buildingIDg, FloorList floorListg){
        buildingID = buildingIDg;
        floorList = floorListg;
    }

    public String getBuildingID() {
        return buildingID;
    }

    public FloorList getFloorList() {
        return floorList;
    }
}
