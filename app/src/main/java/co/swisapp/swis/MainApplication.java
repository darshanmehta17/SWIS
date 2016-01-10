package co.swisapp.swis;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainApplication  extends Application{

    private RequestQueue requestQueue ;
    private static MainApplication mainApplication ;
    public static final String TAG = MainApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();

        mainApplication = this ;
        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;

        /**
         * Calligraphy configuration initialise.
         * Uncomment the line to set the default font to Roboto-Regular.
         */
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );


        printHashKey();

    }

    public static synchronized MainApplication getInstance(){
        return mainApplication ;
    }

    public RequestQueue getRequestQueue(){
        return requestQueue ;
    }

    public <T> void add(Request<T> request)     {
        request.setTag(TAG) ;
    }

    public void cancel(){
        requestQueue.cancelAll(TAG);
    }


    /*Printing the Hashkey to uniquely Identify Developer and App for FacebookAPI*/
    public void printHashKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "co.swisapp.swis",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

}
