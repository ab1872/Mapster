package mapdisplayutil;

import android.util.Log;

public class PosMapping {
    private LLPos LLP; /* Absolute until updated */
    /* Screen Pos is relative */
    private double altitude;
    private ScreenParameters screenPara;

    public PosMapping(LLPos LLPg, double altitude, ScreenParameters screenParag) {
        screenPara = screenParag;
        update(LLPg);
    }

    public PosMapping(ScreenPos ScreenPg, double altitude, ScreenParameters screenParag){
        screenPara = screenParag;
        update(ScreenPg);
    }

    /* Infer the LL from a screen location */
    public void update(ScreenPos ScreenPg){
        LLPos cameraLL = screenPara.getCameraLL();

        double longitudeD = ScreenPg.GetX() / (76407 * 19.2);
        double latitudeD = ScreenPg.GetY() / (111162  * 19.2);

        LLP = new LLPos(longitudeD + cameraLL.GetLongitude(), latitudeD + cameraLL.GetLatitude());
    }

    /* Update LL pos with same camera*/
    public void update(LLPos LLPg){
        LLP = LLPg;
    }

    public void updateCamera(ScreenParameters screenParag){ /* camera */
        screenPara = screenParag;
    }

    /* Get projected Screen pos, without camera offset */
    public ScreenPos getScreenP(){
        LLPos cameraLL = screenPara.getCameraLL();
        double longitudeD = LLP.GetLongitude() - cameraLL.GetLongitude();
        double latitudeD = LLP.GetLatitude() - cameraLL.GetLatitude();

        double screenX = (longitudeD * 76407 * 19.2); // dp
        double screenY = (latitudeD * 111162  * 19.2); // dp

        ScreenPos ScreenP = new ScreenPos(screenX, screenY);
        return ScreenP;
    }

    public LLPos getLLP(){
        return LLP;
    }

    public int getLeft(){
        ScreenPos ScreenP = getScreenP();
        return (int)(screenPara.getCameraWidth()/2 + ScreenP.GetX());
    }

    public int getTop(){
        ScreenPos ScreenP = getScreenP();
        return (int)(screenPara.getCameraHeight()/2 - ScreenP.GetY());
    }

    @Override
    public String toString(){
        return LLP.GetLatitude() + ", " + LLP.GetLongitude();
    }

    public static PosMapping fromString(String s, double altitude, ScreenParameters screenParag){
        String[] spl = s.split(", ");
        return new PosMapping(new LLPos(Double.valueOf(spl[0]), Double.valueOf(spl[1])), altitude, screenParag);
    }

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


}
