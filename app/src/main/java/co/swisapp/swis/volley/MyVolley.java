package co.swisapp.swis.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Map;


public class MyVolley {

    CustomStringRequest stringRequest;
    OnSuccessListener onSuccessListener;
    OnFailureListener onFailureListener;

    public void insertRequest(Context context, String URL, Map<String, String> params){

        stringRequest = new CustomStringRequest(Request.Method.POST, URL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (onSuccessListener != null){
                    onSuccessListener.onSuccess(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (onFailureListener != null){
                    onFailureListener.onFail(error);
                }
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }



    public interface OnSuccessListener {
        void onSuccess(String response);
    }

    public interface OnFailureListener {
        void onFail(VolleyError error);
    }

    public void setOnSuccessListener(final OnSuccessListener onSuccessListener) {
        this.onSuccessListener=onSuccessListener;
    }

    public void setOnFailureListener(final OnFailureListener onFailureListener){
        this.onFailureListener=onFailureListener;
    }
}
