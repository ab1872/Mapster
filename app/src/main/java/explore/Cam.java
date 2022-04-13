package explore;

import worldscreenpositions.LLPos;
import worldscreenpositions.ScreenPos;

public class Cam {
    private LLPos LLP;
    private ScreenPos ScreenP;
    //private ScreenPos Velocity;
    private double width, height;

    public void InformScreenDim(double widthg, double heightg){
        width = widthg;
        height = heightg;
    }

    public double getWidth(){
        return width;
    }

    public double getHeight(){
        return height;
    }

    public double getHalfWidth(){
        return width/2;
    }

    public double getHalfHeight(){
        return height/2;
    }


    private int ZoomLevel; // optional

    //public void InitVelocity(){
    //    Velocity = new ScreenPos(0,0);
   // }

    public void SetPos(LLPos LLPg){
        LLP = LLPg;
        ScreenP = LLPg.ToScreen(this);
    }

    public void SetPos(ScreenPos ScreenPg){
        ScreenP = ScreenPg;
        LLP = ScreenPg.ToLL(this);
    }

    //public void SetVel(ScreenPos Vel){
     //   Velocity = Vel;
   // }

    public LLPos GetLL(){
        return LLP;
    }

    public ScreenPos GetScreenP(){
        return ScreenP;
    }

    public Cam(LLPos LLPg){
        SetPos(LLPg);
        //InitVelocity();
    }
}
