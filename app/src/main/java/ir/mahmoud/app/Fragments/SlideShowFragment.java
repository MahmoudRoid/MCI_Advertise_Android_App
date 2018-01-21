package ir.mahmoud.app.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import ir.mahmoud.app.Activities.ShowSliderActivity;
import ir.mahmoud.app.Activities.VideoDetailActivity;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;

public class SlideShowFragment extends Fragment {

    public static ImageView imageView;
    public static ImageView i;
    private tbl_PostModel asset;

    public void setAsset(tbl_PostModel asset) {
        this.asset = asset;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.slideshow, container, false);
        /*if (savedInstanceState != null) {
            if (asset == null && savedInstanceState.containsKey(BUNDLE_ASSET)) {
                asset = savedInstanceState.getString(BUNDLE_ASSET);
            }
        }*/
        if (asset != null) {
            //temp = asset.split("///");
            imageView = rootView.findViewById(R.id.imgView);
            Glide.with(getActivity()).load(asset.getImageurl())
                    .apply(new RequestOptions().placeholder(R.mipmap.shodani)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(NetworkUtils.getConnectivity(getActivity())){
                    Intent intent = new Intent(getActivity(), ShowSliderActivity.class);
                    intent.putExtra("postUrl", asset.getPosturl());
                    intent.putExtra("postTitle", asset.getTitle());
                    startActivity(intent);
                    }
                    else HSH.showtoast(getActivity(),getString(R.string.error_internet));
//                    Intent intent;
//                    intent = new Intent(getActivity(), VideoDetailActivity.class);
//                    intent.putExtra("feedItem", asset);
//                    startActivity(intent);
                }
            });
        }
        return rootView;
    }


    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        View rootView = getView();
        if (rootView != null) {
            outState.putString(BUNDLE_ASSET, asset);
        }
    }*/
}
