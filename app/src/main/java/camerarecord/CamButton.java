package camerarecord;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CamButton extends androidx.appcompat.widget.AppCompatImageButton {
    public CamButton(Context context, AttributeSet attrs){
        super(context, attrs);


        this.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("CamButton", "Cam button success");
                Intent intent = new Intent(context, CameraViewActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
