package ir.mahmoud.app.Fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.Timer;

import ir.mahmoud.app.Activities.LoginActivity;
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

    private PagerAdapter pagerAdapter;
    private ViewPager pager;
    private RadioGroup RgIndicator;
    private RadioGroup.LayoutParams rprms;
    private LinearLayout hrsv_vip, hrsv_newest, hrsv_attractive, hrsv_tagged ;
    private ProgressBar pb;
    private AppBarLayout appBar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    NestedScrollView nest_scrollview;
    int previousState = 0, currentPage = 0, scrollviewposition = 0;
    Timer timer;
    IWerbService m;
    View rootView = null;

    private void AssignViews()
    {
        hrsv_vip = rootView.findViewById(id.hrsv_vip);
        hrsv_newest = rootView.findViewById(id.hrsv_newest);
        hrsv_attractive = rootView.findViewById(id.hrsv_attractive);
        hrsv_tagged = rootView.findViewById(id.hrsv_tagged);
        nest_scrollview = rootView.findViewById(R.id.nest_scrollview);
        collapsingToolbarLayout = rootView.findViewById(R.id.toolbar_layout);
        appBar = rootView.findViewById(id.app_bar);
        pb = rootView.findViewById(id.pb);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        AssignViews();

        m = new IWerbService() {
            @Override
            public void getResult(List<PostModel> items, LinearLayout ll)throws Exception {
                Binding(ll, items);
            }
            @Override
            public void getError(String ErrorCodeTitle) throws Exception {
            }
        };
        final CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.toolbar_layout);
        //collapsingToolbarLayout.setBackgroundResource(R.drawable.a2);
        getPostsAsynkTask.getVipVideos(getActivity(), m, hrsv_vip, hrsv_newest , hrsv_attractive ,hrsv_tagged);
        float heightDp = (float) (getResources().getDisplayMetrics().heightPixels / 2.5);
        return rootView;
    }

    private void GetSlideShowItems(final IWerbService m) {
        Call<ResponseBody> call =
                ApiClient.getClient().create(ApiInterface.class).GetSlideShowItems();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                appBar.setVisibility(View.VISIBLE);
               //m.getResult(Application.getInstance().vip_feed);
                if (!response.equals("[]")) {
                   /* for (SlideShowModel m : response.body()) {
                        try {
                            final RadioButton rd = new RadioButton(getActivity());
                            rd.setButtonDrawable(R.drawable.rdbtnselector);
                            rd.setPadding(0, 0, 0, 5);
                            rd.setId(Integer.parseInt(m.getId()));
                            rprms = new RadioGroup.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                            RgIndicator.addView(rd, rprms);
                        } catch (Exception e) {
                        }
                    }*/
                   /* pagerAdapter = new SlideShowPagerAdapter(getActivity().getSupportFragmentManager(), response.body().string());
                    pagerAdapter.notifyDataSetChanged();
                    pager.setAdapter(pagerAdapter);*/
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

    private void Binding(final LinearLayout hrsv, final List<PostModel> feed) {
        try {
            if(feed.size() == 1)
            {
                LayoutInflater inflater = (LayoutInflater)
                        getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                final View view1 = inflater.inflate(R.layout.item_fragment_main_content, null);
                view1.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
                hrsv.addView(view1);
            }
            for (scrollviewposition = feed.size() - 1; scrollviewposition >= 0; scrollviewposition--) {
                LayoutInflater inflater = (LayoutInflater)
                        getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                final View view1 = inflater.inflate(R.layout.item_fragment_main_content, null);
                view1.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
                view1.setPadding(6,0,6,0);
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
                                                 intent = new Intent(getActivity(),VideoDetailActivity.class);
                                                 intent.putExtra("feedItem",  feed.get(Integer.parseInt(view1.getTag().toString())));
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
}
