package co.swisapp.swis.utility;


import android.util.SparseIntArray;
import android.view.Surface;

public class CameraHelper {

    private static boolean isRecording ;

    /**
     * SparseIntArray for referencing orientations with respect to their integer value.
     */
    public static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    public static boolean isRecording() {
        return isRecording;
    }

    public static void setIsRecording(boolean isRecording) {
        CameraHelper.isRecording = isRecording;
    }
}
