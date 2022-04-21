package pointclouds;

import java.util.ArrayList;

import mapdisplayutil.PathList;

public class PointCloud2D {


    public PathList meshGeneration(){
        // Get max bounds.
        // Recurse on max bounds.
        // Fuse end points.
        return new PathList();
    }

    private void meshGenRecurse(/*bounds*/){
        // Divide in 4
        // Linear regression
        // Score > threshold?
        // Make Segment
    }
    // Apache simple linear regression Least Squares Method.
    // remake point cloud (extrapolate: Y/N)

    private void fuseEndpoints(){
        // If any two endpoints are close together, fuse them together.
        // Segments into a LinePath.
    }

    // Match with floor: YES/NO, offset in real pixels.
}
