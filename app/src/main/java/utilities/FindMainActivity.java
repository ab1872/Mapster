package utilities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import com.example.map.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;

abstract public class FindMainActivity {

    static public MainActivity Find(Context context){
        if (context instanceof Activity) {
            return (MainActivity)context;
        }

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (MainActivity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}
