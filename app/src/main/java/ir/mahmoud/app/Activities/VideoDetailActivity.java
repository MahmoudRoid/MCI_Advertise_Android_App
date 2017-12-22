package ir.mahmoud.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.mahmoud.app.Adapters.SameVideoAdapter;
import ir.mahmoud.app.Asynktask.GetSameVideos;
import ir.mahmoud.app.Classes.ExpandableTextView;
import ir.mahmoud.app.Classes.RecyclerItemClickListener;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VideoDetailActivity extends AppCompatActivity implements IWebService2 {

    PostModel myModel;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.postContent_tv)
    ExpandableTextView postContentTv;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    SameVideoAdapter adapter;
    @BindView(R.id.postTitile_tv)
    TextView postTitileTv;

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
        postTitileTv.setText(myModel.getTitle());
        postContentTv.setText(myModel.getContent());
        // get same videos
        GetSameVideos getdata = new GetSameVideos(this, this, myModel.tagSlug);
        getdata.getData();
    }

    public void getModel() {
        Intent i = getIntent();
        myModel = (PostModel) i.getSerializableExtra("feedItem");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick({R.id.videoRelative, R.id.share_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.videoRelative:
                Intent intent = new Intent(this, ShowVideoActivity.class);
                intent.putExtra("title", myModel.getTitle());
                intent.putExtra("url", myModel.getVideoUrl());
                startActivity(intent);
                break;
            case R.id.share_icon:
                //share Video Url
                share();
                break;
        }
    }

    private void share() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, myModel.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, myModel.getVideoUrl());

        startActivity(Intent.createChooser(share, "اشتراک گذاری از طریق"));
    }

    @Override
    public void getResult(Object result) throws Exception {
        showList((List<PostModel>) result);
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
    }

    private void showList(final List<PostModel> sameVidesList) {
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        adapter = new SameVideoAdapter(this, sameVidesList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(VideoDetailActivity.this, VideoDetailActivity.class);
                intent.putExtra("feedItem", sameVidesList.get(position));
                startActivity(intent);
            }
        }));

    }
}
