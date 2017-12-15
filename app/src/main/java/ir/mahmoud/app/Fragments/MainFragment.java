package ir.mahmoud.app.Fragments;

import android.app.ActionBar;
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

import ir.mahmoud.app.Interfaces.ApiClient;
import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.Models.SlideShowModel;
import ir.mahmoud.app.R;
import ir.mahmoud.app.R.id;
import retrofit2.Call;
import retrofit2.Callback;


public class MainFragment extends Fragment {

    public static PagerAdapter pagerAdapter;
    public static ViewPager pager;
    public static RadioGroup RgIndicator;
    public static RadioGroup.LayoutParams rprms;
    public static ProgressBar pb1, pb2, pb3;
    public static RelativeLayout rl_vip, rl_newest;
    public static LinearLayout hrsv_vip, hrsv_newest;
    public static AppBarLayout appBar;
    int previousState = 0, currentPage = 0, scrollviewposition = 0;
    Timer timer;
    View rootView = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);

            rl_vip = (RelativeLayout) rootView.findViewById(id.rl_vip);
            rl_newest = (RelativeLayout) rootView.findViewById(R.id.rl_newest);
            ////////////////////////////////////////////////////////////
            hrsv_vip = (LinearLayout) rootView.findViewById(id.hrsv_vip);
            hrsv_newest = (LinearLayout) rootView.findViewById(id.hrsv_newest);

            pb1 = (ProgressBar) rootView.findViewById(id.pb1);
            pb2 = (ProgressBar) rootView.findViewById(id.pb2);
            pb3 = (ProgressBar) rootView.findViewById(id.pb3);

            final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);
            //collapsingToolbarLayout.setBackgroundResource(R.drawable.a2);
            final TextView txt_tile_app = (TextView) rootView.findViewById(R.id.txt_tile_app);
            final ImageView img = (ImageView) rootView.findViewById(R.id.img);
            final Toolbar t = (Toolbar) rootView.findViewById(id.toolbar);
            appBar = (AppBarLayout) rootView.findViewById(id.app_bar);
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


    /*private void full(final List<SlideShowModel> feed) {
        try {
            hrsv.setHorizontalFadingEdgeEnabled(true);
            RightAlignedHorizontalScrollView sv = new RightAlignedHorizontalScrollView(getActivity());
            sv.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
            LinearLayout ll = new LinearLayout(getActivity());
            ll.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));

            sv.addView(ll);
            for (scrollviewposition = feed.size() - 1; scrollviewposition >= 0; scrollviewposition--) {


                @SuppressWarnings("static-access")
                LayoutInflater inflater = (LayoutInflater)
                        getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                final View view1 = inflater.inflate(R.layout.horizontal_slideshow, null);

                ll.addView(view1);

                TextView txt_title = (TextView) view1.findViewById(R.id.txt_title);
                TextView txt_view = (TextView) view1.findViewById(R.id.txt_view);

                txt_title.setText(feed.get(scrollviewposition).getTitle());
                txt_view.setText(feed.get(scrollviewposition).getView());

                TextView txt_titr = (TextView) view1.findViewById(R.id.txt_titr);
                txt_titr.setText(feed.get(scrollviewposition).getExpert());

                try {
                    RatingBar rate = (RatingBar) view1.findViewById(R.id.ratingBar3);
                    rate.setRating(Float.parseFloat(feed.get(scrollviewposition).getRate()));
                } catch (NumberFormatException e) {
                }

                final ImageView image = (ImageView) view1.findViewById(R.id.imgView);
                final ProgressBar p = (ProgressBar) view1.findViewById(R.id.PrgrsBar);
                //image.setBackgroundResource(R.anim.rounded);
                view1.setTag(scrollviewposition);
                view1.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 if (!HSH.isNetworkConnection(getActivity())) {
                                                     HSH.showtoast(getActivity(), "اتصال اینترنت برقرار نیست.");
                                                 } else {
                                                     Application.out.setAnimationListener(new Animation.AnimationListener() {
                                                         @Override
                                                         public void onAnimationStart(Animation animation) {
                                                         }

                                                         @Override
                                                         public void onAnimationEnd(Animation animation) {
                                                             final Bundle bundle = new Bundle();
                                                             bundle.putSerializable("feed", feed.get((int) view1.getTag()));
                                                             final Intent intent = new Intent(getActivity(), DoctorDetailsActivity.class);
                                                             intent.putExtras(bundle);
                                                             intent.putExtra("pos", (int) view1.getTag());
                                                             startActivity(intent);
                                                         }

                                                         @Override
                                                         public void onAnimationRepeat(Animation animation) {
                                                         }
                                                     });

                                                     view.startAnimation(Application.in);
                                                     view.startAnimation(Application.out);


                                                 }
                                             }
                                         }
                );
                Application.imageLoader.displayImage(feed.get(scrollviewposition).getImage(), image, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        p.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        p.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
            hrsv.addView(sv);

        } catch (Exception e) {
        }

    }*/
}
