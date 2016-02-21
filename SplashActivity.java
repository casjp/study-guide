package com.bucs.bupc.art.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bucs.bupc.art.R;

/**
 * Created by John Paul Cas
 *
 * connect <i>John Paul Cas</i> on facebook
 * <br />
 * facebook.com/cassie.next <br />
 * facebook.com/simplePlan.cas
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timer = new Thread() {

            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        };

        timer.start();
    }
}
