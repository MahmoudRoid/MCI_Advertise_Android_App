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

import com.orm.query.Select;

import java.util.List;

import ir.mahmoud.app.Activities.VideoDetailActivity;
import ir.mahmoud.app.Adapters.MarkedAdapter;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.RecyclerItemClickListener;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;

public class MarkedFragment extends Fragment {

    List<tbl_PostModel> list;

    ProgressBar pb;

    RecyclerView rv;
    MarkedAdapter adapter;
    View rootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_marked, container, false);

        rv = rootView.findViewById(R.id.rv);
        pb = rootView.findViewById(R.id.pb);

        list = Select.from(tbl_PostModel.class).list();
        if (list.size() > 0) {
            // show list
            showList(list);
        } else {
            HSH.showtoast(getActivity(), "موردی یافت نشد");
            pb.setVisibility(View.GONE);
        }

        return rootView;
    }

    private void showList(final List<tbl_PostModel> list) {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        adapter = new MarkedAdapter(getActivity(), list);
        rv.setAdapter(adapter);
        pb.setVisibility(View.GONE);
        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                intent.putExtra("feedItem", convertToModel(list.get(position)));
                startActivity(intent);
            }
        }));
    }

    public tbl_PostModel convertToModel(tbl_PostModel tbl) {
        tbl_PostModel model = new tbl_PostModel();
        model.postid = tbl.getPostid();
        model.title = tbl.getTitle();
        model.content = tbl.getContent();
        model.date = tbl.getDate();
        model.categorytitle = tbl.getCategorytitle();
        model.videourl = tbl.getVideourl();
        model.imageurl = tbl.getImageurl();
        model.tagslug = tbl.getTagslug();
        return model;
    }

}
