package camerarecord;

import android.util.Log;
import android.widget.TextView;

import org.opencv.core.Mat;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class FrameRecorder {


    LinkedList<Mat> matList = new LinkedList<Mat>();

    private Mat lastFrame;
    private int count = 0;

    public void FrameRecorder(){
    }

    public LinkedList<Mat> getMatList(){
        return matList;
    }


    public void addFrame(CameraViewActivity ctx){
        if(lastFrame != null) {
            matList.add(lastFrame.clone());
            count ++;
            updText(ctx);
        }
    }

    private void updText(CameraViewActivity ctx) {
        int textId = ctx.getResources().getIdentifier("textView", "id", ctx.getPackageName());

        TextView tv = ctx.findViewById(textId);

        tv.setText("Snapshots: " + count);
    }

    public void setLastFrame(Mat mat){
        lastFrame = mat;
    }
}
