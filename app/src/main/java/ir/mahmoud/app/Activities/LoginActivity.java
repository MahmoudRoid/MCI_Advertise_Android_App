package ir.mahmoud.app.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.R;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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
                    if (NetworkUtils.getConnectivity(LoginActivity.this) == true) {
                        dialog.dismiss();
                        //editTextMobile.setEnabled(false);
                        //progressBar.setVisibility(View.VISIBLE);
                        //view.setEnabled(false);
                        // params.put(getString(R.string.mobile), ((EditText) findViewById(R.id.et_mobile)).getText().toString().trim());
                        SendPhoneNumber(view);
                    } else
                        HSH.showtoast(LoginActivity.this, "خطا در اتصال به اینترنت");
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
