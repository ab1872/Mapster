package com.example.map;

public class Cam {
    private LLPos LLP;
    private ScreenPos ScreenP;
    private ScreenPos Velocity;

    private int ZoomLevel; // optional

    private void InitVelocity(){
        Velocity = new ScreenPos(0,0);
    }

    public void SetPos(LLPos LLPg){
        LLP = LLPg;
        ScreenP = LLPg.ToScreen(this);
    }

    public void SetPos(ScreenPos ScreenPg){
        ScreenP = ScreenPg;
        LLP = ScreenPg.ToLL(this);
    }

    public void SetVel(ScreenPos Vel){
        Velocity = Vel;
    }

    public LLPos GetLL(){
        return LLP;
    }

    public ScreenPos GetScreenP(){
        return ScreenP;
    }

    public Cam(LLPos LLPg){
        SetPos(LLPg);
        InitVelocity();
    }
}
