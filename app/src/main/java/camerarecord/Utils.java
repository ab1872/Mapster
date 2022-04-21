package camerarecord;

import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;

public class Utils {

    public static String debugMatFloat(Mat m){
        String s = "";
        int size = (int) m.total() * m.channels();
        float[] buf = new float[size];
        m.get(0,0,buf);

        int r = m.rows();
        int c = m.cols();
        s += m.rows()+"x"+m.cols() + ":\n";
        for(int i=0; i<r; i++){
            s += "[";
            for(int j=0; j<c; j++){
                s += String.format("%2.2f", buf[i*c + j]) + ", ";
            }
            s += "]\n";
        }
        return s;
    }

    public static String debugDump(Mat m){
        return m.dump();
    }
}
