package ir.mahmoud.app.Classes;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;

import com.orm.SugarApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.Models.SlideShowModel;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

//import java.util.HashMap;
//import me.cheshmak.android.sdk.core.Cheshmak;
//import me.cheshmak.android.sdk.core.CheshmakConfig;

public class Application extends SugarApp {

    public static final String ROOT = Environment.getExternalStorageDirectory()+"/shodani";
    public static final String VIDEO = ROOT + "/videos";


    private static final Application ourInstance = new Application();
    public List<PostModel> sl = new ArrayList<>();
    public List<PostModel> vip_feed = new ArrayList<>();
    public List<PostModel> newest_feed = new ArrayList<>();
    public List<PostModel> attractive_feed = new ArrayList<>();
    public List<PostModel> tagged_feed = new ArrayList<>();
    public List<PostModel> vip_feed_list = new ArrayList<>();
    public List<PostModel> newest_feed_list = new ArrayList<>();
    public List<PostModel> attractive_feed_list = new ArrayList<>();
    public List<PostModel> tagged_feed_list = new ArrayList<>();
    public List<PostModel> downloadList = new ArrayList<>();
    public String videoType = "";

    public HashMap getHashMap() {
        return hashMap;
    }

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

    public List<PostModel> getVip_feed() {
        return vip_feed;
    }

    public void setVip_feed(List<PostModel> vip_feed) {
        this.vip_feed = vip_feed;
    }

    public List<PostModel> getDownloadList(){return downloadList;}

}
