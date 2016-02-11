package co.swisapp.swis;

import android.support.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainApplication  extends MultiDexApplication{

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




}
