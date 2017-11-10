package ir.mahmoud.app;

import android.content.SharedPreferences;
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

//import java.util.HashMap;
//import me.cheshmak.android.sdk.core.Cheshmak;
//import me.cheshmak.android.sdk.core.CheshmakConfig;

/**
 * Created by Amir khodabandeh on 2/11/16 AD.
 */
public class Application extends android.app.Application {


    public static Typeface font;
    public static Animation in;
    public static Animation out;
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;

    public static void setTypeFace(ViewGroup viewGroup, Typeface typeface) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(Application.font);
                ((TextView) view).setTextSize(13);
                //((TextView) view).setSingleLine();
            }
            if (view instanceof ViewGroup) {
                setTypeFace(((ViewGroup) view), typeface);
            }
        }
    }

    public static void myCustomSnackbar(Snackbar snackbar) {
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
        textView.setGravity(Gravity.LEFT);
        TextView textView2 = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView2.setTypeface(Application.font);
        textView.setTypeface(Application.font);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        in = AnimationUtils.loadAnimation(this,
                R.anim.zoom_in);

        out = AnimationUtils.loadAnimation(this,
                R.anim.zoom_out);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }
}
