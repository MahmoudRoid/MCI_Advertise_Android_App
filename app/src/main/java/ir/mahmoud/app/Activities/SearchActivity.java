package ir.mahmoud.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.mahmoud.app.Adapters.SearchAdapter;
import ir.mahmoud.app.Asynktask.SearchVideos;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Classes.RecyclerItemClickListener;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchActivity extends AppCompatActivity implements IWebService2 {

    String searchString;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    SearchAdapter adapter;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.search_edt)
    AppCompatEditText searchEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchString = getIntent().getStringExtra("searchString");
        setTitle("جستجو");
        searchEdt.setText(searchString);
        // call web service
        if(NetworkUtils.getConnectivity(this)){
            SearchVideos getdata = new SearchVideos(this, this, searchString);
            getdata.getData();
        }
        else HSH.showtoast(this,getString(R.string.error_internet));
    }

    @Override
    public void getResult(Object result) throws Exception {
        pb.setVisibility(View.INVISIBLE);
        if (result instanceof String) {
            if (result.equals("empty list"))
                HSH.showtoast(this, "نتیجه ای نداریم");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            showList((List<tbl_PostModel>) result);
        }
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        pb.setVisibility(View.INVISIBLE);
        HSH.showtoast(this, "مشکلی پیش آمده است");
    }

    private void showList(final List<tbl_PostModel> searchList) {
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        adapter = new SearchAdapter(this, searchList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this, VideoDetailActivity.class);
                intent.putExtra("feedItem", searchList.get(position));
                startActivity(intent);
            }
        }));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.search_icon)
    public void onViewClicked() {
        if (searchEdt.getText().toString().trim().length() > 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.VISIBLE);
            if(NetworkUtils.getConnectivity(this)){
                SearchVideos getdata = new SearchVideos(this, this, searchEdt.getText().toString().trim());
                getdata.getData();
            }
            else HSH.showtoast(this,getString(R.string.error_internet));
        }
    }
}
