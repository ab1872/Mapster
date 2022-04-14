package mapdisplayutil;

public class LLPos {
    private  double longitude, latitude;

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

    public ScreenPos ToScreen(Cam camera){
        /*
        m_per_deg_lat = 111132.954 - 559.822 * cos( 2 * latMid ) + 1.175 * cos( 4 * latMid);
        m_per_deg_lon = 111132.954 * cos ( latMid );
        source: https://stackoverflow.com/questions/639695/how-to-convert-latitude-or-longitude-to-meters

        New Brunswick: 46.5653° N, 66.4619° W

        Giving: m_per_deg_lat = 111132.954 - 559.822 * cos( 2 * 46.5653 deg) + 1.175 * cos( 4 *46.5653 deg)
        = 111162 m
        m_per_deg_long = 111132.954 * cos ( 46.5653 deg ) = 76407 m


        At normal zoom level, we set arbitrarily. 50m = 6 inches screen.
        1 dp = 0.15875E-3 m.

        50 m = 6 * 0.0254 m screen
        50 m = 6 * 0.0254 / 0.15875E-3  dp screen
        = 960 dp screen.
        1 m world = 19.2 dp screen.
         */

        LLPos LLgot = camera.GetLL();
        double longitudeD = longitude - LLgot.longitude;
        double latitudeD = latitude - LLgot.latitude;

        double screenX = (longitudeD * 76407 * 19.2); // dp
        double screenY = (latitudeD * 111162  * 19.2); // dp

        ScreenPos ScreenPosNew = new ScreenPos(screenX, screenY);
        return ScreenPosNew;
    }

}
