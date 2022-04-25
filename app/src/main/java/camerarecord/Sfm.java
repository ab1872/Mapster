package camerarecord;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.Feature;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.ORB;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Hashtable;
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
        addMessage("Finding and matching features.");
        foundFeatures ff = findFeatures(frames, K);

        addMessage("Found fundamental matrix: ");
        addMessage(ff.F.dump());
    }

    //https://akshikawijesundara.medium.com/object-recognition-with-opencv-on-android-6435277ab285

    static class feature {
        Point3 worldPoint;
        int imgIndexStart;
        LinkedList<Integer> consecutiveMatches;
        int lastMatch;
    }

    static class foundFeatures extends ArrayList<feature> {
        ArrayList<MatOfKeyPoint> keyPointsPerImage;
        ArrayList<Mat> descPerImage;
        Mat F;
        Mat E;
    }

    private foundFeatures findFeatures(List<Mat> imgs, Mat K){
        ArrayList<MatOfKeyPoint> kps = new ArrayList<>();

        ORB fd = ORB.create();
        fd.detect(imgs,kps);
        ArrayList<Mat> dss = new ArrayList<>();
        fd.compute(imgs, kps, dss);

        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

        foundFeatures featureList = new foundFeatures();
        ArrayList<feature> activeFeatureList = new ArrayList<>();

        /* For each frame */
        for (int i = 0; i < dss.size()-1; i++) {
            MatOfDMatch matches = new MatOfDMatch();
            matcher.match(dss.get(i), dss.get(i+1), matches);
            DMatch[] matchArray = matches.toArray();

            Hashtable<Integer, Boolean> inUse = new Hashtable<>();

            /* For each match in that frame and the next */
            for(int j = 0; j < matchArray.length; j++){
                DMatch match = matchArray[j];
                int whichKeyPointBefore = match.queryIdx;
                int whichKeyPointNow = match.trainIdx;
                /* If this next point is not already part of a feature */
                if(!inUse.containsKey(whichKeyPointNow)){
                    /* Create a new feature */
                    feature newFeat = new feature();
                    newFeat.consecutiveMatches = new LinkedList<>();
                    newFeat.imgIndexStart = i;
                    newFeat.consecutiveMatches.add(whichKeyPointBefore);
                    newFeat.lastMatch = whichKeyPointBefore;
                    activeFeatureList.add(newFeat);
                    featureList.add(newFeat);
                };

                /* Maintain active features */

                ArrayList<feature> newActiveFeatureList = new ArrayList<>();

                for(feature feat : activeFeatureList){
                    if(feat.lastMatch == whichKeyPointBefore){
                        feat.consecutiveMatches.add(match.trainIdx);
                        feat.lastMatch = whichKeyPointNow;
                        newActiveFeatureList.add(feat);
                    }
                }

                activeFeatureList = newActiveFeatureList;
            }

            /* Find the fundamental and essential matrices */

            if(i == 0){

                /* Retrieve keypoint lists from first two frames */

                KeyPoint[] kp1a = kps.get(0).toArray();
                KeyPoint[] kp2a = kps.get(1).toArray();

                /* Convert each keypoint list to a point list */

                int sz = featureList.size();
                Point[] lp1 = new Point[sz];
                Point[] lp2 = new Point[sz];

                for(int k = 0 ; k < sz ; k++){
                    feature feat = featureList.get(k);
                    lp1[k] = kp1a[feat.consecutiveMatches.get(0)].pt;
                    lp2[k] = kp2a[feat.consecutiveMatches.get(1)].pt;
                }

                /* Now find fundamental matrix */

                featureList.F = Calib3d.findFundamentalMat(new MatOfPoint2f(lp1), new MatOfPoint2f(lp2), Calib3d.FM_RANSAC, 3, 0.99);
                //Mat E = K.t() F * K;
                Mat d = new Mat();
                Mat E = new Mat();
                //Code to multiply: gemm(matSrc1, matSrc2, 1, new Mat(), 0, matDest);
                Core.gemm(K.t(), featureList.F, 1, new Mat(), 0, d);
                Core.gemm(d, K, 1, new Mat(), 0, E);
                featureList.E = E;
            }
        }
        featureList.keyPointsPerImage = kps;
        featureList.descPerImage = dss;

        return featureList;
    }






    public void addMessage(String message){
        tv.setText(tv.getText()+message+"\n");
    }

    public void addPicout(){

    }


}
