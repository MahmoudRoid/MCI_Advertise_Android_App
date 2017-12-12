package ir.mahmoud.app.Activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                HSH.onOpenPage(SplashActivity.this, MainActivity.class);
                finish();
            }
        }, 3000);
    }
}
