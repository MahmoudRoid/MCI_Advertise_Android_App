package ir.mahmoud.app.Classes;

import android.os.Environment;

import com.orm.SugarApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

//import java.util.HashMap;
//import me.cheshmak.android.sdk.core.Cheshmak;
//import me.cheshmak.android.sdk.core.CheshmakConfig;

public class Application extends SugarApp {

    public static final String ROOT = Environment.getExternalStorageDirectory() + "/shodani";
    public static final String VIDEO = ROOT + "/videos";


    private static final Application ourInstance = new Application();
    public List<tbl_PostModel> sl = new ArrayList<>();
    public List<tbl_PostModel> vip_feed = new ArrayList<>();
    public List<tbl_PostModel> newest_feed = new ArrayList<>();
    public List<tbl_PostModel> attractive_feed = new ArrayList<>();
    public List<tbl_PostModel> vip_feed_list = new ArrayList<>();
    public List<tbl_PostModel> newest_feed_list = new ArrayList<>();
    public List<tbl_PostModel> attractive_feed_list = new ArrayList<>();
    public List<tbl_PostModel> downloadList = new ArrayList<>();
    public String videoType = "خانه";
    public HashMap hashMap = new HashMap();

    public static Application getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/IRANSansMedium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public List<tbl_PostModel> getDownloadList() {
        return downloadList;
    }

}
