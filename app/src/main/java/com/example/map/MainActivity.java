package com.example.map;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.map.R;

import java.util.LinkedList;

import explore.Cam;
import floorstructures.Floor;
import worldscreenpositions.LLPos;

public class MainActivity extends AppCompatActivity {


    Cam camera;
    LinkedList<Floor> DownloadedFloors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = new Cam(new LLPos(66.4619, 46.5653));

        //img = findViewById(R.id.map_view);
        //img.setContentDescription(getResources().getString(R.string.app_name));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    //server.AskForBuildings(51.249,7.148,51.2495,7.1485);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
}

