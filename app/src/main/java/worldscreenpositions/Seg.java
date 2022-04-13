package worldscreenpositions;

import android.graphics.Canvas;
import android.graphics.Paint;

import explore.Cam;

public class Seg {

    private LLPos a;
    private LLPos b;
    private Cam camera;

    public Seg(LLPos ag, LLPos bg, Cam camerag) {
        a = ag;
        b = bg;
        camera = camerag;
    }

    public LLPos GetA(){
        return a;
    }

    public LLPos GetB(){
        return b;
    }

    public void Plot(Canvas canvas, Paint paint){
        ScreenPos as = a.ToScreen(camera);
        ScreenPos bs = b.ToScreen(camera);

        int aleft = as.GetLeft(camera);
        int atop = as.GetTop(camera);

        int bleft = bs.GetLeft(camera);
        int btop = bs.GetTop(camera);

        canvas.drawLine(aleft, atop, bleft , btop, paint);
    }
}
