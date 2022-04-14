package mapdisplayutil;

public class ScreenParameters {


    private LLPos cameraLL;
    private double cameraWidth;
    private double cameraHeight;
    private double cameraAltitude;

    public ScreenParameters(LLPos LL, double width, double height, double altitude){
        cameraLL = LL;
        cameraWidth = width;
        cameraHeight = height;
        cameraAltitude = altitude;
    }

    public void updateWidthHeight(double width, double height){
        cameraWidth = width;
        cameraHeight = height;
    }

    public void updateAltitude(double altitude){
        cameraAltitude = altitude;
    }

    public void updateLL(LLPos llpg){
        cameraLL = llpg;
    }

    public LLPos getCameraLL(){
        return cameraLL;
    }

    public double getCameraAltitude(){
        return cameraAltitude;
    }

    public double getCameraWidth(){
        return cameraWidth;
    }

    public double getCameraHeight(){
        return cameraHeight;
    }

}
