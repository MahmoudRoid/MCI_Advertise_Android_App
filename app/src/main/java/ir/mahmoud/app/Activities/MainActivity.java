package ir.mahmoud.app.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Fragments.DayTutorialFragment;
import ir.mahmoud.app.Fragments.HomeFragment;
import ir.mahmoud.app.Fragments.NewIdeasFragment;
import ir.mahmoud.app.Fragments.VideosFragment;
import ir.mahmoud.app.R;

import static ir.mahmoud.app.Classes.HSH.openFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMenuItemClickListener {

    public static LinearLayout ll_bottomNavigation;
    TextView txt_home;
    TextView txt_tutorial;
    TextView txt_newIdeas;
    TextView txt_videos;
    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private HomeFragment home_fragment = null;
    private DayTutorialFragment dayTutorial_fragment = null;
    private NewIdeasFragment newIdeas_fragment = null;
    private VideosFragment videos_fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        initMenuFragment();
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

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
//        mMenuDialogFragment.setItemLongClickListener(this);
    }// end initMenuFragment()

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.mipmap.ic_launcher);

        MenuObject like = new MenuObject("First Item");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        like.setBitmap(b);

        MenuObject addFr = new MenuObject("Second Item");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        addFr.setDrawable(bd);

        menuObjects.add(close);
        menuObjects.add(like);
        menuObjects.add(addFr);

        return menuObjects;
    }// end getMenuObjects()

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

    public void xxx(View v) {
        mMenuDialogFragment.show(fragmentManager, "ContextMenuDialogFragment");
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

    @Override
    public void onMenuItemClick(View view, int position) {
        switch (position) {
            case 0:
                // بستن
                break;
            case 1:
                Toast.makeText(this, "first Item", Toast.LENGTH_SHORT).show();
                break;

            case 2:
                Toast.makeText(this, "second Item", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
