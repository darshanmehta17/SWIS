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
    public  String fileName ;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static public File getVideoFileInternal(Context context) {

        File SWIS_DIR = context.getDir("Cache ", Context.MODE_PRIVATE) ;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        return new File(SWIS_DIR.getPath() + File.separator + "VID_" + timeStamp + ".mp4") ;


    }



    static public File getVideoFileExternal(Context context) {
        File folder = new File(Environment.getExternalStorageDirectory() +  "/swisapp") ;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        //return new File(context.getExternalFilesDir(null), "video.mp4");
        if(!folder.exists()){
            folder.mkdirs() ;
        }
        return new File(folder.getPath() + File.separator + getVideoFileInternal(context).getAbsolutePath()) ;
        // TODO: GET THE EXISTING FILE NAME

    }


}
