package ir.mahmoud.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ir.mahmoud.app.Activities.VideoDetailActivity;
import ir.mahmoud.app.Asynktask.getPostsAsynkTask;
import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Interfaces.ApiClient;
import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.Interfaces.IWerbService;
import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.Models.SlideShowModel;
import ir.mahmoud.app.R;
import ir.mahmoud.app.R.id;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class MainFragment extends Fragment {

    CollapsingToolbarLayout collapsingToolbarLayout;
    NestedScrollView nest_scrollview;
    int scrollviewposition = 0, currentPage = 0;
    Timer timer;
    IWerbService m;
    View rootView = null;
    private PagerAdapter pagerAdapter;
    private ViewPager pager;
    private RadioGroup RgIndicator;
    private RadioGroup.LayoutParams rprms;
    ;
    private LinearLayout hrsv_vip, hrsv_newest, hrsv_attractive, hrsv_tagged;
    private ProgressBar pb;
    private AppBarLayout appBar;

    private void AssignViews() {
        RgIndicator = rootView.findViewById(R.id.rg_indicator);
        pager = rootView.findViewById(R.id.pager);
        hrsv_vip = rootView.findViewById(id.hrsv_vip);
        hrsv_newest = rootView.findViewById(id.hrsv_newest);
        hrsv_attractive = rootView.findViewById(id.hrsv_attractive);
        hrsv_tagged = rootView.findViewById(id.hrsv_tagged);
        nest_scrollview = rootView.findViewById(R.id.nest_scrollview);
        collapsingToolbarLayout = rootView.findViewById(R.id.toolbar_layout);
        appBar = rootView.findViewById(id.app_bar);
        pb = rootView.findViewById(id.pb);

        RgIndicator.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                pager.setCurrentItem(checkedId);
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                RgIndicator.check(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        AssignViews();

        m = new IWerbService() {
            @Override
            public void getResult(List<PostModel> items, LinearLayout ll) throws Exception {
                Binding(ll, items);
            }

            @Override
            public void getError(String ErrorCodeTitle) throws Exception {
            }
        };
        GetSlideShowItems();
        final CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.toolbar_layout);
        //collapsingToolbarLayout.setBackgroundResource(R.drawable.a2);
        if (Application.getInstance().vip_feed.size() == 0 ||
                Application.getInstance().newest_feed.size() == 0 ||
                Application.getInstance().attractive_feed.size() == 0) {

            getPostsAsynkTask getPosts = new getPostsAsynkTask(getActivity(), m, hrsv_vip, hrsv_newest, hrsv_attractive, hrsv_tagged);
            getPosts.getData();
        } else {
            try {
                m.getResult(Application.getInstance().vip_feed, hrsv_vip);
                m.getResult(Application.getInstance().newest_feed, hrsv_newest);
                m.getResult(Application.getInstance().attractive_feed, hrsv_attractive);
                m.getResult(Application.getInstance().tagged_feed, hrsv_tagged);
            } catch (Exception e) {
            }
        }
        float heightDp = (float) (getResources().getDisplayMetrics().heightPixels / 2.5);
        return rootView;
    }

    private void GetSlideShowItems() {
        Call<ResponseBody> call =
                ApiClient.getClient().create(ApiInterface.class).GetSlideShowItems("اسلایدر");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                appBar.setVisibility(View.VISIBLE);
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    JSONArray jary = new JSONArray(obj.getString(getString(R.string.posts)));
                    for (int i = 0; i < jary.length(); i++) {
                        try {
                            SlideShowModel item = new SlideShowModel();
                            item.setId(jary.getJSONObject(i).getInt(getString(R.string.id)));
                            item.setImage(jary.getJSONObject(i).getJSONObject(getString(R.string.thumbnail_images)).getJSONObject(getString(R.string.thumbnail)).getString(getString(R.string.url)));
                            Application.getInstance().sl.add(item);
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e) {
                }
                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == Application.getInstance().sl.size()) {
                            currentPage = 0;
                        }
                        pager.setCurrentItem(currentPage++, true);
                    }
                };
                timer = new Timer();
                timer.schedule(new TimerTask() { // task to be scheduled

                    @Override
                    public void run() {
                        handler.post(Update);

                    }
                }, 1000, 3000);
                if (!response.equals("[]")) {
                    int i = 0;
                    for (SlideShowModel m : Application.getInstance().sl) {
                        try {
                            final RadioButton rd = new RadioButton(getActivity());
                            rd.setButtonDrawable(R.drawable.rdbtnselector);
                            rd.setPadding(0, 0, 5, 5);
                            rd.setId(i);
                            rprms = new RadioGroup.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                            RgIndicator.addView(rd, rprms);
                            i++;
                        } catch (Exception e) {
                        }
                    }
                    RgIndicator.check(0);
                    pagerAdapter = new SlideShowPagerAdapter(getActivity().getSupportFragmentManager(), Application.getInstance().sl);
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void Binding(final LinearLayout hrsv, final List<PostModel> feed) {
        try {
            if (feed.size() == 1) {
                LayoutInflater inflater = (LayoutInflater)
                        getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                final View view1 = inflater.inflate(R.layout.item_fragment_main_content, null);
                view1.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
                hrsv.addView(view1);
            }
            for (scrollviewposition = 1; scrollviewposition >= 0; scrollviewposition--) {
                LayoutInflater inflater = (LayoutInflater)
                        getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                final View view1 = inflater.inflate(R.layout.item_fragment_main_content, null);
                view1.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
                view1.setPadding(6, 0, 6, 0);
                TextView txt_title = view1.findViewById(R.id.txt_title);
                TextView txt_date = view1.findViewById(id.txt_date);
                ImageView img_post = view1.findViewById(id.img_post);

                txt_title.setText(feed.get(scrollviewposition).getTitle());
                txt_date.setText(feed.get(scrollviewposition).getDate());
                try {
                    Glide.with(getActivity()).load(feed.get(scrollviewposition).getImageUrl())
                            .into(img_post);
                } catch (Exception e) {
                }
                view1.setTag(scrollviewposition);
                view1.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 Intent intent;
                                                 intent = new Intent(getActivity(), VideoDetailActivity.class);
                                                 intent.putExtra("feedItem", feed.get(Integer.parseInt(view1.getTag().toString())));
                                                 startActivity(intent);
                                             }
                                         }
                );
                hrsv.addView(view1);
            }
            nest_scrollview.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        } catch (Exception e) {
        }

    }

    private class SlideShowPagerAdapter extends FragmentStatePagerAdapter {
        List<SlideShowModel> feed;

        public SlideShowPagerAdapter(FragmentManager fm, List<SlideShowModel> feed) {
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
}
