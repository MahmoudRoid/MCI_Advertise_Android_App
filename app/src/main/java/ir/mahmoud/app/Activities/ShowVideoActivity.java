package ir.mahmoud.app.Activities;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;
import java.io.File;
import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.R;

public class ShowVideoActivity extends AppCompatActivity implements BetterVideoCallback {

    private BetterVideoPlayer player;
    String videoTitle,videoUrl,videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        getData();
        player = (BetterVideoPlayer) findViewById(R.id.player);
        player.setCallback(this);
        if(isVideoExists()){
            File file = new File(Application.VIDEO + "/" + videoId + ".mp4");
            player.setSource(Uri.fromFile(file));
            player.setAutoPlay(true);
        }
        else {
            player.setSource(Uri.parse(videoUrl));
            player.setAutoPlay(true);
        }

    }

    private boolean isVideoExists() {
        File file = new File(Application.VIDEO + "/" + videoId + ".mp4");
        if(file.exists()){
            return true;
        }
        else return false;
    }

    private void getData() {
        videoTitle = getIntent().getStringExtra("title");
        videoUrl = getIntent().getStringExtra("url");
        videoId = getIntent().getStringExtra("id");
    }
    
    public void saveVideo(View view){
        File direct = new File(Application.VIDEO);
        if (!direct.exists()) {
            direct.mkdirs();
        }
        String nameOfFile = videoId+".mp4";
        File file = new File(direct, nameOfFile);
        if (file.exists()) {
            Toast.makeText(ShowVideoActivity.this, "قبلا این ویدئو را دانلود کردید!", Toast.LENGTH_SHORT).show();
        }
        else {
            DialogChoose(nameOfFile,videoUrl);
        }
    }

    private void DialogChoose(final String fileName,final String fileUrl) {
        final Dialog d = new Dialog(this);
        d.setCancelable(true);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_choose);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = d.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final TextView txtHeader = (TextView) d.findViewById(R.id.txtHeader_dialog);
        final TextView txtContext = (TextView) d.findViewById(R.id.txtContext_dialog);
        final TextView txtOne = (TextView) d.findViewById(R.id.txtOne_dialog);
        final TextView txtTwo = (TextView) d.findViewById(R.id.txtTwo_dialog);
        final TextView txtThree = (TextView) d.findViewById(R.id.txtThree_dialog);

        txtHeader.setText("فایل ناموجود");
        txtContext.setText("آیا میخواهید فایل را دانلود کنید؟");
        txtOne.setText("بله");
        txtTwo.setVisibility(View.INVISIBLE);
        txtThree.setText("خیر");

        txtThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        txtOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    d.dismiss();
                    DownloadManager mgr = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri downloadUri = Uri.parse(fileUrl);
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                    request.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI
                                    | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false).setTitle("دانلود ویدئو")
                            .setDestinationInExternalPublicDir("/shodani/videos", fileName);
                    mgr.enqueue(request);
                }else{
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        d.show();
    }// end DialogChoose()
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }// isNetworkAvailable()

    @Override
    public void onStarted(BetterVideoPlayer player) {}

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
