package com.example.map;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import gpsinterface.GPSInterface;

public class MainActivity extends AppCompatActivity {
    private GPSInterface gpsInterface;

    // Define the permission callback.
    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), got -> {
                boolean fail = false;
                for (String key : got.keySet()) {

                    if(got.get(key) == false){
                        fail = true;
                    }
                    System.out.println("Permission--" + key + ":" + got.get(key));
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context ctx = getApplicationContext();


        /* ACCESS PERMISSION FOR LOCATION */
        if (ContextCompat.checkSelfPermission(ctx
                , Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.i("Test","Has already been granted.");

        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            Log.i("Test","Has not been granted.");
            requestPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
            /* also will need to request camera permission */
        }

        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ctx);

        if(resultCode != ConnectionResult.SUCCESS) {
            Log.i("google play","Not available");
        }
        else{
            gpsInterface = new GPSInterface(this);
        }
        // Check if a button clicked, set Content View etc here.
    }

    Handler handler = new Handler(); /* HANDLER for GPS position updates */
    Runnable runnable;
    int delay = 0;
    @Override
    protected void onResume() {
        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                gpsInterface.LookUpMyPosition();
                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }
    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }
}

