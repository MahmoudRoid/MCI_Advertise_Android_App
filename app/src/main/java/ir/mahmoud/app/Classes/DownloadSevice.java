package ir.mahmoud.app.Classes;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;


/**
 * Created by soheilsystem on 1/3/2018.
 */

public class DownloadSevice extends IntentService {

    public DownloadSevice() {
        super("shodani");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String videoUrl = intent.getStringExtra("URL");
        String videoName = intent.getStringExtra("Name");
        String videoId = intent.getStringExtra("Id");

        DownloadManager mgr = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(videoUrl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("دانلود ویدئو")
                .setDestinationInExternalPublicDir("/shodani/videos", videoName);
        long refrenceId = mgr.enqueue(request);
        Application.getInstance().hashMap.put(refrenceId, Integer.valueOf(videoId));

//        Intent i = new Intent(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        sendBroadcast(i);

    }
}
