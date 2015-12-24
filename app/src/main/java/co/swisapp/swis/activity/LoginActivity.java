package co.swisapp.swis.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.swisapp.swis.R;
import co.swisapp.swis.utility.InputValidator;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.input_login_username)
    EditText etUserName;

    @Bind(R.id.input_login_password)
    EditText etUserPassword;

    @Bind(R.id.btn_login)
    Button btLogin;

    @OnClick(R.id.link_signup)
    public void LaunchSignUp() {
        //TODO : Link Sign Up
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btLogin.setEnabled(true);
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

        btLogin.setEnabled(false);

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
        btLogin.setEnabled(true);
        finish();
    }

}
