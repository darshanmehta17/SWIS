package co.swisapp.swis.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.swisapp.swis.R;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String SIGNUP_URL = "https://swisapp.co:55555/api/signup" ;
    public String USERCHECK_URL ;
    public static final String USERNAME = "username" ;
    public static final String EMAIL = "email" ;
    public static final String PASSWORD = "password" ;

    private EditText ETusername ;
    private EditText ETemail ;
    private EditText ETpassword ;
    private Button Bregister ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /*Declaration of all the UI elements with onClickListener*/
        ETusername = (EditText) findViewById(R.id.username) ;
        ETemail = (EditText) findViewById(R.id.email_id) ;
        ETpassword = (EditText) findViewById(R.id.password) ;
        Bregister = (Button) findViewById(R.id.register) ;

        Bregister.setOnClickListener(this);
        ETusername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!usernameCheck()){
                           //Update UI to modify username
                    }
                }
            }
        });

    }

    /*Function to handle the entire registration*/
    public void registration() throws JSONException {

        /*Extraction of data as inserted by the user from UI */
        final String username = ETusername.getText().toString().trim() ;
        final String email = ETemail.getText().toString().trim() ;
        final String password = ETpassword.getText().toString().trim() ;

        /*Setting parameters to POST call*/
        HashMap<String, String> params = new HashMap<>() ;
        params.put(USERNAME, username) ;
        params.put(EMAIL, email) ;
        params.put(PASSWORD, password) ;

        /*Making a jsonObject request and handling response */
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, SIGNUP_URL, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    String data = response.getString("response");

                    switch (data){
                        case "success" :
                            //Intent to confirmation
                            break;
                        case "invalid" :
                            // Please try again
                            checkErrors() ;
                            break;
                        case "emailtaken" :
                            Log.i("Inside Log", "Email taken response" + response.toString()) ;
                            Toast.makeText(getApplicationContext(), R.string.email_taken, Toast.LENGTH_LONG).show();

                            break;

                    }


                }catch (JSONException e){

                    Log.e("EXCEPTION", "JSON " + e) ;
                }


                if(!response.toString().equals(" ")){
                    Log.d("Registration response", "Response is" + response.toString()) ;

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Response Error", "Error: " + error) ;

            }
        });


        /*Queue request of the generated string */
        RequestQueue requestQueue = Volley.newRequestQueue(this) ;
        requestQueue.add(jsonRequest) ;


    }

    /* Method to handle errors returned by API */
    private void checkErrors() {



    }

    boolean value ;

    /*api call to username duplicates */
    private boolean usernameCheck(){

        USERCHECK_URL = String.format("https://swisapp.co:55555/api/useravailable?username=%s", USERNAME);

        JsonObjectRequest jsonRequest2 = new JsonObjectRequest(Request.Method.GET, USERCHECK_URL, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    value = response.getBoolean("available") ;
                }
                catch (JSONException e){
                    Log.e("JSON Error", "Error: " + e) ;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Response Error", "Error: " + error) ;

            }
        });

        /*Queue request of the generated string */
        RequestQueue requestQueue = Volley.newRequestQueue(this) ;
        requestQueue.add(jsonRequest2) ;



        return  value ;
    }


    /*method of the class view.OnClickListener that is implemented*/
    @Override
    public void onClick(View v) {
        if(v == Bregister) {
            try {
                boolean check = usernameCheck() ;
                if(check)
                    registration();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
