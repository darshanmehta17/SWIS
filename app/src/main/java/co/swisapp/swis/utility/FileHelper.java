package co.swisapp.swis.utility;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class FileHelper {

    public static int TYPE_INTERNAL = 0x03;
    public static int TYPE_EXTERNAL = 0x07;

    public static String generateVideoFileName(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        return "VID_" + timeStamp + ".mp4";
    }

    public static File createVideoFile(Context context, String filename, int fileType){
        if(fileType == 0x03){

            File folder = context.getDir("Videos", Context.MODE_PRIVATE) ;
            return new File(folder.getPath() + File.separator + filename) ;

        }
        else if(fileType == 0x07){
            File folder = new File(Environment.getExternalStorageDirectory() +  "/swisapp") ;
            if(!folder.exists()){
                folder.mkdirs() ;
            }
            return new File(folder.getPath() + File.separator + filename) ;
        }

        return new File("void") ;
    }
}
