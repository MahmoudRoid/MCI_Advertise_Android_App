package ir.mahmoud.app.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ir.mahmoud.app.Fragments.IntroSlideFragment;

/**
 * Created by soheilsystem on 5/4/2018.
 */

public class IntroSlideShowAdapter extends FragmentStatePagerAdapter {

    public int[] myImageList ;

    public IntroSlideShowAdapter(FragmentManager fm, int[] myImageList) {
        super(fm);
        this.myImageList = myImageList;
    }

    @Override
    public Fragment getItem(int position) {
        IntroSlideFragment fragment = new IntroSlideFragment();
        fragment.setAsset(myImageList[position]);
        return fragment;
    }

    @Override
    public int getCount() {
        return myImageList.length;
    }
}
