package ir.mahmoud.app.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Fragments.DayTutorialFragment;
import ir.mahmoud.app.Fragments.HomeFragment;
import ir.mahmoud.app.Fragments.NewIdeasFragment;
import ir.mahmoud.app.Fragments.VideosFragment;
import ir.mahmoud.app.R;

import static ir.mahmoud.app.Classes.HSH.openFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static LinearLayout ll_bottomNavigation;
    TextView txt_home;
    TextView txt_tutorial;
    TextView txt_newIdeas;
    TextView txt_videos;
    private HomeFragment home_fragment = null;
    private DayTutorialFragment dayTutorial_fragment = null;
    private NewIdeasFragment newIdeas_fragment = null;
    private VideosFragment videos_fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_home = (TextView) findViewById(R.id.txt_home);
        txt_tutorial = (TextView) findViewById(R.id.txt_tutorial);
        txt_newIdeas = (TextView) findViewById(R.id.txt_newIdeas);
        txt_videos = (TextView) findViewById(R.id.txt_videos);
        txt_home.setOnClickListener(this);
        txt_tutorial.setOnClickListener(this);
        txt_newIdeas.setOnClickListener(this);
        txt_videos.setOnClickListener(this);


        ll_bottomNavigation = (LinearLayout) findViewById(R.id.linearLayout);

        home_fragment = new HomeFragment();
        openFragment(MainActivity.this, home_fragment);
        HSH.setMainDrawableColor(ll_bottomNavigation, txt_home);
    }

    @Override
    public void onClick(View v) {
        HSH.setMainDrawableColor(ll_bottomNavigation, v);
        switch (v.getId()) {
            case R.id.txt_home:
                if (home_fragment == null)
                    home_fragment = new HomeFragment();
                openFragment(MainActivity.this, home_fragment);
                break;

            case R.id.txt_tutorial:
                if (dayTutorial_fragment == null)
                    dayTutorial_fragment = new DayTutorialFragment();
                openFragment(MainActivity.this, dayTutorial_fragment);
                break;

            case R.id.txt_newIdeas:
                if (newIdeas_fragment == null)
                    newIdeas_fragment = new NewIdeasFragment();
                openFragment(MainActivity.this, newIdeas_fragment);
                break;

            case R.id.txt_videos:
                if (videos_fragment == null)
                    videos_fragment = new VideosFragment();
                openFragment(MainActivity.this, videos_fragment);
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            exit();
        } else {
            try {
                String tag = getSupportFragmentManager()
                        .getBackStackEntryAt(getSupportFragmentManager()
                                .getBackStackEntryCount() - 2).getName();

                if (tag.equals("HomeFragment"))
                    HSH.setMainDrawableColor(ll_bottomNavigation, txt_home);

                else if (tag.equals("DayTutorialFragment"))
                    HSH.setMainDrawableColor(ll_bottomNavigation, txt_tutorial);

                else if (tag.equals("NewIdeasFragment"))
                    HSH.setMainDrawableColor(ll_bottomNavigation, txt_newIdeas);

                else if (tag.equals("VideosFragment"))
                    HSH.setMainDrawableColor(ll_bottomNavigation, txt_videos);
                super.onBackPressed();
            } catch (Exception e) {
                try {
                    getSupportFragmentManager().popBackStack();
                } catch (Exception e1) {
                    exit();
                }
            }
        }
    }

    public void exit() {
        final AlertDialog.Builder alertComment = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alertComment.setMessage(HSH.setTypeFace(MainActivity.this, "آیا مایل به خروج از برنامه هستید؟"));
        alertComment.setPositiveButton(HSH.setTypeFace(MainActivity.this, "بله"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                try {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    System.exit(0);
                    finish();
                } catch (Exception e) {
                }

            }
        });
        alertComment.setNeutralButton(HSH.setTypeFace(MainActivity.this, "نظر دهید"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setData(Uri.parse("bazaar://details?id=" + getPackageName()));
                    intent.setPackage("com.farsitel.bazaar");
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        });
        alertComment.setNegativeButton(HSH.setTypeFace(MainActivity.this, "خیر"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alertComment.show();
    }
}
