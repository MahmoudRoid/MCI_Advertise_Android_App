package ir.mahmoud.app.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.mahmoud.app.Adapters.ListAdapter;
import ir.mahmoud.app.Asynktask.getListPostsAsynkTask;
import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;

public class VideosFragment extends Fragment {

    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.rv)
    RecyclerView rv;
    ListAdapter adapter;
    IWebService2 m;
    View rootView = null;
    private List<tbl_PostModel> feed = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_videos, container, false);
        ButterKnife.bind(getActivity());
        rv = rootView.findViewById(R.id.rv);
        pb = rootView.findViewById(R.id.pb);
        m = new IWebService2() {
            @Override
            public void getResult(Object items) throws Exception {
                feed.addAll((List<tbl_PostModel>) items);
                adapter.notifyDataSetChanged();
                pb.setVisibility(View.GONE);
            }

            @Override
            public void getError(String ErrorCodeTitle) throws Exception {
            }
        };

        setAdapter();

        if (Application.getInstance().vip_feed_list.size() > 1 && Application.getInstance().videoType.equals("پیشنهاد-ویژه")) {
            feed.addAll(Application.getInstance().vip_feed_list);
            adapter.notifyDataSetChanged();
            pb.setVisibility(View.GONE);
        } else if (Application.getInstance().videoType.equals("پیشنهاد-ویژه")) {
            getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity(), Application.getInstance().vip_feed_list, m, Application.getInstance().videoType);
            a.getListPosts();
        }
        /////////////////////////////////////////////////////////////////////////////////////////////
        if (Application.getInstance().attractive_feed_list.size() > 1 && Application.getInstance().videoType.equals("جذابترین_ها")) {
            feed.addAll(Application.getInstance().attractive_feed_list);
            adapter.notifyDataSetChanged();
            pb.setVisibility(View.GONE);
        } else if (Application.getInstance().videoType.equals("جذابترین_ها")) {
            getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity(), Application.getInstance().attractive_feed_list, m, Application.getInstance().videoType);
            a.getListPosts();
        }
        /////////////////////////////////////////////////////////////////////////////////////////////
        if (Application.getInstance().newest_feed_list.size() > 1 && Application.getInstance().videoType.equals("جدیدترین-ها")) {
            feed.addAll(Application.getInstance().newest_feed_list);
            adapter.notifyDataSetChanged();
            pb.setVisibility(View.GONE);
        } else if (Application.getInstance().videoType.equals("جدیدترین-ها")) {
            getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity(), Application.getInstance().newest_feed_list, m, Application.getInstance().videoType);
            a.getListPosts();
        }
        return rootView;
    }

    private void setAdapter() {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        adapter = new ListAdapter(getActivity(), feed, pb);
        rv.setAdapter(adapter);
    }
}
