package ir.mahmoud.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.mahmoud.app.Adapters.SameVideoAdapter;
import ir.mahmoud.app.Asynktask.GetSameVideos;
import ir.mahmoud.app.Classes.ExpandableTextView;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Classes.RecyclerItemClickListener;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VideoDetailActivity extends AppCompatActivity implements IWebService2 {

    tbl_PostModel myModel;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.postContent_tv)
    ExpandableTextView postContentTv;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    SameVideoAdapter adapter;
    @BindView(R.id.postTitle_tv)
    TextView postTitleTv;
    @BindView(R.id.toolbar_custom_tv)
    TextView toolbarCustomTv;
    @BindView(R.id.mark_icon)
    ImageView markIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getModel();
        setTitle("");
        toolbarCustomTv.setText(myModel.getTitle());
        Glide.with(this).load(myModel.imageurl).apply(new RequestOptions().placeholder(R.mipmap.homeb)).into(imageView);
        postTitleTv.setText(myModel.getTitle());
        postContentTv.setText(myModel.getContent());
        try {
            long count = Select.from(tbl_PostModel.class).where(Condition.prop("postid").eq(myModel.getPostid())).count();
            if (count > 0) markIcon.setImageResource(R.drawable.ic_mark);
            // get same videos
            if (NetworkUtils.getConnectivity(this)) {
                GetSameVideos getdata = new GetSameVideos(this, this, myModel.tagslug, pb);
                getdata.getData();
            } else HSH.showtoast(this, getString(R.string.error_internet));
        } catch (Exception e) {
            HSH.showtoast(VideoDetailActivity.this, e.getMessage());
        }
    }

    public void getModel() {
        Intent i = getIntent();
        myModel = (tbl_PostModel) i.getSerializableExtra("feedItem");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick({R.id.videoRelative, R.id.share_icon, R.id.mark_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.videoRelative:
                Intent intent = new Intent(this, ShowVideoActivity.class);
                intent.putExtra("id", String.valueOf(myModel.getPostid()));
                intent.putExtra("title", myModel.getTitle());
                intent.putExtra("content", myModel.getContent());
                intent.putExtra("date", myModel.getDate());
                intent.putExtra("categoryTitle", myModel.getCategorytitle());
                intent.putExtra("url", myModel.getVideourl());
                intent.putExtra("imageUrl", myModel.getImageurl());
                intent.putExtra("tagSlug", myModel.getTagslug());
                intent.putExtra("postUrl", myModel.getPosturl());

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.share_icon:
                share();
                break;
            case R.id.mark_icon:
                mark();
                break;
        }
    }

    private void mark() {
        long count = Select.from(tbl_PostModel.class).where(Condition.prop("postid").eq(myModel.getPostid())).count();
        if (count > 0) {
            // unmark and delete from database
            markIcon.setImageResource(R.drawable.ic_unmark);
            tbl_PostModel tbl = Select.from(tbl_PostModel.class).where(Condition.prop("postid").eq(myModel.getPostid())).first();
            tbl.delete();
        } else {
            // mark and save into db
            markIcon.setImageResource(R.drawable.ic_mark);
            tbl_PostModel tbl = new tbl_PostModel(myModel.getPostid(), myModel.getTitle(), myModel.getContent()
                    , myModel.getDate(), myModel.getCategorytitle(), myModel.getVideourl(), myModel.getImageurl()
                    , myModel.getTagslug(),myModel.getPosturl());
            tbl.save();
            HSH.showtoast(this, "نشان شد");
        }
    }

    private void share() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, myModel.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, myModel.getVideourl());
        startActivity(Intent.createChooser(share, "اشترک گذاری از طریق"));
    }
    @Override
    public void getResult(Object result) throws Exception {

        List<tbl_PostModel> list = (List<tbl_PostModel>) result;
        list = list.subList(0,6); // baraye joda kardane 5 iteme akhare list

        for (int i = 0; i < list.size() ; i++) {
            if(list.get(i).getPostid() == myModel.getPostid()){
                // remove this item
                list.remove(i);
            }
        }
        showList(list);
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        HSH.showtoast(this, "مشکلی پیش آمده است");
    }

    private void showList(final List<tbl_PostModel> sameVidesList) {
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
