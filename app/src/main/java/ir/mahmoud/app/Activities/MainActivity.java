package ir.mahmoud.app.Activities;

import android.Manifest;
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

import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ir.mahmoud.app.Asynktask.CheckNewVersion;
import ir.mahmoud.app.BuildConfig;
import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.BaseActivity;
import ir.mahmoud.app.Classes.DownloadAppService;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Classes.PermissionHandler;
import ir.mahmoud.app.Fragments.AttractiveFragment;
import ir.mahmoud.app.Fragments.MainFragment;
import ir.mahmoud.app.Fragments.MarkedFragment;
import ir.mahmoud.app.Fragments.NewestFragment;
import ir.mahmoud.app.Fragments.VipFragment;
import ir.mahmoud.app.Interfaces.IWebServiceByTag;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static ir.mahmoud.app.Classes.HSH.openFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnMenuItemClickListener,IWebServiceByTag {

    public static LinearLayout ll_bottomNavigation;
    public static TextView txt_home, txt_vip, txt_newest, txt_attractive, txt_marked;
    Toolbar toolbar;
    ImageButton btn_clear, btn_srch;
    private EditText edt_search;
    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private MainFragment home_fragment = null;
    private VipFragment vip_fragment = null;
    private AttractiveFragment attactive_fragment = null;
    private NewestFragment newest_fragment = null;
    private MarkedFragment marked_fragment = null;

    String[] writePermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public final String CHECK_VERSION_TAG = "check_version";

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
        // check for new version
        checkNewVerson();

        fragmentManager = getSupportFragmentManager();
        initMenuFragment();

        home_fragment = new MainFragment();
        openFragment(MainActivity.this, home_fragment);
        HSH.setMainDrawableColor(ll_bottomNavigation, txt_home);
    }

    private void checkNewVerson() {
        int versionCode = BuildConfig.VERSION_CODE;
        // send it to server
        if (NetworkUtils.getConnectivity(this)) {
            // call check version API
            CheckNewVersion post = new CheckNewVersion(MainActivity.this, MainActivity.this,
                    String.valueOf(versionCode), CHECK_VERSION_TAG);
            post.getData();
        }
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
        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.mipmap.ic_close);

        MenuObject download = new MenuObject("دانلود ها");
        BitmapDrawable b1 = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_download));
        download.setDrawable(b1);

        MenuObject mintroduce = new MenuObject("معرفی به دوستان");
        Bitmap b2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_introduce);
        mintroduce.setBitmap(b2);

//        MenuObject aboutUs = new MenuObject("درباره ما");
//        Bitmap b3 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        aboutUs.setBitmap(b3);

        menuObjects.add(close);
//        menuObjects.add(account);
        menuObjects.add(download);
        menuObjects.add(mintroduce);
//        menuObjects.add(aboutUs);

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
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                break;
            case R.id.txt_home:
                if (!Application.getInstance().videoType.equals("خانه")) {
                    Application.getInstance().videoType = "خانه";
                    home_fragment = new MainFragment();
                    openFragment(MainActivity.this, home_fragment);
                }
                break;

            case R.id.txt_vip:
                if (!Application.getInstance().videoType.equals("پیشنهاد-ویژه")) {
                    Application.getInstance().videoType = "پیشنهاد-ویژه";
                    if (vip_fragment == null)
                        vip_fragment = new VipFragment();
                    openFragment(MainActivity.this, vip_fragment);
                }
                break;
            case R.id.txt_newest:
                if (!Application.getInstance().videoType.equals("جدیدترین-ها")) {
                    Application.getInstance().videoType = "جدیدترین-ها";
                    if (newest_fragment == null)
                        newest_fragment = new NewestFragment();
                    openFragment(MainActivity.this, newest_fragment);
                }
                break;

            case R.id.txt_attractive:
                if (!Application.getInstance().videoType.equals("جذابترین-ها")) {
                    Application.getInstance().videoType = "جذابترین-ها";
                    if (attactive_fragment == null)
                        attactive_fragment = new AttractiveFragment();
                    openFragment(MainActivity.this, attactive_fragment);
                }
                break;

            case R.id.txt_marked:
                Application.getInstance().videoType = "نشان شده-ها";
                if (marked_fragment == null)
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
        exit();
    }

    @Override
    public void onMenuItemClick(View view, int position) {
        switch (position) {
            case 0:
                // بستن
                break;
//            case 1:
//                Toast.makeText(this, "ساخت حساب کاربری", Toast.LENGTH_SHORT).show();
//                break;

            case 1:
                // دانلودها
                startActivity(new Intent(MainActivity.this, DownloadsActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 2:
//                String shareBody = "سلام.این برنامه خیلی باحاله.با شدنی میتونی کلی فیلم جالب و جذاب ببینی\n " + "http://cafebazaar.ir/app/ir.mahmoud.app/?l=fa" ;
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "شدنی\n\n");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                startActivity(Intent.createChooser(sharingIntent, "اشتراک گذاری"));
                sendApp();
                break;
//
        }
    }

    private void sendApp() {
        try {
            ArrayList<Uri> uris = new ArrayList<>();
            Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            sendIntent.setType("application/*");
            uris.add(Uri.fromFile(new File(getApplicationInfo().publicSourceDir)));
            sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            startActivity(Intent.createChooser(sendIntent, null));
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void getResult(Object result, String Tag) throws Exception {
        showUpdateDialog(String.valueOf(result));
    }

    @Override
    public void getError(String ErrorCodeTitle, String Tag) throws Exception {

    }


    private void showUpdateDialog(final String appUrl) {
        final AlertDialog.Builder alertComment = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alertComment.setMessage(HSH.setTypeFace(MainActivity.this, "آیا مایل به دانلود نسخه جدید برنامه هستید؟"));
        alertComment.setPositiveButton(HSH.setTypeFace(MainActivity.this, "بله"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (NetworkUtils.getConnectivity(MainActivity.this)) {
                    new PermissionHandler().checkPermission(MainActivity.this, writePermission, new PermissionHandler.OnPermissionResponse() {
                        @Override
                        public void onPermissionGranted() {
                            Intent intent = new Intent(MainActivity.this, DownloadAppService.class);
                            intent.putExtra("URL", appUrl);
                            startService(intent);
                        }

                        @Override
                        public void onPermissionDenied() {
                            HSH.showtoast(MainActivity.this, "برای دانلود اپلیکیشن دسترسی را صادر نمایید.");
                        }
                    });

                } else {
                    HSH.showtoast(getApplicationContext(), getResources().getString(R.string.error_internet));
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
