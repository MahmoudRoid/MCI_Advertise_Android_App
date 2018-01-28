package ir.mahmoud.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.mahmoud.app.Adapters.SearchAdapter;
import ir.mahmoud.app.Asynktask.SearchVideos;
import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Classes.RecyclerItemClickListener;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Interfaces.OnLoadMoreListener;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

//import com.github.ybq.endless.Endless;

public class SearchActivity extends AppCompatActivity implements IWebService2 {

    String searchString;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    SearchAdapter adapter;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.search_edt)
    AppCompatEditText searchEdt;
    //Endless endless;
    View loadingView;
    List<tbl_PostModel> searchList;
    private int searchInitPage = 1;

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

        loadingView = View.inflate(this, R.layout.layout_loading, null);
        loadingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);

        // call web service
        if (NetworkUtils.getConnectivity(this)) {
            SearchVideos getdata = new SearchVideos(this, this, searchString, searchInitPage);
            getdata.getData();
        } else HSH.showtoast(this, getString(R.string.error_internet));


    }

    @Override
    public void getResult(Object result) throws Exception {
        pb.setVisibility(View.INVISIBLE);
        if (result instanceof String) {
            if (result.equals("empty list"))
                HSH.showtoast(this, "نتیجه ای نداریم");
        } else {
            if (searchInitPage == 1) {
                recyclerView.setVisibility(View.VISIBLE);
                showList((List<tbl_PostModel>) result);
            } else if (searchInitPage > 1) {
                // add to adapter
                searchList.remove(searchList.size() - 1);
                adapter.notifyItemRemoved(searchList.size());
                searchList.addAll((List<tbl_PostModel>) result);
                adapter.notifyDataSetChanged();
                adapter.setLoaded();
            }
        }
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        pb.setVisibility(View.INVISIBLE);
        HSH.showtoast(this, "مشکلی پیش آمده است");
    }

    private void showList(final List<tbl_PostModel> searchList) {
        this.searchList = searchList;
        adapter = new SearchAdapter(this, searchList, recyclerView);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this, VideoDetailActivity.class);
                intent.putExtra("feedItem", searchList.get(position));
                startActivity(intent);
            }
        }));

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                searchInitPage++;
                if (searchInitPage <= Application.getInstance().getSearchFinalPage()) {
                    searchList.add(null);
                    adapter.notifyItemInserted(searchList.size() - 1);
                    // get data
                    if (NetworkUtils.getConnectivity(SearchActivity.this)) {
                        SearchVideos getdata = new SearchVideos(SearchActivity.this, SearchActivity.this, searchString, searchInitPage);
                        getdata.getData();
                    } else HSH.showtoast(SearchActivity.this, getString(R.string.error_internet));
                } else {
                }
            }
        });

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
            if (NetworkUtils.getConnectivity(this)) {
                searchInitPage = 1;
                SearchVideos getdata = new SearchVideos(this, this, searchEdt.getText().toString().trim(), searchInitPage);
                getdata.getData();
            } else HSH.showtoast(this, getString(R.string.error_internet));
        }
    }
}
