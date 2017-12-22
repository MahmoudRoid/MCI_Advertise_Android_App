package ir.mahmoud.app.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import ir.mahmoud.app.R;

public class ShowVideoActivity extends AppCompatActivity implements BetterVideoCallback {

    private BetterVideoPlayer player;
    String videoTitle,videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        getData();
        player = (BetterVideoPlayer) findViewById(R.id.player);
        player.setCallback(this);
        player.setSource(Uri.parse(videoUrl));
        player.setAutoPlay(true);
    }

    private void getData() {
        videoTitle = getIntent().getStringExtra("title");
        videoUrl = getIntent().getStringExtra("url");
    }

    @Override
    public void onStarted(BetterVideoPlayer player) {
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
