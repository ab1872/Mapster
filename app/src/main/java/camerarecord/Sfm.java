package camerarecord;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Sfm {

    LinearLayout console, picout;
    TextView tv;
    Context ctx;



    /*

    Write mat debugger.

~ PART 1: matching features ~

List Of Mat ------> * Gey Key Points *
------> Match key points between one and the next.
Returns Array of: MatOfDMatch


~ PART 2: Find Fundamental and essential matrix ~
Between one and the next: Calib3d.findFundamentalMat


~ PART 3: SVD to get rotation and translation ~


~ PART 4: Check if the rotation is coherent ~

Function interface FindCameraMatrices: Ask for K and two images, returns P1 matrix and whether is valid.

     */

    public void init(Context ctxg, LinearLayout consoleg, LinearLayout picoutg){
        console = consoleg;
        ctx = ctxg;
        tv = new TextView(ctx);
        picout = picoutg;
        console.addView(tv);
    }

    // https://stackoverflow.com/questions/46153260/getting-error-while-using-findfundamentalmat-in-android-opencv-and-not-able-to-r

    public void startEngine(Mat K, LinkedList<Mat> frames){
        addMessage("Got ("+frames.size()+") frames as input.");
        // Debugging K...
        addMessage("Debugging K:");
       //addMessage(Utils.debugMatFloat(K));

        // extract matched features.
        // PnP reconstruct
        // refine

        //org.opencv.calib3d.Calib3d.findFundamentalMat(.
        //addMessage("Starting ORB feature detection.");

        addMessage("Finding fundamental matrix:");
        Mat F = fundamentalMatrix(frames.get(0), frames.get(1));
        addMessage("Found!");
        addMessage(F.dump());
        addMessage("Finding E:");

        //Mat E = K.t() F * K;
        Mat d = new Mat();
        Mat E = new Mat();
        //Code to multiply: gemm(matSrc1, matSrc2, 1, new Mat(), 0, matDest);
        Core.gemm(K.t(), F, 1, new Mat(), 0, d);
        Core.gemm(d, K, 1, new Mat(), 0, E);

        addMessage("Found essential matrix!");

        //Calib3d.findFundamentalMat(imgpts1, imgpts2, Calib3d.FM_RANSAC,3, 0.99);

    }

    //https://akshikawijesundara.medium.com/object-recognition-with-opencv-on-android-6435277ab285
    /* This function should match the features between 2 */

    private Mat fundamentalMatrix(Mat img1, Mat img2){

        addMessage("Getting key points");
        MatOfKeyPoint kp1 = new MatOfKeyPoint();
        MatOfKeyPoint kp2 = new MatOfKeyPoint();
        FeatureDetector fd = FeatureDetector.create(FeatureDetector.ORB);
        fd.detect(img1, kp1);
        fd.detect(img2, kp2);
        DescriptorExtractor descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);

        addMessage("Getting descriptors");
        Mat ds1 = new Mat();
        Mat ds2 = new Mat();
        descriptor.compute(img1, kp1, ds1);
        descriptor.compute(img2, kp2, ds2);

        addMessage("Doing the matching");
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

        MatOfDMatch matches = new MatOfDMatch();
        matcher.match(ds2, ds1, matches);

        addMessage("Done - converting to array.");

        DMatch[] dma = matches.toArray();
        KeyPoint[] kp1a = kp1.toArray();
        KeyPoint[] kp2a = kp2.toArray();

        int sz = dma.length;

        Point[] lp1 = new Point[sz];
        Point[] lp2 = new Point[sz];

        for(int i=0; i<dma.length; i++){
            lp1[i] = kp1a[dma[i].trainIdx].pt;
            lp2[i] = kp2a[dma[i].queryIdx].pt;
        }
        Mat F = Calib3d.findFundamentalMat(new MatOfPoint2f(lp1), new MatOfPoint2f(lp2), Calib3d.FM_RANSAC, 3, 0.99);
        return F;
    }

    public MatOfKeyPoint detectFeatures(Mat img){
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY);

        DescriptorExtractor descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
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
