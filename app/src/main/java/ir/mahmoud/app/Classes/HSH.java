package ir.mahmoud.app.Classes;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import ir.mahmoud.app.R;

/**
 * Created by hossein1 on 6/28/2014.
 */
public class HSH {

    private static final String TIMEPICKER = "TimePickerDialog",
            DATEPICKER = "DatePickerDialog";
    private static String[] persianNumbers = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};
    private static String[] englishNumbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public static void dialog(Dialog dialog) {
        Window window = dialog.getWindow();
        ViewGroup.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes((WindowManager.LayoutParams) params);
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }

    public static String toEnglishNumber(String text) {
        if ("".equals(text)) return "";
        String ch, str = "";
        int i = 0;
        while (text.length() > i) {
            ch = String.valueOf(text.charAt(i));
            if (TextUtils.isDigitsOnly(ch)) str += englishNumbers[Integer.parseInt(ch)];
            else str += ch;
            i++;
        }
        return str;
    }

    public static SpannableStringBuilder setTypeFace(Context cn, String s)

    {
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(s);
        ssbuilder.setSpan(new CustomTypefaceSpan("IRANSansMedium.ttf", cn), 0, ssbuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return ssbuilder;
    }

    public static void onOpenPage(Context context, @SuppressWarnings("rawtypes") Class tow_class) {
        Intent intent = new Intent(context, tow_class);
        context.startActivity(intent);
    }

    public static boolean isNetworkConnection(Context context) {
        ConnectivityManager con =
                (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = con.getActiveNetworkInfo();
        if (info == null)
            return false;
        else
            return true;
    }

    public static void showtoast(Context cn, String s) {

// ساخت یک لایه با استفاده از toast.xml file


        LayoutInflater inflater = (LayoutInflater) cn.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

// فراخوانی لایه برای نمایش toast
        View toastRoot = inflater.inflate(R.layout.custom_toast, null);

        TextView t = (TextView) toastRoot.findViewById(R.id.text);
        t.setText(s);
        Toast toast = new Toast(cn);

// ست کردن مقادیر لایه به toast جهت نمایش
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
                0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @SuppressLint("NewApi")
    public static String toPersianNumber(String text) {
        if (text.isEmpty()) return "";
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out += persianNumbers[number];
            } else if (c == '٫') {
                out += '،';
            } else {
                out += c;
            }
        }
        return out;
    }

    public static String getCompleteAddress(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Locale lHebrew = new Locale("fa");
        Geocoder geocoder = new Geocoder(context, lHebrew);
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {

                Address address = addresses.get(0);
                String street = address.getThoroughfare();
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
//				String country    = addresses.get(0).getCountryName();
//				String postalCode = addresses.get(0).getPostalCode();
//				String knownName  = addresses.get(0).getFeatureName();

                if (street != null)
                    strAdd = street;
                else
                    strAdd = city;


            } else {
            }

        } catch (Exception e) {
        }
        return strAdd;
    }

    //////////////////////////////////////////////////////////////////////////
    public static void setTextViewDrawableColor(TextView textView, int x, int y, int z) {
        try {
            for (Drawable drawable : textView.getCompoundDrawables()) {
                if (drawable != null) {
                    drawable.setColorFilter(new PorterDuffColorFilter(Color.rgb(x, y, z), PorterDuff.Mode.MULTIPLY));
                }
            }
            textView.setTextColor(Color.rgb(x, y, z));
        } catch (Exception e) {
        }
    }

    public static void setMainDrawableColor(LinearLayout layout, View view) {
        for (int j = 0; j < layout.getChildCount(); j++) {
            View v = layout.getChildAt(j);
            if (v.getId() == view.getId()) {
                setTextViewDrawableColor((TextView) v, 25, 120, 130);
            } else {
                setTextViewDrawableColor((TextView) v, 150, 150, 150);
            }
        }
    }

    public static void openFragment(Activity activity, Fragment fragment) {
        String fragmentTag = fragment.getClass().getSimpleName();
        FragmentManager fragmentManager = ((AppCompatActivity) activity)
                .getSupportFragmentManager();

        boolean fragmentPopped = fragmentManager
                .popBackStackImmediate(fragmentTag, 0);

        FragmentTransaction ftx = fragmentManager.beginTransaction();
        if (!fragmentPopped && fragmentManager.findFragmentByTag(fragmentTag) == null)
            ftx.addToBackStack(fragment.getClass().getSimpleName());

        ftx.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left, R.anim.slide_in_left,
                R.anim.slide_out_right);
        ftx.replace(R.id.frame, fragment, fragmentTag);
        ftx.commit();
    }

    public static void display(final Context ctx, final View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                final Animator animator = ViewAnimationUtils.createCircularReveal(v,
                        v.getWidth() - Utils.dpToPx(ctx, 56),
                        Utils.dpToPx(ctx, 23),
                        0,
                        (float) Math.hypot(v.getWidth(), v.getHeight()));
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setEnabled(true);
                        if (v instanceof EditText)
                            ((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                v.setVisibility(View.VISIBLE);
                if (v.getVisibility() == View.VISIBLE) {
                    animator.setDuration(500);
                    animator.start();
                    v.setEnabled(true);
                }
            } catch (Exception e) {
                HSH.showtoast(ctx, e.getMessage() + "11");
            }
        } else {
            v.setVisibility(View.VISIBLE);
            v.setEnabled(true);
            ((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public static void hide(final Context ctx, final View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                final Animator animatorHide = ViewAnimationUtils.createCircularReveal(v,
                        v.getWidth() - Utils.dpToPx(ctx, 56),
                        Utils.dpToPx(ctx, 23),
                        (float) Math.hypot(v.getWidth(), v.getHeight()),
                        0);
                animatorHide.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animatorHide.setDuration(500);
                animatorHide.start();
            } catch (Exception e) {
                HSH.showtoast(ctx, e.getMessage() + "222");
            }
        } else {
            v.setVisibility(View.GONE);
        }
    }
}