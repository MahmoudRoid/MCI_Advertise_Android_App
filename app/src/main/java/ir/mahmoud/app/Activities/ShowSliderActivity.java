package ir.mahmoud.app.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.mahmoud.app.R;

public class ShowSliderActivity extends AppCompatActivity {

    String postUrl, postTitle;

    @BindView(R.id.postTitle)
    TextView title;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.pb)
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_slider);
        ButterKnife.bind(this);
        //HSH.showtoast(this,"لطفا تا بارگذاری صفحه، شکیبا باشید");

        try {
            postTitle = getIntent().getExtras().getString("postTitle");
            postUrl = getIntent().getExtras().getString("postUrl");
            title.setText(postTitle);

            webView.setWebViewClient(new MyWebViewClient());
            webView.loadUrl(postUrl);
        } catch (Exception e) {

        }
    }




    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            pb.setVisibility(View.GONE);
        }
    }

}



