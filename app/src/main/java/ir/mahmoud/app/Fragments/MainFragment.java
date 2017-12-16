package ir.mahmoud.app.Fragments;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.RightAlignedHorizontalScrollView;
import ir.mahmoud.app.Interfaces.ApiClient;
import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.Models.SlideShowModel;
import ir.mahmoud.app.R;
import ir.mahmoud.app.R.id;
import retrofit2.Call;
import retrofit2.Callback;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainFragment extends Fragment {

    public static PagerAdapter pagerAdapter;
    public static ViewPager pager;
    public static RadioGroup RgIndicator;
    public static RadioGroup.LayoutParams rprms;
    public static ProgressBar pb1, pb2, pb3;
    public static RelativeLayout rl_vip, rl_newest;
    public static LinearLayout hrsv_vip, hrsv_newest, hrsv_attractive, hrsv_tagged ;
    public static AppBarLayout appBar;
    int previousState = 0, currentPage = 0, scrollviewposition = 0;
    Timer timer;
    View rootView = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);

            rl_vip = rootView.findViewById(id.rl_vip);
            rl_newest = rootView.findViewById(R.id.rl_newest);
            ////////////////////////////////////////////////////////////
            hrsv_vip = rootView.findViewById(id.hrsv_vip);
            hrsv_newest = rootView.findViewById(id.hrsv_newest);
            hrsv_attractive = rootView.findViewById(id.hrsv_attractive);
            hrsv_tagged = rootView.findViewById(id.hrsv_tagged);
            PostModel i = new PostModel();
            i.setTitle("تست 1");
            i.setDate("دو دقیقه قبل");
            Application.getInstance().vip_feed.add(i);
            i = new PostModel();
            i.setTitle("تست 2");
            i.setDate("نیم ساعت قبل");
            Application.getInstance().vip_feed.add(i);
            full(hrsv_vip, Application.getInstance().vip_feed);


            pb1 = rootView.findViewById(id.pb1);
            pb2 = rootView.findViewById(id.pb2);
            pb3 = rootView.findViewById(id.pb3);

            final CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.toolbar_layout);
            //collapsingToolbarLayout.setBackgroundResource(R.drawable.a2);

            final Toolbar t = rootView.findViewById(id.toolbar);
            appBar = rootView.findViewById(id.app_bar);
            float heightDp = (float) (getResources().getDisplayMetrics().heightPixels / 2.5);
        }
        return rootView;
    }

    private void GetSlideShowItems() {
        Call<List<SlideShowModel>> call =
                ApiClient.getClient().create(ApiInterface.class).GetSlideShowItems();
        call.enqueue(new Callback<List<SlideShowModel>>() {
            @Override
            public void onResponse(Call<List<SlideShowModel>> call, retrofit2.Response<List<SlideShowModel>> response) {
                if (!response.equals("[]")) {
                    for (SlideShowModel m : response.body()) {
                        try {
                            final RadioButton rd = new RadioButton(getActivity());
                            rd.setButtonDrawable(R.drawable.rdbtnselector);
                            rd.setPadding(0, 0, 0, 5);
                            rd.setId(Integer.parseInt(m.getId()));
                            rprms = new RadioGroup.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                            RgIndicator.addView(rd, rprms);
                        } catch (Exception e) {
                        }
                    }
                    pagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager(), response.body());
                    pagerAdapter.notifyDataSetChanged();
                    pager.setAdapter(pagerAdapter);
                } else {
                    CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    appBar.setLayoutParams(lp);
                }
            }

            @Override
            public void onFailure(Call<List<SlideShowModel>> call, Throwable t) {

            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        List<SlideShowModel> feed;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<SlideShowModel> feed) {
            super(fm);
            this.feed = feed;
        }

        @Override
        public Fragment getItem(int position) {
            MainSlideShowFragment fragment = new MainSlideShowFragment();

            fragment.setAsset(feed.get(position).getId() + "///" + feed.get(position).getImage());
            return fragment;
        }

        @Override
        public int getCount() {
            return feed.size();
        }
    }

    private void full(final LinearLayout hrsv, final List<PostModel> feed) {
        try {
            for (scrollviewposition = 1; scrollviewposition >= 0; scrollviewposition--) {
//feed.size() - 1

                LayoutInflater inflater = (LayoutInflater)
                        getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                final View view1 = inflater.inflate(R.layout.item_fragment_main_content, null);
                view1.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                view1.setPadding(6,0,6,0);
                TextView txt_title = view1.findViewById(R.id.txt_title);
                TextView txt_date = view1.findViewById(id.txt_date);
                ImageView img_post = view1.findViewById(id.img_post);

                txt_title.setText(feed.get(scrollviewposition).getTitle());
                txt_date.setText(feed.get(scrollviewposition).getDate());

                final ProgressBar p = view1.findViewById(R.id.PrgrsBar);
                //image.setBackgroundResource(R.anim.rounded);
                view1.setTag(scrollviewposition);
                view1.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {

                                             }
                                         }
                );
                hrsv.addView(view1);
            }


        } catch (Exception e) {
        }

    }


}
