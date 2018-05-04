package ir.mahmoud.app.Classes;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;



public class DownloadAppService extends IntentService{

    public DownloadAppService() {
        super("shodani");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String appUrl = intent.getStringExtra("URL");

        DownloadManager mgr = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(appUrl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("دانلود نسخه جدید");
        mgr.enqueue(request);

    }
}

