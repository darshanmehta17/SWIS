package co.swisapp.swis;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainApplication  extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

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
}
