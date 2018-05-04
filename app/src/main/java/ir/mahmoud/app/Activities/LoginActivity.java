package ir.mahmoud.app.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ir.mahmoud.app.Asynktask.CheckNewVersion;
import ir.mahmoud.app.BuildConfig;
import com.orm.query.Select;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.mahmoud.app.Adapters.SlideShowAdapter;
import ir.mahmoud.app.Asynktask.CheckMemberSub;
import ir.mahmoud.app.Asynktask.GetMemberStatus;
import ir.mahmoud.app.Asynktask.PostMemberSub;
import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.BaseActivity;
import ir.mahmoud.app.Classes.DownloadAppService;
import ir.mahmoud.app.Classes.DownloadSevice;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Classes.PermissionHandler;
import ir.mahmoud.app.Interfaces.IWebServiceByTag;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.Models.tbl_User;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class LoginActivity extends BaseActivity implements IWebServiceByTag {

    String[] permissions = {Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS};

    BroadcastReceiver myReceiver;

    @BindView(R.id.et_mobile)
    EditText etMobile;
    Snackbar snackbar;
    private RadioGroup.LayoutParams rprms;
    private PagerAdapter pagerAdapter;

    public final String STATUS_TAG = "status";
    public final String SUB_TAG = "sub";
    public final String CHECK_TAG = "check";


    @BindView(R.id.parentLayout)
    RelativeLayout parentLayout;

    String tid;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_login)
    Button etLogin;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    public void init(){
        getPermission();
        TextView txt_guest = (TextView) findViewById(R.id.txt_guest);
        snackbar = Snackbar.make(parentLayout, "لطفا منتظر بمانید", Snackbar.LENGTH_INDEFINITE);
        ViewCompat.setLayoutDirection(snackbar.getView(), ViewCompat.LAYOUT_DIRECTION_RTL);
        txt_guest.setPaintFlags(txt_guest.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PermissionHandler().checkPermission(LoginActivity.this, permissions, new PermissionHandler.OnPermissionResponse() {
                    @Override
                    public void onPermissionGranted() {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onPermissionDenied() {
                        HSH.showtoast(LoginActivity.this, "برای ورود به برنامه دسترسی را صادر نمایید.");
                    }
                });

            }
        });

        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String verificationCode = intent.getStringExtra("code");
                etCode.setText(verificationCode);
            }
        };


        prepareSlider();

        // vared kardane shomare dar edit text
        try {
            tbl_User tbl = Select.from(tbl_User.class).first();
            if (tbl != null) {
                if (tbl.getNumber() != null) etMobile.setText(tbl.getNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPermission() {

        new PermissionHandler().checkPermission(LoginActivity.this, permissions, new PermissionHandler.OnPermissionResponse() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied() {

            }
        });
    }

    public void prepareSlider(){
        // related to slider
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                pager.setCurrentItem(checkedId);
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                radioGroup.check(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = (int) (width / 2.85);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        pager.setLayoutParams(params);

        bindImges();
    }

    private void bindImges() {
        pager.removeAllViews();
        radioGroup.removeAllViews();

        int[] myImageList = new int[]{R.drawable.first_login_slider_image, R.drawable.second_login_slider_image, R.drawable.third_login_slider_image};

        if (radioGroup.getChildCount() == 0)
            for (int i = 0; i < 3; i++) {
                try {
                    final RadioButton rd = new RadioButton(this);
                    rd.setButtonDrawable(R.drawable.rdbtnselector);
                    rd.setPadding(0, 0, 5, 5);
                    rd.setId(i);
                    rprms = new RadioGroup.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    radioGroup.addView(rd, rprms);

                    pagerAdapter = null;
                    pagerAdapter = new SlideShowAdapter(getSupportFragmentManager(),myImageList);
                    pager.setAdapter(pagerAdapter);


                } catch (Exception e) {
                }
            }
        radioGroup.check(0);

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

    @OnClick(R.id.et_login)
    public void onViewClicked() {
        if (etLogin.getText().equals("ارسال کد")) {
            final String codeText = etCode.getText().toString();
            if (codeText.equals("")) {
                etCode.setError("لطفا کد ارسالی را وارد نمایید");
            } else {
                if (NetworkUtils.getConnectivity(this)) {
                    snackbar.show();
                    // call status API
                    CheckMemberSub get = new CheckMemberSub(LoginActivity.this, LoginActivity.this,
                            tid, codeText, CHECK_TAG);
                    get.getData();
                } else HSH.showtoast(this, getString(R.string.error_internet));
            }
        }
        // ******************************** az inja shoroo mishavad ***************************************************
        else {
            final String phoneText = etMobile.getText().toString();
            if (phoneText.equals("") || !phoneText.startsWith("09") || phoneText.length() != 11) {
                etMobile.setError("لطفا شماره موبایل معتبر وارد نمایید");
            } else {
                if (NetworkUtils.getConnectivity(this)) {
                    snackbar.show();
                    // call status API
                    GetMemberStatus get = new GetMemberStatus(LoginActivity.this, LoginActivity.this,
                            phoneText, STATUS_TAG);
                    get.getData();
                } else HSH.showtoast(this, getString(R.string.error_internet));

            }
        }
    }

    @Override
    public void getResult(Object result, String Tag) throws Exception {
        switch (Tag) {
            case STATUS_TAG:
                if (result.toString().equals("\"ON\"")) {
                    snackbar.dismiss();
                    saveUserNumber(etMobile.getText().toString());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                } else {
                    if (NetworkUtils.getConnectivity(this)) {
                        // call sub method
                        PostMemberSub post = new PostMemberSub(LoginActivity.this, LoginActivity.this
                                , etMobile.getText().toString().trim(), SUB_TAG);
                        post.getData();
                    } else {
                        HSH.showtoast(this, getString(R.string.error_internet));
                        snackbar.dismiss();
                    }

                }
                break;
            case SUB_TAG:
                snackbar.dismiss();
                tid = result.toString().substring(9, result.toString().length() - 1);
                // hide et_mobile
                etMobile.setVisibility(View.INVISIBLE);
                // show et_code
                etCode.setVisibility(View.VISIBLE);
                // change button text
                etLogin.setText("ارسال کد");
                HSH.showtoast(this, "کد پیامک شده را در فیلد بالا وارد نمایید");
                break;
            case CHECK_TAG:
                // go to main activity
                saveUserNumber(etMobile.getText().toString());
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;


        }
    }

    private void saveUserNumber(String phoneText) {
        // delete database row
        try {
            tbl_User.deleteAll(tbl_User.class);
            tbl_User tbl = new tbl_User(phoneText);
            tbl.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getError(String ErrorCodeTitle, String Tag) throws Exception {

        snackbar.dismiss();
        // show snack = dobare talash konid .......
        Snackbar message = Snackbar.make(parentLayout, "عملیات ناموفق بود! مجددا تلاش نمایید", Snackbar.LENGTH_LONG);
        ViewCompat.setLayoutDirection(message.getView(), ViewCompat.LAYOUT_DIRECTION_RTL);
        message.show();

        if (Tag.equals(CHECK_TAG)) {
            etCode.setVisibility(View.INVISIBLE);
            etMobile.setVisibility(View.VISIBLE);
            etLogin.setText("ورود");
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,new IntentFilter("BroadCast.Sms.Code"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    }
}
