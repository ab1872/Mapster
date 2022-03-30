package com.example.map;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img = findViewById(R.id.map_view);
        img.setImageResource(R.drawable.ic_hallview);
        img.setContentDescription(getResources().getString(R.string.app_name));

        MapPicture map = new MapPicture(5);
        img.setImageDrawable(map);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    OutdoorMapData mapdata = new OutdoorMapData();
                    mapdata.GetData(51.249,7.148,51.2495,7.1485);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
}

