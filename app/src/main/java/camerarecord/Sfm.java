package camerarecord;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

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
        addMessage("Got focal length of " + K.get(0,0) + " px " + " as input.");

        /* TODO: import K properly */
        //org.opencv.calib3d.Calib3d.findFundamentalMat(.
        addMessage("Starting ORB feature detection.");
        MatOfKeyPoint mkp = detectFeatures(frames.get(0));
        addMessage("Got keypoints: " + mkp.toArray() + "");

    }
    //https://akshikawijesundara.medium.com/object-recognition-with-opencv-on-android-6435277ab285
    public MatOfKeyPoint detectFeatures(Mat img){
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY);

        DescriptorExtractor descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

        Mat dss = new Mat();
        MatOfKeyPoint kpt = new MatOfKeyPoint();
        FeatureDetector fd = FeatureDetector.create(FeatureDetector.ORB);

        fd.detect(img, kpt);
        descriptor.compute(img, kpt, dss);

        return kpt;
    }


    public void addMessage(String message){
        tv.setText(tv.getText()+message+"\n");
    }

    public void addPicout(){

    }


}
