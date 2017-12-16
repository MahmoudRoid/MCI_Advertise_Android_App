package ir.mahmoud.app.Activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Interfaces.ApiClient;
import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class NewsDetailsActivity extends AppCompatActivity {

    //CommonFeedItem fFeed;
    WebView desc;
    ProgressBar p;
    CollapsingToolbarLayout i;

    String pish = "<html><head><style type=\"text/css\">" +
            "@font-face {font-family: MyFont;color: #000000;src: url(file:///android_asset/IRANSansMedium.ttf);}" +
            "body {color: #000000;font-family: MyFont;text-align: justify;width:100%;}" +
            "</style></head><body  dir=\"rtl\" style=\"margin:0px;line-height:40px;\">";
    String pas = "<br /><hr /></body></html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_news);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        ImageButton img_back = (ImageButton) findViewById(R.id.img_back);
        ImageButton btn_share = (ImageButton) findViewById(R.id.btn_share);
        img_back.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP));
        btn_share.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String shareBody = "http://emdadkeshavarz.com/content/" + fFeed.getTitle().replace(" ", "-").replace(".", "") + "/" + fFeed.getId() + "/";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "امداد کشاورز");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, null));*/
            }
        });

        p = (ProgressBar) findViewById(R.id.pb);

        //fFeed = (CommonFeedItem) getIntent().getExtras().getSerializable("feed");

        final AppBarLayout appBar = (AppBarLayout) findViewById(R.id.app_bar);
        float heightDp = (float) (getResources().getDisplayMetrics().heightPixels / 2.5);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
        lp.height = (int) heightDp;

        desc = (WebView) findViewById(R.id.webView);
        desc.setHorizontalScrollBarEnabled(false);
        desc.getSettings().setLoadWithOverviewMode(true);
        desc.getSettings().setPluginState(WebSettings.PluginState.ON);
        desc.getSettings().setJavaScriptEnabled(true);
        desc.setFocusableInTouchMode(true);
        desc.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        desc.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);

        //RetrieveNewsDetail();

        i = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        i.setTitle("");
        try {
            ImageView img = (ImageView) findViewById(R.id.image_header);
            //Application.imageLoader.displayImage(fFeed.getPhotoUrl(), img, Application.options);
        } catch (Exception e) {
        }

    }

    public void RetrieveNewsDetail() {
        Call<ResponseBody> call =
                ApiClient.getClient().create(ApiInterface.class).getNewsDetails("1"/*fFeed.getId()*/);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse
                    (Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.toString().equals("[]")) {

                        Toast.makeText(NewsDetailsActivity.this, "مجدد تلاش کنید /مشکل در دریافت اطلاعات", Toast.LENGTH_LONG).show();

                    } else {
                        try {
                            JSONArray result = new JSONArray(response.body().string());
                            for (int i = 0; i < result.length(); i++) {
                                try {
                                    desc.loadDataWithBaseURL("file:///android_asset/",
                                            pish + "<div style=\"color:#8fc800;size:50px;\">"
                                                    + ""/*fFeed.getTitle()*/
                                                    + "</div><br />"
                                                    + result.getJSONObject(0).getString("DetailsNews").replace("<img", "<img width=\"100%\"")
                                                    + pas, "text/html", "charset=utf-8", null);
                                } catch (Exception e) {
                                    HSH.showtoast(NewsDetailsActivity.this, "خطا در برقراری ارتباط با سرور");
                                }
                                p.setVisibility(View.GONE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
