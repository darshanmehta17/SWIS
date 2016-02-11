package co.swisapp.swis.utility;

import android.os.Build;

import co.swisapp.swis.BuildConfig;

public abstract class Constants {

    public static final String BASE_URL = "https://swis-vuln-1.c9users.io";

    public static final String USER_AGENT = "device-android-" + BuildConfig.VERSION_NAME;

    public static final String URL_API_PREFIX = BASE_URL + "/api";
    public static final String URL_VIDEO_UPLOAD = URL_API_PREFIX + "/upload";


    public static final int VALID = 0;
    public static final int EMAIL_ERROR = 1;
    public static final int PASSWORD_ERROR = 2;
    public static final int USERNAME_ERROR = 3;

    public static final int API_LEVEL = Build.VERSION.SDK_INT ;

    public static final String SIGNUP_URL = "https://swisapp.co:55555/api/signup" ;

    public static final String USERNAME = "username" ;
    public static final String EMAIL = "email" ;
    public static final String PASSWORD = "password" ;
    public static final String TAG = "SWIS Logcat TAG " ;

    public static final String  KEY_UPLOAD_VIDEO_PARAMETER = "video";

    public static final String TEXT_UPLOAD_NOTIFICATION_TITLE = "Uploading";

}
