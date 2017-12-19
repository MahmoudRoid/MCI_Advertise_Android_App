package ir.mahmoud.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.R;

public class VideoDetailActivity extends AppCompatActivity {

    PostModel myModel;
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getModel();
        Glide.with(this).load(myModel.imageUrl).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher)).into(imageView);

    }

    public void getModel() {
        Intent i = getIntent();
        myModel = (PostModel) i.getSerializableExtra("feedItem");
    }

    @OnClick(R.id.videoRelative)
    public void onViewClicked() {
    }
}
