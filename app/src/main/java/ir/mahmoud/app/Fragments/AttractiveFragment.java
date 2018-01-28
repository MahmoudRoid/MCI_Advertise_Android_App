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
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Interfaces.OnLoadMoreListener;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;

public class AttractiveFragment extends Fragment {

    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.rv)
    RecyclerView rv;
    ListAdapter adapter;
    IWebService2 m;
    View rootView = null;
    private List<tbl_PostModel> feed = new ArrayList<>();
    private List<tbl_PostModel> attract_feed = new ArrayList<>();
    private int InitPage = 1;

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
                if (InitPage == 1) {
                    attract_feed.addAll((List<tbl_PostModel>) items);
                    ((List<tbl_PostModel>) items).clear();
                    Application.getInstance().vip_feed_list.addAll(attract_feed);
                    feed.clear();
                    feed.addAll(attract_feed);
                    adapter.notifyDataSetChanged();
                } else if (InitPage > 1) {
                    feed.remove(feed.size() - 1);
                    adapter.notifyItemRemoved(feed.size());
                    attract_feed.addAll((List<tbl_PostModel>) items);
                    feed.clear();
                    feed.addAll(attract_feed);
                    //((List<tbl_PostModel>) items).clear();
                    adapter.notifyDataSetChanged();
                    adapter.setLoaded();
                    //Application.getInstance().vip_feed_list.clear();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void getError(String ErrorCodeTitle) throws Exception {
            }
        };

        setAdapter();

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                InitPage++;
                if (InitPage <= Application.getInstance().getVipFinalPage()) {
                    feed.add(null);
                    adapter.notifyItemInserted(feed.size() - 1);
                    // get data
                    if (NetworkUtils.getConnectivity(getActivity())) {
                        getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity(), /*Application.getInstance().vip_feed_list,*/ m, Application.getInstance().videoType, InitPage);
                        a.getListPosts();
                    } else HSH.showtoast(getActivity(), getString(R.string.error_internet));
                } else {
                }
            }
        });
        if (attract_feed.size() > 1) {
            //vip_feed.clear();
            //vip_feed.addAll(Application.getInstance().vip_feed_list);
            feed.clear();
            feed.addAll(attract_feed);
            adapter.notifyDataSetChanged();
            pb.setVisibility(View.GONE);
        } else {
            if (NetworkUtils.getConnectivity(getActivity())) {
                getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity()/*, Application.getInstance().vip_feed_list*/, m, Application.getInstance().videoType, InitPage);
                a.getListPosts();
            } else HSH.showtoast(getActivity(), getString(R.string.error_internet));
        }
        return rootView;
    }

    private void setAdapter() {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        adapter = new ListAdapter(getActivity(), feed, pb, rv);
        rv.setAdapter(adapter);
    }
}
