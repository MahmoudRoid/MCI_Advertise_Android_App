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
import ir.mahmoud.app.Adapters.NewestAdapter;
import ir.mahmoud.app.Asynktask.getListPostsAsynkTask;
import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Interfaces.OnLoadMoreListener;
import ir.mahmoud.app.Models.tbl_Download;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;

public class NewestFragment extends Fragment {

    public static List<tbl_Download> list = new ArrayList<>();
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.rv)
    RecyclerView rv;
    NewestAdapter adapter;
    IWebService2 m;
    View rootView = null;
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
                    list = tblPostModelToTblDownload((List<tbl_PostModel>) items);
                    //Application.getInstance().newest_feed_list.addAll(list);
                    adapter.notifyDataSetChanged();
                } else if (InitPage > 1) {
                    list.remove(list.size() - 1);
                    adapter.notifyItemRemoved(list.size());
                    tblPostModelToTblDownload((List<tbl_PostModel>) items);
                    adapter.notifyDataSetChanged();
                    adapter.setLoaded();
                    //Application.getInstance().newest_feed_list.clear();
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
                    list.add(null);
                    adapter.notifyItemInserted(list.size() - 1);
                    // get data
                    if (NetworkUtils.getConnectivity(getActivity())) {
                        getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity(), /*Application.getInstance().vip_feed_list,*/ m, Application.getInstance().videoType, InitPage);
                        a.getListPosts();
                    } else HSH.showtoast(getActivity(), getString(R.string.error_internet));
                } else {
                }
            }
        });
        if (list.size() > 1) {
            adapter.notifyDataSetChanged();
            pb.setVisibility(View.GONE);
        } else {
            if (NetworkUtils.getConnectivity(getActivity())) {
                getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity(), /*Application.getInstance().newest_feed_list,*/ m, Application.getInstance().videoType, InitPage);
                a.getListPosts();
            } else HSH.showtoast(getActivity(), getString(R.string.error_internet));
        }
        return rootView;
    }

    private void setAdapter() {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        adapter = new NewestAdapter(getActivity(), list, "Newest", rv);
        rv.setAdapter(adapter);
    }


    private List<tbl_Download> tblPostModelToTblDownload(List<tbl_PostModel> feed) {
        for (tbl_PostModel item : feed) {
            tbl_Download tbl = new tbl_Download();

            tbl.postid = item.getPostid();
            tbl.title = item.getTitle();
            tbl.content = item.getContent();
            tbl.date = item.getDate();
            tbl.categorytitle = item.getCategorytitle();
            tbl.videourl = item.getVideourl();
            tbl.imageurl = item.getImageurl();
            tbl.tagslug = item.getTagslug();
            tbl.posturl = item.getPosturl();
            list.add(tbl);
        }
        return list;
    }
}
