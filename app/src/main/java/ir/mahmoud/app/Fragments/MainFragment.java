package ir.mahmoud.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.mahmoud.app.Activities.VideoDetailActivity;
import ir.mahmoud.app.Asynktask.getPostsAsynkTask;
import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Interfaces.ApiClient;
import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.Interfaces.IWerbService;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;
import ir.mahmoud.app.R.id;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class MainFragment extends Fragment {

    NestedScrollView nest_scrollview;
    int scrollviewposition = 0;
    IWerbService m;
    private View rootView = null;
    private PagerAdapter pagerAdapter;
    private ViewPager pager;
    private RadioGroup RgIndicator;
    private RadioGroup.LayoutParams rprms;
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
            public void getResult(List<tbl_PostModel> items, LinearLayout ll) throws Exception {
                if(ll.getId() == id.hrsv_attractive)
                    getMarkedPost();
                if (items.size() > 0)
                    Binding(ll, items);
                else
                    ((RelativeLayout) ll.getParent()).setVisibility(View.GONE);
            }

            @Override
            public void getError(String ErrorCodeTitle) throws Exception {
            }
        };

        try {
            if (Application.getInstance().vip_feed.size() == 0 ||
                    Application.getInstance().newest_feed.size() == 0 ||
                    Application.getInstance().attractive_feed.size() == 0) {
                if (NetworkUtils.getConnectivity(getActivity())) {
                    getPostsAsynkTask getPosts = new getPostsAsynkTask(getActivity(), m, hrsv_vip, hrsv_newest, hrsv_attractive, hrsv_tagged);
                    getPosts.getData();
                } else HSH.showtoast(getActivity(), getString(R.string.error_internet));

            } else {
                m.getResult(Application.getInstance().vip_feed, hrsv_vip);
                m.getResult(Application.getInstance().newest_feed, hrsv_newest);
                m.getResult(Application.getInstance().attractive_feed, hrsv_attractive);
                getMarkedPost();
            }
        } catch (Exception e) {
        }

        if (Application.getInstance().sl.size() > 0)
            BindSlideShow();
        else {

            if (NetworkUtils.getConnectivity(getActivity())) {
                GetSlideShowItems();
            }
        }
        return rootView;
    }

    private void GetSlideShowItems() {
        Call<ResponseBody> call =
                ApiClient.getClient().create(ApiInterface.class).GetSlideShowItems("اسلایدر");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                try {
                    Application.getInstance().sl.clear();
                    JSONObject obj = new JSONObject(response.body().string());
                    JSONArray jary = new JSONArray(obj.getString(getString(R.string.posts)));
                    for (int i = 0; i < jary.length(); i++) {
                        tbl_PostModel item = new tbl_PostModel();
                        item.setPostid(jary.getJSONObject(i).getLong(getString(R.string.id)));
                        item.setTitle(jary.getJSONObject(i).getString(getString(R.string.title)));
                        item.setContent(jary.getJSONObject(i).getString(getString(R.string.excerpt)));
                        item.setDate(jary.getJSONObject(i).getString(getString(R.string.date)));

                        JSONArray jary2 = new JSONArray(jary.getJSONObject(i).getString(getString(R.string.categories)));
                        item.setCategorytitle(jary2.getJSONObject(0).getString(getString(R.string.title)));

                        try {
                            JSONArray jary3 = new JSONArray(jary.getJSONObject(i).getString(getString(R.string.tags)));
                            item.setTagslug(jary3.getJSONObject(0).getString(getString(R.string.slug)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            jary2 = new JSONArray(jary.getJSONObject(i).getString(getString(R.string.attachments)));
                            item.setVideourl(jary2.getJSONObject(0).getString(getString(R.string.url)));

                            item.setImageurl(jary.getJSONObject(i).getJSONObject(getString(R.string.thumbnail_images)).getJSONObject(getString(R.string.medium)).getString(getString(R.string.url)));
                        } catch (Exception e) {
                        }
                        Application.getInstance().sl.add(item);
                    }
                } catch (Exception e) {
                }
                BindSlideShow();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void BindSlideShow() {
        try {
            appBar.setVisibility(View.VISIBLE);
            pager.removeAllViews();
            RgIndicator.removeAllViews();
            if (Application.getInstance().sl.size() > 0) {
                if (RgIndicator.getChildCount() == 0)
                    for (int i = 0; i < Application.getInstance().sl.size(); i++) {
                        try {
                            final RadioButton rd = new RadioButton(getActivity());
                            rd.setButtonDrawable(R.drawable.rdbtnselector);
                            rd.setPadding(0, 0, 5, 5);
                            rd.setId(i);
                            rprms = new RadioGroup.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                            RgIndicator.addView(rd, rprms);
                        } catch (Exception e) {
                        }
                    }
                RgIndicator.check(0);
                List<tbl_PostModel> feed = new ArrayList<>();
                feed.addAll(Application.getInstance().sl);
                pagerAdapter = null;
                pagerAdapter = new SlideShowPagerAdapter(getActivity().getSupportFragmentManager(), feed);
                pager.setAdapter(pagerAdapter);
            } else {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                appBar.setLayoutParams(lp);
            }
        } catch (Exception e) { }
    }

    private void Binding(final LinearLayout hrsv, final List<tbl_PostModel> feed) {
        try {
            //fake view
            if (feed.size() == 1) {
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
                view1.setPadding(6, 0, 6, 0);
                TextView txt_title = view1.findViewById(R.id.txt_title);
                TextView txt_date = view1.findViewById(id.txt_date);
                ImageView img_post = view1.findViewById(id.img_post);

                txt_title.setText(feed.get(scrollviewposition).getTitle());
                txt_date.setText(feed.get(scrollviewposition).getDate());

                txt_title.setTextSize(14);
                txt_date.setTextSize(11);

                try {
                    Glide.with(getActivity()).load(feed.get(scrollviewposition).getImageurl())
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
            ((RelativeLayout)hrsv.getParent()).setVisibility(View.VISIBLE);
            ((NestedScrollView)((hrsv.getParent()).getParent()).getParent()).setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        } catch (Exception e) {
        }

    }

    private class SlideShowPagerAdapter extends FragmentStatePagerAdapter {
        List<tbl_PostModel> feed;

        public SlideShowPagerAdapter(FragmentManager fm, List<tbl_PostModel> feed) {
            super(fm);
            this.feed = feed;
        }

        @Override
        public Fragment getItem(int position) {
            SlideShowFragment fragment = new SlideShowFragment();
            fragment.setAsset(feed.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return feed.size();
        }
    }

    private void getMarkedPost() {
        List<tbl_PostModel> endList = new ArrayList<>();
        List<tbl_PostModel> tmpList = Select.from(tbl_PostModel.class).list();
        if (tmpList.size() > 1) {
            endList.add(tmpList.get(tmpList.size() - 1));
            endList.add(tmpList.get(tmpList.size() - 2));
        } else if (tmpList.size() == 1)
            endList.add(tmpList.get(0));

        hrsv_tagged.removeAllViews();
        Binding(hrsv_tagged, endList);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(Application.getInstance().sl.size() > 0 )
            getMarkedPost();
    }

}
