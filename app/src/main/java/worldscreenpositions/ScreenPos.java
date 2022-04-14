package worldscreenpositions;

import explore.Cam;

public class ScreenPos {
    /*
    LL-.-> X()
   |-> Y()
   |-> ToScreen(cam)

     */

    private double X, Y;

    public ScreenPos(double Xg, double Yg) {
        X = Xg;
        Y = Yg;
    }

    public double GetX (){
        return X;
    }

    public double GetY(){
        return Y;
    }

    public int GetTop(Cam camera){
        return (int)(camera.getHalfWidth() - Y);
    }

    public int GetLeft(Cam camera){
        return (int)(camera.getHalfWidth() + X);
    }

    public LLPos ToLL(Cam camera){
        // cf. coefficients in LLPos.

        ScreenPos Screengot = camera.GetScreenP();

        double longitudeD = Screengot.X / (76407 * 19.2);
        double latitudeD = Screengot.Y / (111162  * 19.2);

        LLPos LLgot = camera.GetLL();

        LLPos LLPosNew = new LLPos(longitudeD + LLgot.GetLongitude(), latitudeD + LLgot.GetLatitude());
        return LLPosNew;
    }
}
