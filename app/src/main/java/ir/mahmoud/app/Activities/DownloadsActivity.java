package ir.mahmoud.app.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.orm.query.Select;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.mahmoud.app.Adapters.DownloadAdapter;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Models.tbl_Download;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DownloadsActivity extends AppCompatActivity {

    List<tbl_Download> tblDownloadList;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    DownloadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if (Select.from(tbl_Download.class).count() > 0) {
            tblDownloadList = Select.from(tbl_Download.class).list();
            showList(tblDownloadList);
        } else
            HSH.showtoast(this, "ویدئویی وجود ندارد");
    }

    private void showList(List<tbl_Download> tblDownloadList) {
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        adapter = new DownloadAdapter(this, tblDownloadList, "Download");
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
