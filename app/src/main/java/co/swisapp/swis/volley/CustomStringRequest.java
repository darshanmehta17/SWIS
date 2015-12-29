package co.swisapp.swis.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class CustomStringRequest extends Request<String> {

    private final Map<String, String> params;
    private Response.Listener<String> listener;


    public CustomStringRequest(int method, String url, Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.params = params;
        this.listener = listener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        Set<Map.Entry<String, String>> headers = response.headers.entrySet();
//        VolleySingleton.getInstance(MainApplication.getInstance()).checkSessionCookie(response.headers);
        try {
            parsed= "Headers: \n";

            for (Map.Entry<String, String> entry : headers) {
                parsed = parsed + "\n" + entry.getKey() + ":" + entry.getValue();
            }
            parsed = parsed + "\n\n\nData: \n" + new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        if (listener != null){
            listener.onResponse(response);
        }
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        listener = null;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

//        VolleySingleton.getInstance(MainApplication.getInstance()).addSessionCookie(headers);

        return headers;
    }
}
