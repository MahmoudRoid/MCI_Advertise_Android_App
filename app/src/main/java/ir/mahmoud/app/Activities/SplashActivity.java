package ir.mahmoud.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.orm.query.Select;

import ir.mahmoud.app.Asynktask.GetMemberStatus;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Interfaces.IWebServiceByTag;
import ir.mahmoud.app.Models.tbl_User;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity implements IWebServiceByTag   {

    public final String STATUS_TAG = "status";
    String phoneText = "";
    boolean isFirstTimeToUseApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        isFirstTimeToUseApp = prefs.getBoolean("isFirstTimeToUseApp", true);

        try {
            tbl_User tbl = Select.from(tbl_User.class).first();
            if(tbl != null)
                phoneText = tbl.getNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(phoneText.length()>0){
            if (NetworkUtils.getConnectivity(this)) {
                // call sub method
                GetMemberStatus get = new GetMemberStatus(SplashActivity.this, SplashActivity.this,
                        phoneText, STATUS_TAG);
                get.getData();
            } else {
                HSH.showtoast(this, getString(R.string.error_internet));
            }
        }
        else {
            // bere too safheye ba'ad
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    goLoginActivity();
                }
            }, 3000);
        }

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void getResult(final Object result, String Tag) throws Exception {
        final String myResult = result.toString();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (myResult.toString().equals("\"ON\"")) {
                    // login ro rad bokone
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();

                } else {
                    // bere too safheye login
                   goLoginActivity();
                }
            }
        }, 3000);
    }

    @Override
    public void getError(String ErrorCodeTitle, String Tag) throws Exception {
        HSH.showtoast(SplashActivity.this,"متاسفانه مشکلی پیش آمده است");
        try {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            System.exit(0);
            finish();
        } catch (Exception e) {
        }
    }

   public void goLoginActivity(){

        if(isFirstTimeToUseApp){
            // baraye inke dg to safheye intro nare
            SharedPreferences.Editor editor = getSharedPreferences("Pref", MODE_PRIVATE).edit();
            editor.putBoolean("isFirstTimeToUseApp",false);
            editor.apply();

            // bere safheye intro
            startActivity(new Intent(SplashActivity.this, IntroActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
        else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }


    }
}
