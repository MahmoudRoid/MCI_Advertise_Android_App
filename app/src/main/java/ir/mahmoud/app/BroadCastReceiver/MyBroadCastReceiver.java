package ir.mahmoud.app.BroadCastReceiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Models.tbl_Download;
import ir.mahmoud.app.Models.tbl_PostModel;


public class MyBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            long refrenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (refrenceId != -1) {
                int videoId = (int) Application.getInstance().hashMap.get(refrenceId);
                for (tbl_PostModel model : Application.getInstance().getDownloadList()) {
                    if (model.getPostid() == videoId) {
                        // save into download Table
                        tbl_Download tbl = new tbl_Download(model.getPostid(), model.getTitle(), model.getContent(),
                                model.getDate(), model.categorytitle, model.videourl, model.getImageurl(), model.getTagslug());
                        tbl.save();
                        HSH.showtoast(context, "ویدئو با موفقیت ذخیره شد");
                        break;
                    }
                }
            } else {
                HSH.showtoast(context, "متاسفانه ویدئو در لیست ویدئوها ذخیره نشد");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HSH.showtoast(context, "متاسفانه مشکلی در ذخیره ویدئو پیش آمده است");
        }

    }
}
