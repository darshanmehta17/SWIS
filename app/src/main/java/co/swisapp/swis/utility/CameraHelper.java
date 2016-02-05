package co.swisapp.swis.utility;


import android.content.Context;
import android.os.Environment;
import android.util.SparseIntArray;
import android.view.Surface;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class CameraHelper {

    public static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    public String timeStamp ;


    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
}
