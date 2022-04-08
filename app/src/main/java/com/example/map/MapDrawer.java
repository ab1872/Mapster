package com.example.map;

import android.widget.ImageView;

import java.util.Vector;

public class MapDrawer {

    ImageView img;
    Vector<DownloadedBuilding> buildings;

    int longitude, latitude, altitude;

    public MapDrawer(ImageView im, Vector<DownloadedBuilding> bu){
        img = im;
        buildings = bu;
    }

    // **Method** to convert longitude,latitude to pixel offset.
    Vector<Integer> ConvertLongLatToPixelOffset(Vector<Double> LongLat){
        return new Vector<Integer>();
    }

    public void UpdateView(){
        // **UpdateView** For all DownloadedBuildings, parse their XML:


        //img.setImageDrawable(Drawable.createFromXml(getResources(), xmlpullparse));
    }
    // **UpdateView** For all DownloadedBuildings, parse their XML:
    // check if long,lat in range if not mark for deletion.
    // add long,lat to offset, convert to map,
    // Add to a bigger XML file
    // Save XML as resource
    // img.setContentDescription(getResources().getString(R.string.app_name));
}
