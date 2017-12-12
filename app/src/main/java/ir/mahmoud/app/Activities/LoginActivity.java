package ir.mahmoud.app.Activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ir.mahmoud.app.Application;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Interfaces.ApiClient;
import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class LoginActivity extends AppCompatActivity {
    public static EditText editTextMobile, editTextCode;
    public TextView txt_timer;
    FloatingActionButton fab;
    private TextInputLayout input_layout_code;
    private ProgressBar progressBar;
    private Map<String, String> params = new HashMap<>();
    private Button bt_go;

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
        String mobile = editTextMobile.getText().toString();
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
                        editTextMobile.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        view.setEnabled(false);
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
                HSH.showtoast(LoginActivity.this, "لطفا دقایقی بعد مجددا تلاش کنید");
                progressBar.setVisibility(View.GONE);
                v.setEnabled(true);
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
