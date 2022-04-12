package com.example.map;

public class ScreenPos {
    /*
    LL-.-> X()
   |-> Y()
   |-> ToScreen(cam)

     */

    private int X, Y;

    public ScreenPos(int Xg, int Yg) {
        X = Xg;
        Y = Yg;
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
