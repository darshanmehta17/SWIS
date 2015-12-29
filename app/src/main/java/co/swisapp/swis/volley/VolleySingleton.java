package co.swisapp.swis.volley;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {


    private SharedPreferences preferences;

    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private VolleySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
 
    public static VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }
    
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
    
    public void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }

    /**
     * Checks the response headers for session cookie and saves it
     * if it finds it.
     * @param headers Response Headers.
     */
//    public final void checkSessionCookie(Map<String, String> headers) {
//        if (headers.containsKey(Constants.KEY_SET_COOKIE)
//                && headers.get(Constants.KEY_SET_COOKIE).startsWith(Constants.KEY_SESSIONID)) {
//            String cookie = headers.get(Constants.KEY_SET_COOKIE);
//            if (cookie.length() > 0) {
//                String[] splitCookie = cookie.split(";");
//                String[] splitSessionId = splitCookie[0].split("=");
//                cookie = splitSessionId[1];
//                SharedPreferences.Editor prefEditor = preferences.edit();
//                prefEditor.putString(Constants.KEY_SESSIONID, cookie);
//                prefEditor.commit();
//            }
//        }
//    }

    /**
     * Adds session cookie to headers if exists.
     * @param headers
     */
//    public final void addSessionCookie(Map<String, String> headers) {
//        String sessionId = preferences.getString(Constants.KEY_SESSIONID, "");
//        if (sessionId.length() > 0) {
//            StringBuilder builder = new StringBuilder();
//            builder.append(Constants.KEY_SESSIONID);
//            builder.append("=");
//            builder.append(sessionId);
//            if (headers.containsKey(Constants.KEY_COOKIE)) {
//                builder.append("; ");
//                builder.append(headers.get(Constants.KEY_COOKIE));
//            }
//            headers.put(Constants.KEY_COOKIE, builder.toString());
//        }
//    }

}
