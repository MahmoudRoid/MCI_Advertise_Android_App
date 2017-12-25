package ir.mahmoud.app.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.mahmoud.app.Activities.VideoDetailActivity;
import ir.mahmoud.app.Adapters.MarkedAdapter;
import ir.mahmoud.app.Classes.RecyclerItemClickListener;
import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;

public class MarkedFragment extends Fragment {

    List<tbl_PostModel> list;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.rv)
    RecyclerView rv;
    MarkedAdapter adapter;
    private List<PostModel> feed = new ArrayList<>();
    View rootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_marked, container, false);
        ButterKnife.bind(getActivity());
        rv = rootView.findViewById(R.id.rv);
        pb = rootView.findViewById(R.id.pb);

        list = Select.from(tbl_PostModel.class).list();
        if(list.size()>0){
            // show list
            showList(list);
        }
        else Toast.makeText(getActivity(), "no record", Toast.LENGTH_SHORT).show();

        return rootView;
    }

    private void showList(final List<tbl_PostModel> list) {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        adapter = new MarkedAdapter(getActivity(), list);
        rv.setAdapter(adapter);

        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
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

}
