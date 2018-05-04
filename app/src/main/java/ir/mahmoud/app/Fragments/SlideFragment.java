package ir.mahmoud.app.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ir.mahmoud.app.R;

/**
 * Created by soheilsystem on 5/3/2018.
 */

public class SlideFragment extends Fragment {

    public int asset;
    public ImageView imageView;

    public void setAsset(int asset) {
        this.asset = asset;
    }

@Nullable
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


    View rootView = inflater.inflate(R.layout.slide, container, false);

        imageView = rootView.findViewById(R.id.img);
        imageView.setImageResource(asset);


    return rootView;

}


}
