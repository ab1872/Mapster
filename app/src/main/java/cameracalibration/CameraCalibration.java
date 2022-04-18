package cameracalibration;

import static org.opencv.core.CvType.CV_32FC4;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.opencv.core.Mat;
import org.opencv.core.Size;

import camerarecord.CameraViewActivity;

public class CameraCalibration {

    Camera camera;
    Camera.Parameters cparam;

    public float getFocalLength(CameraViewActivity activity){//}, int width, int height){
        //Mat K = Mat.eye(4, 4, CV_32FC4);


        //camera = Camera.open();
        //cparam = camera.getParameters();
        float f = 28;//mmToPx(cparam.getFocalLength(), activity);
        //activity.printOut("Focal length: " + cparam.getFocalLength() + "mm, " + f + "px.");
        /*K.put(0,0, f);
        K.put(1,1,f);
        K.put(0,2, width/2);
        K.put(0,3, height/2);*/
        //camera.release();

        return mmToPx(f, activity);
    }

    public static float mmToPx(final float mm, final Context context)
    {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return mm * TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, dm);
    }
}
