package co.swisapp.swis.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.swisapp.swis.R;
import co.swisapp.swis.utility.Constants;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{


    public String USERCHECK_URL ;

    private EditText etUserName;
    private EditText etEmail;
    private EditText etPassword;
    private Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /*Declaration of all the UI elements with onClickListener*/
        etUserName = (EditText) findViewById(R.id.username) ;
        etEmail = (EditText) findViewById(R.id.email_id) ;
        etPassword = (EditText) findViewById(R.id.password) ;
        bRegister = (Button) findViewById(R.id.register) ;

        bRegister.setOnClickListener(this);
        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!usernameCheck()){
                           //Update UI to modify username
                        Toast.makeText(getApplicationContext(), R.string.username_taken, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    /*Extraction of data as inserted by the user from UI */
    final String username = etUserName.getText().toString().trim() ;
    final String email = etEmail.getText().toString().trim() ;
    final String password = etPassword.getText().toString().trim() ;



    /*Function to handle the entire registration*/
    public void registration() throws JSONException {



        /*Setting parameters to POST call*/
        HashMap<String, String> params = new HashMap<>() ;
        params.put(Constants.USERNAME, username) ;
        params.put(Constants.EMAIL, email) ;
        params.put(Constants.PASSWORD, password) ;

        /*Making a jsonObject request and handling response */
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Constants.SIGNUP_URL, new JSONObject(params), new Response.Listener<JSONObject>() {
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

        USERCHECK_URL = String.format("https://swisapp.co:55555/api/useravailable?username=%s", username);

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
        if(v == bRegister) {
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
