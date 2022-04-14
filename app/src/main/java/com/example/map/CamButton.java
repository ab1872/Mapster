package com.example.map;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class CamButton extends androidx.appcompat.widget.AppCompatImageButton {
    public CamButton(Context context, AttributeSet attrs){
        super(context, attrs);

        this.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("CamButton", "Cam button success");
            }
        });
    }
}
