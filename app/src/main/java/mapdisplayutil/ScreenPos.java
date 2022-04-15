package mapdisplayutil;

public class ScreenPos {
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

    @Override
    public String toString(){
        return GetX() + " " + GetY() + " (from center)";
    }

}
