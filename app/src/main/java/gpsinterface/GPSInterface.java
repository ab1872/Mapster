package gpsinterface;

import android.content.Context;
import android.location.Location;

import com.example.map.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import utilities.FindMainActivity;
import mapdisplayutil.LLPos;

public class GPSInterface {


    private final MainActivity mainac;
    private FusedLocationProviderClient fusedLocationClient;

    private Location loc = null;

    public GPSInterface(Context ctx){
        mainac = FindMainActivity.Find(ctx);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx);
    }

    public void LookUpMyPosition(){
        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            loc = location;
                        }
                    }
            );
        }
        catch(SecurityException Sec){

        }
    }

    public LLPos GetLL() {
        if (loc == null) {
            return new LLPos(0,0);
        } else {
            return new LLPos(loc.getLongitude(), loc.getLatitude());

        }
    }
    public double GetAltitude(){
        if(loc == null){
            return 0;
        }
        else {
            return loc.getAltitude();
        }
    }
}
