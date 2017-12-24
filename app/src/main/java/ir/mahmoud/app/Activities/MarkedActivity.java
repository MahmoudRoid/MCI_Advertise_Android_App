package ir.mahmoud.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.orm.query.Select;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.mahmoud.app.Adapters.MarkedAdapter;
import ir.mahmoud.app.Classes.RecyclerItemClickListener;
import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MarkedActivity extends AppCompatActivity {

    List<tbl_PostModel> list;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    MarkedAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marked);
        ButterKnife.bind(this);

        list = Select.from(tbl_PostModel.class).list();
        if(list.size()>0){
            // show list
            showList(list);
        }
        else Toast.makeText(this, "no record", Toast.LENGTH_SHORT).show();
    }

    private void showList(final List<tbl_PostModel> list) {
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        adapter = new MarkedAdapter(this, list);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MarkedActivity.this, VideoDetailActivity.class);
                intent.putExtra("feedItem",convertToModel(list.get(position)));
                startActivity(intent);
            }
        }));
    }

    public PostModel convertToModel(tbl_PostModel tbl){
        PostModel  model = new PostModel();
        model.id= tbl.getPostid();
        model.title= tbl.getTitle();
        model.content= tbl.getContent();
        model.date= tbl.getDate();
        model.categoryTitle= tbl.getCategorytitle();
        model.videoUrl= tbl.getVideourl();
        model.imageUrl= tbl.getImageurl();
        model.tagSlug= tbl.getTagslug();
        return model;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}