package co.swisapp.swis.utility;


import android.content.Context;
import android.os.Environment;
import android.util.SparseIntArray;
import android.view.Surface;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraHelper {

    public static final SparseIntArray ORIENTATIONS = new SparseIntArray();


    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }



    CameraHelper(){


    }

    static public File getVideoFile(Context context) {
        File folder = new File(Environment.getExternalStorageDirectory() +  "/swisapp") ;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        //return new File(context.getExternalFilesDir(null), "video.mp4");
        if(!folder.exists()){
            folder.mkdirs() ;
        }
        return new File(folder.getPath() + File.separator + "VID_" + timeStamp + ".mp4") ;


    }


}
