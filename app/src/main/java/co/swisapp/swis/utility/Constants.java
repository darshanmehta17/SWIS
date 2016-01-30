package co.swisapp.swis.utility;

import android.os.Build;

public class Constants {


    public static final int VALID = 0;
    public static final int EMAIL_ERROR = 1;
    public static final int PASSWORD_ERROR = 2;
    public static final int USERNAME_ERROR = 3;

    public static final int API_LEVEL = Integer.valueOf(Build.VERSION.SDK_INT);

    public static final String SIGNUP_URL = "https://swisapp.co:55555/api/signup" ;

    public static final String USERNAME = "username" ;
    public static final String EMAIL = "email" ;
    public static final String PASSWORD = "password" ;
    public static final String TAG = "SWIS Logcat TAG " ;
}
