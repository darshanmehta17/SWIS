package co.swisapp.swis.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.swisapp.swis.R;
import co.swisapp.swis.utility.InputValidator;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    private Button bLogin;

    private TextView tvSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    private void initialize() {
        etUsername = (EditText) findViewById(R.id.login_input_username);
        etPassword = (EditText) findViewById(R.id.login_input_password);

        tvSignUp = (TextView) findViewById(R.id.login_link_signup);

        bLogin = (Button) findViewById(R.id.login_button_facebook);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        bLogin.setEnabled(true);
    }

    /**
     * Validates login related fields
     * @return a boolean {@code valid = true} if success, else {@code valid = false} if failure
     */
    public boolean validate() {

        boolean valid = true;
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (InputValidator.validate_username(username)) {
            etUsername.setError(null);
        } else {
            etUsername.setError("Enter Valid User Name");
            valid = false;
        }

        if (InputValidator.vaildate_pass(password)) {
            etPassword.setError(null);
        } else {
            etPassword.setError("Enter Valid Password");
            valid = false;
        }

        return valid;
    }

    public void startLogin() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        bLogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Verifying your details");
        progressDialog.show();

        // TODO : Implementing Networking

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                }, 2000);
    }

    public void onLoginSuccess() {
        bLogin.setEnabled(true);
        finish();
    }

}
