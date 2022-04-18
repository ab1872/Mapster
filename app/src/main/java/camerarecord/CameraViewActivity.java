package camerarecord;

import static org.opencv.videoio.Videoio.CAP_ANY;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.map.R;
import com.google.gson.Gson;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfFloat4;
import org.opencv.core.MatOfInt;
import org.opencv.engine.OpenCVEngineInterface;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.File;

import cameracalibration.CameraCalibration;

public class CameraViewActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private CameraBridgeViewBase mOpenCvCameraView;
    private CameraCalibration cc;
    private FrameRecorder fr;
    private Sfm sfm;
    private float focal;
    public MatOfFloat K;

    int mWidth, mHeight;

    private boolean doneRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        /* Declaration stuff */
        fr = new FrameRecorder();
        cc = new CameraCalibration();
        sfm = new Sfm();
        focal = cc.getFocalLength(this);

        /* View stuff */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.camerarecord);
       // Intent intent = getIntent();


        /* OpenCV stuff */
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);


        /* Snapshot button stuff */
        CameraViewActivity me = this;

        findViewById(R.id.recordbutton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fr.addFrame(me);
            }
        });

        findViewById(R.id.donebutton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                doneRecording = true;
                onPause();
                mOpenCvCameraView.setVisibility(SurfaceView.GONE);
                findViewById(R.id.recordbutton).setVisibility(SurfaceView.GONE);
                findViewById(R.id.donebutton).setVisibility(SurfaceView.GONE);
                findViewById(R.id.textView).setVisibility(SurfaceView.GONE);
                LinearLayout console = findViewById(R.id.console);
                LinearLayout picout = findViewById(R.id.picout);
                findViewById(R.id.scrollview1).setVisibility(SurfaceView.VISIBLE);

                findViewById(R.id.scrollview2).setVisibility(SurfaceView.VISIBLE);
                sfm.init(me, console, picout);
                sfm.startEngine(K,fr.getMatList());
            }
        });

        /* Go to  */
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            printOut( "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            printOut( "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
    public void printOut(String msg){
        Log.i("OCV", msg);
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        fr.setLastFrame(rgba);
        return inputFrame.rgba();
    }

    public void onCameraViewStarted(int width, int height) {
        if (mWidth != width || mHeight != height) {
            mWidth = width;
            mHeight = height;

            K = new MatOfFloat();
            K.fromArray(new float[]{focal,0,width/2,
                    0,focal,height/2,
                    0,0    ,1});

        }
    }

    public void onCameraViewStopped() {
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    printOut( "OpenCV loaded successfully, now taking a video");
                    mOpenCvCameraView.enableView();
                } break;
                case LoaderCallbackInterface.INIT_FAILED:
                    Log.i("OCV","Init Failed");
                    break;
                case LoaderCallbackInterface.INSTALL_CANCELED:
                    Log.i("OCV","Install Cancelled");
                    break;
                case LoaderCallbackInterface.INCOMPATIBLE_MANAGER_VERSION:
                    Log.i("OCV","Incompatible Version");
                    break;
                case LoaderCallbackInterface.MARKET_ERROR:
                    Log.i("OCV","Market Error");
                    break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onStart(){
        super.onStart();
        Log.i("Camera","Has started the camera");
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.i("Camera","Has stopped the camera");
    }

    @Override
    public void onBackPressed(){
        Log.i("Camera","Has pressed back");
        finish();
    }
}
