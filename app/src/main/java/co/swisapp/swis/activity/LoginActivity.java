package co.swisapp.swis.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.swisapp.swis.R;
import co.swisapp.swis.utility.InputValidator;

public class LoginActivity extends AppCompatActivity {

    EditText etUserName;

    EditText etUserPassword;

    Button bLogin;

    TextView tvSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName = (EditText) findViewById(R.id.login_input_username);
        etUserPassword = (EditText) findViewById(R.id.login_input_password);
        bLogin = (Button) findViewById(R.id.login_button);
        tvSignUp = (TextView) findViewById(R.id.login_link_signup);

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

    //Login Validation

    public boolean validate() {

        boolean valid = true;
        String username = etUserName.getText().toString();
        String password = etUserPassword.getText().toString();

        if (InputValidator.validate_username(username)) {
            etUserName.setError(null);
        } else {
            etUserName.setError("Enter Valid User Name");
            valid = false;
        }

        if (InputValidator.vaildate_pass(password)) {
            etUserPassword.setError(null);
        } else {
            etUserPassword.setError("Enter Valid Password");
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
