package ir.mahmoud.app.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.mahmoud.app.Adapters.SearchAdapter;
import ir.mahmoud.app.Asynktask.SearchVideos;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Models.PostModel;
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
        SearchVideos getdata = new SearchVideos(this, this, searchString);
        getdata.getData();
    }

    @Override
    public void getResult(Object result) throws Exception {
        pb.setVisibility(View.INVISIBLE);
        if (result instanceof String) {
            if (result.equals("empty list"))
                Toast.makeText(this, "نتیجه ای نداریم", Toast.LENGTH_SHORT).show();
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            showList((List<PostModel>) result);
        }
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {
        pb.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "مشکلی پیش آمده است", Toast.LENGTH_SHORT).show();
    }

    private void showList(List<PostModel> searchList) {
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        adapter = new SearchAdapter(this, searchList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.search_icon)
    public void onViewClicked() {
        if(searchEdt.getText().toString().trim().length()>0){
            recyclerView.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.VISIBLE);
            SearchVideos getdata = new SearchVideos(this, this, searchEdt.getText().toString().trim());
            getdata.getData();
        }
    }
}
