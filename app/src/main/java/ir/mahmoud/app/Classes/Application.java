package ir.mahmoud.app.Classes;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
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

    public List<SlideShowModel> sl = new ArrayList<>() ;
    public List<PostModel> vip_feed = new ArrayList<>() ;
    public List<PostModel> newest_feed = new ArrayList<>() ;
    public List<PostModel> attractive_feed = new ArrayList<>() ;
    public List<PostModel> tagged_feed = new ArrayList<>() ;
    public String videoType = "";

    private static final Application ourInstance = new Application();
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
