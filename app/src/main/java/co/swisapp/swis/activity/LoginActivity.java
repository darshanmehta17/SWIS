package co.swisapp.swis.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import co.swisapp.swis.R;
import co.swisapp.swis.customview.MorphDemo;

public class LoginActivity extends AppCompatActivity {

    MorphDemo morph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        morph = (MorphDemo) findViewById(R.id.morph);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (int i = 20; i < 150; i++) {

                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                morph.setRadius(finalI);
                            }
                        });

                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int i = 150; i >= 20; i--) {

                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                morph.setRadius(finalI);
                            }
                        });

                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
