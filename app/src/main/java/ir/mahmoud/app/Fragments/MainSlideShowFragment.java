/*
Copyright 2014 David Morrissey

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package ir.mahmoud.app.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import ir.mahmoud.app.Application;
import ir.mahmoud.app.R;
import ir.mahmoud.app.R.layout;

public class MainSlideShowFragment extends Fragment {

    private static final String BUNDLE_ASSET = "asset";
    public static ImageView imageView;
    public static ImageView i;
    String[] temp;
    private String asset;

    public MainSlideShowFragment() {
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(layout.slideshow, container, false);

        if (savedInstanceState != null) {
            if (asset == null && savedInstanceState.containsKey(BUNDLE_ASSET)) {
                asset = savedInstanceState.getString(BUNDLE_ASSET);
            }
        }
        if (asset != null) {
            temp = asset.split("///");

            imageView = (ImageView) rootView.findViewById(R.id.imgView);
            final ProgressBar p = (ProgressBar) rootView.findViewById(R.id.PrgrsBar);

            try {

                Application.imageLoader.displayImage(temp[1], imageView, new ImageLoadingListener() {
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
            } catch (Exception e) {
            }
        }

        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        View rootView = getView();
        if (rootView != null) {
            outState.putString(BUNDLE_ASSET, asset);
        }
    }
}
