package ir.mahmoud.app.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Fragments.MainFragment;
import ir.mahmoud.app.Fragments.MarkedFragment;
import ir.mahmoud.app.Fragments.VideosFragment;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static ir.mahmoud.app.Classes.HSH.openFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMenuItemClickListener {

    Toolbar toolbar;
    ImageButton btn_clear, btn_srch;
    private LinearLayout ll_bottomNavigation;
    private TextView txt_home, txt_vip, txt_newest, txt_attractive, txt_marked;
    private EditText edt_search;
    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private MainFragment home_fragment = null;
    private VideosFragment dayTutorial_fragment = null;
    private MarkedFragment marked_fragment = null;

    private void AssignViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        btn_clear = (ImageButton) findViewById(R.id.btn_clear);
        btn_srch = (ImageButton) findViewById(R.id.btn_srch);
        txt_home = (TextView) findViewById(R.id.txt_home);
        txt_vip = (TextView) findViewById(R.id.txt_vip);
        txt_newest = (TextView) findViewById(R.id.txt_newest);
        txt_attractive = (TextView) findViewById(R.id.txt_attractive);
        txt_marked = (TextView) findViewById(R.id.txt_marked);
        edt_search = (EditText) findViewById(R.id.edit_srch);
        txt_home.setOnClickListener(this);
        txt_vip.setOnClickListener(this);
        txt_newest.setOnClickListener(this);
        txt_attractive.setOnClickListener(this);
        txt_marked.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_srch.setOnClickListener(this);
        ll_bottomNavigation = (LinearLayout) findViewById(R.id.ll_bottomNavigation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssignViews();
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        initMenuFragment();

        home_fragment = new MainFragment();
        openFragment(MainActivity.this, home_fragment);
        HSH.setMainDrawableColor(ll_bottomNavigation, txt_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        // _menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(false);
        int id = item.getItemId();
        if (id == R.id.action_search) {
            HSH.display(MainActivity.this, findViewById(R.id.search_bar));
            //HSH.hide(MainActivity.this, tabHost);
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        MenuObject account = new MenuObject("ساخت حساب کاربری");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        account.setBitmap(b);

        MenuObject download = new MenuObject("دانلود ها");
        BitmapDrawable b1 = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        download.setDrawable(b1);

        MenuObject mintroduce = new MenuObject("معرفی به دوستان");
        Bitmap b2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mintroduce.setBitmap(b2);

        MenuObject aboutUs = new MenuObject("درباره ما");
        Bitmap b3 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        aboutUs.setBitmap(b3);

        menuObjects.add(close);
        menuObjects.add(account);
        menuObjects.add(download);
        menuObjects.add(mintroduce);
        menuObjects.add(aboutUs);

        return menuObjects;
    }// end getMenuObjects()

    @Override
    public void onClick(View v) {
        HSH.setMainDrawableColor(ll_bottomNavigation, v);
        switch (v.getId()) {
            case R.id.btn_clear:
                HSH.hide(MainActivity.this, findViewById(R.id.search_bar));
                break;
            case R.id.btn_srch:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("searchString", edt_search.getText().toString());
                startActivity(intent);
                break;
            case R.id.txt_home:
                //if (home_fragment == null)
                home_fragment = new MainFragment();
                openFragment(MainActivity.this, home_fragment);
                break;

            case R.id.txt_vip:
                Application.getInstance().videoType = "پیشنهاد-ویژه";
                //if (dayTutorial_fragment == null)
                dayTutorial_fragment = new VideosFragment();
                openFragment(MainActivity.this, dayTutorial_fragment);
                break;
            case R.id.txt_newest:
                Application.getInstance().videoType = "جدیدترین-ها";
                //if (dayTutorial_fragment == null)
                dayTutorial_fragment = new VideosFragment();
                openFragment(MainActivity.this, dayTutorial_fragment);
                break;

            case R.id.txt_attractive:
                Application.getInstance().videoType = "جذابترین_ها";
                //if (dayTutorial_fragment == null)
                dayTutorial_fragment = new VideosFragment();
                openFragment(MainActivity.this, dayTutorial_fragment);
                break;

            case R.id.txt_marked:
                marked_fragment = new MarkedFragment();
                openFragment(MainActivity.this, marked_fragment);
                break;

            default:
                break;
        }
    }

    public void menuClick(View v) {
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

                if (tag.equals("MainFragment"))
                    HSH.setMainDrawableColor(ll_bottomNavigation, txt_home);

                else if (tag.equals("VideosFragment"))
                    HSH.setMainDrawableColor(ll_bottomNavigation, txt_vip);

                else if (tag.equals("NewIdeasFragment"))
                    HSH.setMainDrawableColor(ll_bottomNavigation, txt_newest);

                else if (tag.equals("VideosFragment"))
                    HSH.setMainDrawableColor(ll_bottomNavigation, txt_attractive);
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

    @Override
    public void onMenuItemClick(View view, int position) {
        switch (position) {
            case 0:
                // بستن
                break;
            case 1:
                Toast.makeText(this, "ساخت حساب کاربری", Toast.LENGTH_SHORT).show();
                break;

            case 2:
                Toast.makeText(this, "دانلودها", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "معرفی به دوستان", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this, "درباره ما", Toast.LENGTH_SHORT).show();
                break;
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
