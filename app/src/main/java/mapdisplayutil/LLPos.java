package mapdisplayutil;

public class LLPos {
    private  double longitude, latitude;

    @Override
    public String toString(){
        return "Lon: " + longitude + " Lat: " + latitude;
    }

    public LLPos(double latitudeg, double longitudeg){
        longitude = longitudeg;
        latitude = latitudeg;
    }

    public double GetLongitude() {
        return longitude;
    }

    public double GetLatitude(){
        return latitude;
    }
}
