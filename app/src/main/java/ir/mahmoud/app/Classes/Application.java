package ir.mahmoud.app.Classes;

import com.orm.SugarApp;

import java.util.ArrayList;
import java.util.List;

import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.Models.SlideShowModel;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

//import java.util.HashMap;
//import me.cheshmak.android.sdk.core.Cheshmak;
//import me.cheshmak.android.sdk.core.CheshmakConfig;

public class Application extends SugarApp {

    private static final Application ourInstance = new Application();
    public List<SlideShowModel> sl = new ArrayList<>();
    public List<PostModel> vip_feed = new ArrayList<>();
    public List<PostModel> newest_feed = new ArrayList<>();
    public List<PostModel> attractive_feed = new ArrayList<>();
    public List<PostModel> tagged_feed = new ArrayList<>();
    public List<PostModel> vip_feed_list = new ArrayList<>();
    public List<PostModel> newest_feed_list = new ArrayList<>();
    public List<PostModel> attractive_feed_list = new ArrayList<>();
    public List<PostModel> tagged_feed_list = new ArrayList<>();
    public String videoType = "";

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


    public List<SlideShowModel> getSl() {
        return sl;
    }

    public void setSl(List<SlideShowModel> sl) {
        this.sl = sl;
    }

    public List<PostModel> getVip_feed() {
        return vip_feed;
    }

    public void setVip_feed(List<PostModel> vip_feed) {
        this.vip_feed = vip_feed;
    }


}
