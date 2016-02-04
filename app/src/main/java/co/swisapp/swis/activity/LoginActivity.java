package co.swisapp.swis.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import co.swisapp.swis.R;
import co.swisapp.swis.customview.RecordButton;

public class LoginActivity extends AppCompatActivity implements RecordButton.OnStopRecordListener, RecordButton.OnStartRecordListener {

    private static final String TAG = "LoginActivity";

    RecordButton recordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        recordButton = (RecordButton) findViewById(R.id.login_record_button);

        recordButton.setOnStartRecordListener(this);
        recordButton.setOnStopRecordListener(this);
    }

    @Override
    public void onStopRecord() {
        Log.d(TAG, "onStopRecord: ");
    }

    @Override
    public void onStartRecord() {
        Log.d(TAG, "onStartRecord() called with: " + "");
    }
}
