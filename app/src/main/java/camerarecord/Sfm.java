package camerarecord;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;

import java.util.LinkedList;

public class Sfm {

    LinearLayout console, picout;
    TextView tv;
    Context ctx;

    public void init(Context ctxg, LinearLayout consoleg, LinearLayout picoutg){
        console = consoleg;
        ctx = ctxg;
        tv = new TextView(ctx);
        picout = picoutg;
        console.addView(tv);
    }

    public void startEngine(MatOfFloat K, LinkedList<Mat> frames){
        addMessage("Got ("+frames.size()+") frames as input.");
        addMessage("Got focal length of " + K.get(0,0) + " px " + "as input.");


        /* TODO: import K properly */


        /* Find fundamental and essential matrices*/

        // Mat F = org.opencv.calib3d.Calib3d.findFundamentalMat(...);
        // Mat E = K.t() * F * K;


    }

    public void addMessage(String message){
        tv.setText(tv.getText()+message+"\n");
    }

    public void addPicout(){

    }


}
