package ir.mahmoud.app.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

import ir.mahmoud.app.Classes.BaseActivity;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Classes.PermissionHandler;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class LoginActivity extends BaseActivity {

    String[] permissions = {Manifest.permission.ACCESS_NETWORK_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView txt_guest = (TextView) findViewById(R.id.txt_guest);
        txt_guest.setPaintFlags(txt_guest.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PermissionHandler().checkPermission(LoginActivity.this, permissions, new PermissionHandler.OnPermissionResponse() {
                    @Override
                    public void onPermissionGranted() {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onPermissionDenied() {
                        HSH.showtoast(LoginActivity.this, "برای ورود به برنامه دسترسی را صادر نمایید.");
                    }
                });

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED, null);
        finish();
    }

    public void clickLogin(final View view) {
        String mobile = ""/*editTextMobile.getText().toString()*/;
        if (mobile.equals("") || !mobile.startsWith("09") || mobile.length() != 11)
            HSH.showtoast(LoginActivity.this, "لطفا شماره موبایل معتبر وارد نمایید");
        else {
            final String PN = ((EditText) findViewById(R.id.et_mobile)).getText().toString().trim();

            final Dialog dialog = new Dialog(LoginActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_submit_phone);
            dialog.setCancelable(false);
            HSH.dialog(dialog);
            TextView txt_title = (TextView) dialog.findViewById(R.id.text);
            txt_title.setText("\n" + PN + "\n\n" + "شماره موبایل صحیح است؟" + "\n");
            TextView txt_yes = (TextView) dialog.findViewById(R.id.txt_yes);
            TextView txt_no = (TextView) dialog.findViewById(R.id.txt_no);
            txt_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtils.getConnectivity(LoginActivity.this)) {
                        dialog.dismiss();
                        //editTextMobile.setEnabled(false);
                        //progressBar.setVisibility(View.VISIBLE);
                        //view.setEnabled(false);
                        // params.put(getString(R.string.mobile), ((EditText) findViewById(R.id.et_mobile)).getText().toString().trim());
                        SendPhoneNumber(view);
                    } else HSH.showtoast(LoginActivity.this, getString(R.string.error_internet));
                }
            });
            txt_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            HSH.dialog(dialog);
            dialog.show();
        }
    }

    private void SendPhoneNumber(final View v) {
        /*Call<ResponseBody> call =
                ApiClient.getClient().create(ApiInterface.class).inesrtUser(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });*/
    }

    private void CheckPhoneNumber(final View v, final Map<String, String> params) {
        /*Call<ResponseBody> call =
                ApiClient.getClient().create(ApiInterface.class).checkPhoneNumber(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.code() == 200)
                    try {
                        JSONObject result = null;
                        String body = response.body().string().toString().trim();
                        result = new JSONObject(body);

                    } catch (Exception e) {
                    }
                else {

                }
                v.setEnabled(true);
                editTextMobile.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                v.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                HSH.showtoast(LoginActivity.this, "خطا در برقراری ارتباط با سرور");
            }
        });*/
    }
}
