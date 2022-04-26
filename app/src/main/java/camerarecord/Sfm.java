package camerarecord;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.Feature;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
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
import java.util.Vector;

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

    /*
1  2  3  4  5
P->P->P->P         Features
   P->P->P->P


1) Make a view -> feature look up.
2) For each view, get earliest common feature among the features. Exclude those that start now.
Alternate between 3) and 4):
3) Triangulate -> Do it wrt. The earliest common feature's camera position.
4)
     */

    // https://stackoverflow.com/questions/46153260/getting-error-while-using-findfundamentalmat-in-android-opencv-and-not-able-to-r

    private static void mult(Mat a, Mat b, Mat r){
        Core.gemm(a, b, 1, new Mat(), 0, r);
    }


    public void startEngine(Mat K, LinkedList<Mat> frames){
        addMessage("Got ("+frames.size()+") frames as input.");

        addMessage("Finding and matching features.");
        foundFeatures ff = findFeatures(frames, K);

        addMessage("Found essential matrix: ");
        addMessage(ff.E.dump());

        /* get SVD of Essential Matrix */
        // https://answers.opencv.org/question/30824/decomposition-of-essential-matrix-leads-to-wrong-rotation-and-translation/

        Mat w = new Mat(3,3, CvType.CV_64F);
        Mat u = new Mat();
        Mat vt = new Mat();

        Core.SVDecomp(ff.E, w, u, vt, Core.DECOMP_SVD);

        /* ToDo: exhaust 4 possibilities */
        //  u * W * vt is the first one

        w.put(0,0,new double[]{0,-1,0,1,0,0,0,0,1});

        Mat d = new Mat();
        Mat R = new Mat();
        mult(u, w, d);
        mult(d, vt, R);

        Mat t = u.col(2);


        Mat P1 = new Mat(3,4,CvType.CV_64F);
        /*
            Concatenate to make P1. (test if hconcat works)

            P1 is the base case for the PnP / triangulation iteration, the second frame's camera.

           P1( R(0,0),R(0,1), R(0,2), t(0),
                R(1,0),R(1,1), R(1,2), t(1),
                R(2,0),R(2,1), R(2,2), t(2));
        */

        ArrayList<Mat> d2 = new ArrayList<>();
        d2.add(R);
        d2.add(t);
        Core.hconcat(d2, P1);

        /* Make a view-> feature lookup, with earliest common view */
        // i: img index. v: feature is present table.
        ArrayList<Hashtable<feature, Integer>> VFlookup = new ArrayList<>();
        /* i: img index, v: view*/
        ArrayList<Integer> eCF = new ArrayList<>();

        for(int i = 0; i < frames.size(); i++){
            /* featList: list of features which will be used for triangulation */
            /* and their key point's respective location */
            Hashtable<feature, Integer> featList = new Hashtable<>();
            int min = -1;
            for(feature feat : ff){
                int firstFrameOfFeature = feat.imgIndexStart;
                /* If the feature is on the screen currently */
                /* And if the feature is not currently starting */
                if(i > firstFrameOfFeature && i < feat.consecutiveMatches.size() + feat.imgIndexStart){
                    if(0<min || min>firstFrameOfFeature){
                        min = firstFrameOfFeature;
                    }
                    featList.put(feat, i - firstFrameOfFeature);
                }
            }
            /* Add view->feature lookup. */
            VFlookup.add(featList);
            /* Add the earliest common frame among features*/
            eCF.add(min);
        }

        /* Initialize a camera position list */
        /* New positions are gotten with solvePnP */
        /* First position is the unity matrix, because the scene is referenced from the first frame */


        Mat P0 = new Mat(3, 4,CvType.CV_64F);
        P0.put(0,0,new double[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0
        });


        /* Second position comes from previously (P1) */

        ArrayList<Mat> cameraPositionList = new ArrayList<>();
        cameraPositionList.add(P0);
        cameraPositionList.add(P1);

        /* Now, we alternately triangulate and solvePnP to build the scene. */

        /* Initially, we triangulate on the (second frame) */
        /* Then, from the third frame on, we 1) PnP to get cam. pos., 2) triangulate */
        /* This invites the creation of a method, which we call triangulate wrapper */

        /*

        TRIANGULATE
        - inputs: current frame's feature list.
        - modifies: sets a feature's 3d point.
        algorithm: turn feature list into PointArray. (BuildPointArray),
            call the actual Triangulate.


            1) Get 2d points from feature list.
            2) Call Triangulate.

        PNP (called before triangulate)
        - make (3d point, current feature point) array
        - filter frame's features that have a 3d point


         */

    }

    //https://akshikawijesundara.medium.com/object-recognition-with-opencv-on-android-6435277ab285

    static class feature {
        Mat worldPoint;
        private boolean isSetWorldPoint;

        int imgIndexStart;
        LinkedList<Integer> consecutiveMatches;
        int lastMatch;

        public boolean isSetWorldPoint(){
            return isSetWorldPoint;
        }

        public void setWorldPoint(Mat wp){
            isSetWorldPoint = true;
            worldPoint = wp;
        }
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

                mult(K.t(), featureList.F, d);
                mult(d, K, E);
                featureList.E = E;
            }
        }
        featureList.keyPointsPerImage = kps;
        featureList.descPerImage = dss;

        return featureList;
    }

    Mat LinearLSTTriangulation(Point3 u, Mat P, Point3 u1, Mat P1){
        double[] p = new double[12];
        P.get(0,0, p);

        double[] p1 = new double[12];
        P1.get(0,0,p1);

        Mat A = new Mat(4, 3,CvType.CV_64F);
        A.put(0,0,new double[]{
                u.x*p[2*4 + 0]-p[0], u.x * p[2*4 + 1] - p[0*4 + 1], u.x * p[2*4 + 2] - p[0*4 + 2],
                u.y*p[2*4 + 0]-p[1*4 + 0],u.y*p[2*4 + 1]-p[1*4 + 1],u.y*p[2*4 + 2]-p[1*4 + 2],
                u1.x*p1[2*4 + 0]-p1[0*4 + 0],u1.x*p1[2*4 + 1]-p1[0*4 + 1],u1.x*p1[2*4 + 2]-p1[0*4 + 2],
                u1.y*p1[2*4 + 0]-p1[1*4 + 0], u1.y*p1[2*4 + 1]-p1[1*4 + 1],u1.y*p1[2*4 + 2]-p1[1*4 + 2]
        });

        Mat B = new Mat(3, 1, CvType.CV_64F);
        B.put(0,0, new double[]{
                -(u.x * p[2*4 + 3] - p[0*4+3]),
                -(u.y * p[2*4+3] - p[1*4+3]),
                -(u1.x *p1[2*4+3] - p1[0*4+3]),
                -(u1.y *p1[2*4+3] - p1[1*4+3])
        });

        /* From textbook:
        //build B vector
        Matx41d B(-(u.x*P(2,3)-P(0,3)),
                -(u.y*P(2,3)-P(1,3)),
                -(u1.x*P1(2,3)-P1(0,3)),
                -(u1.y*P1(2,3)-P1(1,3)));
        */
        /*
         Matx43d A(u.x*p.get(2,0)-p(0,0),u.x*p(2,1)-p(0,1),u.x*p(2,2)-p(0,2),
u.y*p(2,0)-p(1,0),u.y*p(2,1)-p(1,1),u.y*p(2,2)-p(1,2),
u1.x*p1(2,0)-p1(0,0), u1.x*p1(2,1)-p1(0,1),u1.x*P1(2,2)-P1(0,2),
u1.y*P1(2,0)-P1(1,0), u1.y*P1(2,1)-P1(1,1),u1.y*P1(2,2)-P1(1,2)
 );


         */
        Mat X = new Mat();
        Core.solve(A, B, X, Core.DECOMP_SVD);

        /* ToDo: Check Valid Rotation as they do in the textbook */
        return X;
    }

    double Triangulate(List<KeyPoint> pt_set1, List<KeyPoint> pt_set2, Mat K, Mat Kinv, Mat P, Mat P1, foundFeatures ff){

        double rep_error_sum = 0;
        for(int i=0; i < pt_set1.size(); i++){
            Point kp = pt_set1.get(i).pt;
            Point3 u = new Point3(kp.x,kp.y,1.0);
            Mat umat = new Mat(3, 1, CvType.CV_64F);
            umat.put(0,0, new double[]{kp.x,kp.y,1.0});

            /* um = Kinv u*/
            Mat um = new Mat();
            mult(Kinv, umat, um);
            double[] uarr = new double[3];
            um.get(0,0,uarr);

            Point kp1 = pt_set2.get(i).pt;

            Point3 u1 = new Point3(kp1.x,kp1.y,1.0);
            Mat u1mat = new Mat(3,1, CvType.CV_64F);
            u1mat.put(0,0, new double[]{kp1.x,kp1.y,1.0});


            Mat um1 = new Mat();
            mult(Kinv, u1mat, um1);
            double[] u1arr = new double[3];
            um1.get(0,0,u1arr);

            Mat X = LinearLSTTriangulation(
                    new Point3(uarr[0],uarr[1],uarr[2]), P,
                    new Point3(u1arr[0],u1arr[1],u1arr[2]), P1
            );
            /* calculate the reprojection error */

            Mat xPt_img = new Mat();
            Mat d = new Mat();
            mult(K, P1, d);
            mult(d, X, xPt_img);
            double [] xPt_imgarr = xPt_img.get(0,0);

            xPt_imgarr[0] /= xPt_imgarr[2];
            xPt_imgarr[1] /= xPt_imgarr[2];

            xPt_img.put(0,0,xPt_imgarr);
            rep_error_sum += (Core.norm(xPt_img));

            /* Add point to cloud */

            /* AKA: put X in its feature associated with "i" */
        }
        /* Return the mean reprojection error */
        return rep_error_sum / pt_set1.size();
    }

    /*
    double TriangulatePoints(
const vector<KeyPoint>& pt_set1,
const vector<KeyPoint>& pt_set2,
const Mat&Kinv,
const Matx34d& P,
const Matx34d& P1,
vector<Point3d>& pointcloud)
{
vector<double> reproj_error;
for (unsigned int i=0; i<pts_size; i++) {
 //convert to normalized homogeneous coordinates
 Point2f kp = pt_set1[i].pt;
 Point3d u(kp.x,kp.y,1.0);
 Mat_<double> um = Kinv * Mat_<double>(u);
 u = um.at<Point3d>(0);
 Point2f kp1 = pt_set2[i].pt;
 Point3d u1(kp1.x,kp1.y,1.0);
 Mat_<double> um1 = Kinv * Mat_<double>(u1);
 u1 = um1.at<Point3d>(0);

 //triangulate
 Mat_<double> X = LinearLSTriangulation(u,P,u1,P1);

 //calculate reprojection error
 Mat_<double> xPt_img = K * Mat(P1) * X;
 Point2f xPt_img_(xPt_img(0)/xPt_img(2),xPt_img(1)/xPt_img(2));
 reproj_error.push_back(norm(xPt_img_-kp1));
 //store 3D point
 pointcloud.push_back(Point3d(X(0),X(1),X(2)));
}
//return mean reprojection error
Scalar me = mean(reproj_error);
return me[0];
}

     */







    public void addMessage(String message){
        tv.setText(tv.getText()+message+"\n");
    }

    public void addPicout(){

    }


}
