package co.swisapp.swis.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.swisapp.swis.MainApplication;
import co.swisapp.swis.R;
import co.swisapp.swis.utility.Constants;
import co.swisapp.swis.utility.NetworkUtils;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    public String USERCHECK_URL;
    private EditText etUserName;
    private EditText etEmail;
    private EditText etPassword;



    private Button bRegister;
    boolean value;
    MainApplication helper = MainApplication.getInstance() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


       /* LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

        */



        /*Declaration of all the UI elements with onClickListener/onFocusChangeListener*/
                etUserName = (EditText) findViewById(R.id.username);
        etEmail = (EditText) findViewById(R.id.email_id);
        etPassword = (EditText) findViewById(R.id.password);

        bRegister = (Button) findViewById(R.id.register);
        bRegister.setOnClickListener(this);

        //API call as soon as Username added or changed to check uniqueness

        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
                        if (!usernameCheck()) {
                            //Update UI to modify username
                            etUserName.setBackgroundResource(R.drawable.signup_error_ui);
                            Toast.makeText(getApplicationContext(), R.string.username_taken, Toast.LENGTH_SHORT).show();
                        } else {
                            etUserName.setBackgroundResource(R.drawable.signup_ok_ui);

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!getEmailfromUI().contains("@")) {
                        etEmail.setBackgroundResource(R.drawable.signup_error_ui);
                        Toast.makeText(getApplicationContext(), R.string.invalid_Email, Toast.LENGTH_SHORT).show();
                    } else
                        etEmail.setBackgroundResource(R.drawable.signup_ok_ui);

                }
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (getPasswordfromUI().length() < 6) {
                        Toast.makeText(getApplicationContext(), R.string.password_short, Toast.LENGTH_SHORT).show();
                        etPassword.setBackgroundResource(R.drawable.signup_error_ui);
                    } else if (getPasswordfromUI().contains(getUsernamefromUI())) {
                        Toast.makeText(getApplicationContext(), R.string.password_contains_username, Toast.LENGTH_SHORT).show();
                        etPassword.setBackgroundResource(R.drawable.signup_error_ui);
                    } else
                        etPassword.setBackgroundResource(R.drawable.signup_ok_ui);
                }
            }
        });

    }




    /*method of view.OnClickListener that is implemented*/
    @Override
    public void onClick(View v) {
        if (v == bRegister) {
            if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
                if (validityCheck()) {
                    try {
                        if (usernameCheck())
                            registration();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), R.string.error_common, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_LONG).show();
            }

        }
    }


    /*Function to handle the entire registration*/
    public void registration() throws JSONException {



        /*Setting parameters to POST call*/
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.USERNAME, getUsernamefromUI());
        params.put(Constants.EMAIL, getEmailfromUI());
        params.put(Constants.PASSWORD, getPasswordfromUI());

        /*Making a jsonObject request and handling response */
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Constants.SIGNUP_URL, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String data = response.getString("response");

                    switch (data) {
                        case "success":
                            //Intent to confirmation
                            break;
                        case "invalid":
                            // Please try again
                            HandleError();
                            break;
                        case "emailtaken":
                            Log.i("Inside Log", "Email taken response" + response.toString());
                            Toast.makeText(getApplicationContext(), R.string.email_taken, Toast.LENGTH_LONG).show();

                            break;

                    }


                } catch (JSONException e) {

                    Log.e("EXCEPTION", "JSON " + e);
                    Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_LONG).show();
                }


                if (!response.toString().equals(" ")) {
                    Log.d("Registration response", "Response is" + response.toString());

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Response Error", "Error: " + error);
                Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_LONG).show();
            }
        });


        helper.add(jsonRequest);

        /*Queue request of the generated string */
        /*RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonRequest);*/

    }

    private void HandleError() {
        //Handle "error" response
    }

    /* Method to handle errors returned by API */


    /*Extraction of data as inserted by the user from UI */
    public String getUsernamefromUI() {
        return etUserName.getText().toString().trim();
    }

    public String getEmailfromUI() {
        return etEmail.getText().toString().trim();
    }

    public String getPasswordfromUI() {
        return etPassword.getText().toString().trim();
    }

    /*api call to username duplicates */
    private boolean usernameCheck() {

        USERCHECK_URL = String.format("https://swisapp.co:55555/api/useravailable?username=%s", getUsernamefromUI());

        JsonObjectRequest jsonRequest2 = new JsonObjectRequest(Request.Method.GET, USERCHECK_URL, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    value = response.getBoolean("available");
                } catch (JSONException e) {
                    Log.e("JSON Error", "Error: " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Response Error", "Error: " + error);
                value = false;

            }
        });


        helper.add(jsonRequest2);
        /*Queue request of the generated string */
        /*RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonRequest2);*/

        return value;
    }

    private boolean validityCheck() {

        String Email = getEmailfromUI();

        if (!Email.contains("@") || getPasswordfromUI().length() < 6 || getPasswordfromUI().contains(getUsernamefromUI())) {
            return false;
        } else
            return true;
    }
}
