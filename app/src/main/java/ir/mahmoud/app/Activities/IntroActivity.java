package ir.mahmoud.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.mahmoud.app.Adapters.IntroSlideShowAdapter;
import ir.mahmoud.app.Fragments.IntroSlideFragment;
import ir.mahmoud.app.R;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.forwardImage)
    ImageView forwardImage;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.backImage)
    ImageView backImage;

    private RadioGroup.LayoutParams rprms;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                pager.setCurrentItem(checkedId);
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == 0){
                    backImage.setVisibility(View.INVISIBLE);
                }

                else if(position > 0 && position != radioGroup.getChildCount()-1){
                    forwardImage.setImageResource(R.drawable.ic_arrow_forward);
                    backImage.setVisibility(View.VISIBLE);
                }

                else if(position==radioGroup.getChildCount()-1){
                    forwardImage.setImageResource(R.drawable.ic_done);
                    backImage.setVisibility(View.VISIBLE);
                }


                radioGroup.check(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bindImges();
    }


    private void bindImges() {
        pager.removeAllViews();
        radioGroup.removeAllViews();

        int[] myImageList = new int[]{R.drawable.m1,
                R.drawable.m2,
                R.drawable.m3};

        if (radioGroup.getChildCount() == 0)
            for (int i = 0; i < 3; i++) {
                try {
                    final RadioButton rd = new RadioButton(this);
                    rd.setButtonDrawable(R.drawable.rdbtnselector_intro);
                    rd.setPadding(0, 0, 5, 5);
                    rd.setId(i);
                    rprms = new RadioGroup.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    radioGroup.addView(rd, rprms);

                    pagerAdapter = null;
                    pagerAdapter = new IntroSlideShowAdapter(getSupportFragmentManager(),myImageList);
                    pager.setAdapter(pagerAdapter);


                } catch (Exception e) {
                }
            }
        radioGroup.check(0);

    }

    @OnClick({R.id.forwardImage, R.id.backImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forwardImage:
                if(pager.getCurrentItem() == radioGroup.getChildCount()-1){
                    // go to next activity
                    startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
                else
                    pager.setCurrentItem(pager.getCurrentItem()+1);
                break;
            case R.id.backImage:
                pager.setCurrentItem(pager.getCurrentItem()-1);
                break;
        }
    }

}
