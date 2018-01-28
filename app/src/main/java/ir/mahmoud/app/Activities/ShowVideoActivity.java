package ir.mahmoud.app.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.BaseActivity;
import ir.mahmoud.app.Classes.DownloadSevice;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Classes.PermissionHandler;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;

public class ShowVideoActivity extends BaseActivity implements BetterVideoCallback {

    String videoTitle, videoUrl, videoId, videoContent, videoDate, videoCategoryTitle,
            videoImageUrl, videoTaglug, videoPostUrl;
    long refrenceId;
    BroadcastReceiver receiver;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    @BindView(R.id.default_Image)
    ImageView defaultImage;
    private BetterVideoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        ButterKnife.bind(this);
        getData();
        player = (BetterVideoPlayer) findViewById(R.id.player);
        player.setLoadingStyle(2);
        player.setCallback(this);
        if (isVideoExists()) {
            File file = new File(Application.VIDEO + "/" + videoId + ".mp4");
            player.setSource(Uri.fromFile(file));
            player.setAutoPlay(true);
        } else {
            // agar net vojood dasht neshan dahad
            if (NetworkUtils.getConnectivity(this)) {
                player.setSource(Uri.parse(videoUrl));
                player.setAutoPlay(true);
            } else HSH.showtoast(this, getString(R.string.error_internet));

        }
        player.enableSwipeGestures();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //registerReceiver(receiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private boolean isVideoExists() {
        File file = new File(Application.VIDEO + "/" + videoId + ".mp4");
        if (file.exists()) {
            return true;
        } else return false;
    }

    private void getData() {
        videoId = getIntent().getStringExtra("id");
        videoTitle = getIntent().getStringExtra("title");
        videoContent = getIntent().getStringExtra("content");
        videoDate = getIntent().getStringExtra("date");
        videoCategoryTitle = getIntent().getStringExtra("categoryTitle");
        videoUrl = getIntent().getStringExtra("url");
        videoImageUrl = getIntent().getStringExtra("imageUrl");
        videoTaglug = getIntent().getStringExtra("tagSlug");
        videoPostUrl = getIntent().getStringExtra("postUrl");
    }

    public void saveVideo(View view) {
        File direct = new File(Application.VIDEO);
        if (!direct.exists()) {
            direct.mkdirs();
        }
        String nameOfFile = videoId + ".mp4";
        File file = new File(direct, nameOfFile);
        if (file.exists()) {
            HSH.showtoast(ShowVideoActivity.this, "قبلا این ویدئو را دانلود کردید!");
        } else {
            Dialogcoose(nameOfFile, videoUrl);
        }
    }

    public void Dialogcoose(final String fileName, final String fileUrl) {
        final AlertDialog.Builder alertComment = new AlertDialog.Builder(ShowVideoActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alertComment.setMessage(HSH.setTypeFace(ShowVideoActivity.this, "آیا میخواهید فایل را دانلود کنید؟"));
        alertComment.setPositiveButton(HSH.setTypeFace(ShowVideoActivity.this, "بله"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (NetworkUtils.getConnectivity(ShowVideoActivity.this)) {
                    new PermissionHandler().checkPermission(ShowVideoActivity.this, permissions, new PermissionHandler.OnPermissionResponse() {
                        @Override
                        public void onPermissionGranted() {
                            // add to download List
                            tbl_PostModel model = new tbl_PostModel();
                            model.setPostid(Long.valueOf(videoId));
                            model.setTitle(videoTitle);
                            model.setContent(videoContent);
                            model.setDate(videoDate);
                            model.setCategorytitle(videoCategoryTitle);
                            model.setVideourl(videoUrl);
                            model.setImageurl(videoImageUrl);
                            model.setTagslug(videoTaglug);
                            model.setPosturl(videoPostUrl);
                            Application.getInstance().downloadList.add(model);

                            Intent intent = new Intent(ShowVideoActivity.this, DownloadSevice.class);
                            intent.putExtra("URL", fileUrl);
                            intent.putExtra("Name", fileName);
                            intent.putExtra("Id", videoId);
                            startService(intent);
                        }

                        @Override
                        public void onPermissionDenied() {
                            HSH.showtoast(ShowVideoActivity.this, "برای دانلود ویدئو دسترسی را صادر نمایید.");
                        }
                    });

                } else {
                    HSH.showtoast(getApplicationContext(), getResources().getString(R.string.error_internet));
                }
            }
        });

        alertComment.setNegativeButton(HSH.setTypeFace(ShowVideoActivity.this, "خیر"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alertComment.show();
    }

    @Override
    public void onStarted(BetterVideoPlayer player) {
        defaultImage.setVisibility(View.GONE);
    }

    @Override
    public void onPaused(BetterVideoPlayer player) {
        player.pause();
    }

    @Override
    public void onPreparing(BetterVideoPlayer player) {

    }

    @Override
    public void onPrepared(BetterVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(BetterVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(BetterVideoPlayer player) {

    }

    @Override
    public void onToggleControls(BetterVideoPlayer player, boolean isShowing) {

    }
}
