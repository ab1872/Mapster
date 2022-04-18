package cameracalibration;

import static org.opencv.core.CvType.CV_32FC4;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.RequiresApi;

import org.opencv.core.Mat;

public class CameraCalibration {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getCameraMatrix(Activity activity){//int width, int height){

        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        String[] idList = new String[0];
        try {
            idList = manager.getCameraIdList();

        int maxCameraCnt = idList.length;

            for (int index = 0; index < maxCameraCnt; index++) {
                String cameraId = manager.getCameraIdList()[index];
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                float[] arr = characteristics.get(CameraCharacteristics.LENS_INTRINSIC_CALIBRATION);
                Log.i("Camera " + index, "OK " + arr);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        //Mat K = Mat.zeros(4,4, CV_32FC4);


        //return new Mat();
    }

    public static float mmToPx(final float mm, final Context context)
    {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return mm * TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, dm);
    }
}
