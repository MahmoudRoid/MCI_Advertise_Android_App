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
import ir.mahmoud.app.Activities.SearchActivity;
import ir.mahmoud.app.Adapters.DownloadAdapter;
import ir.mahmoud.app.Adapters.ListAdapter;
import ir.mahmoud.app.Adapters.NewestAdapter;
import ir.mahmoud.app.Asynktask.SearchVideos;
import ir.mahmoud.app.Asynktask.getListPostsAsynkTask;
import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Classes.NetworkUtils;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Interfaces.OnLoadMoreListener;
import ir.mahmoud.app.Models.tbl_Download;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;

public class VideosFragment extends Fragment {

    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.rv)
    RecyclerView rv;
    ListAdapter adapter;
    NewestAdapter newest_adapter;
    IWebService2 m;
    View rootView = null;
    private List<tbl_PostModel> feed = new ArrayList<>();
    List<tbl_Download> list = new ArrayList<>();
    private int vipInitPage = 1 ;
    private int newestInitPage = 1 ;
    private int attractiveInitPage = 1 ;

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
//                feed.addAll((List<tbl_PostModel>) items);
                if(Application.getInstance().videoType.equals("جدیدترین-ها")) {
                    if(newestInitPage==1){
                        feed.clear();
                        feed.addAll((List<tbl_PostModel>) items);
                        list = tblPostModelToTblDownload((List<tbl_PostModel>) items);
                        Application.getInstance().newest_feed_list.addAll(feed);
                        rv.setAdapter(newest_adapter);
                        newest_adapter.notifyDataSetChanged();

                        newest_adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                newestInitPage++;
                                if(newestInitPage <= Application.getInstance().getNewestFinalPage()){
                                    list.add(null);
                                    newest_adapter.notifyItemInserted(list.size() - 1);
                                    // get data
                                    if (NetworkUtils.getConnectivity(getActivity())) {
                                        getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity(), Application.getInstance().newest_feed_list, m, Application.getInstance().videoType,newestInitPage);
                                        a.getListPosts();
                                    } else HSH.showtoast(getActivity(), getString(R.string.error_internet));
                                }
                                else {}
                            }
                        });
                    }
                    else if(newestInitPage>1){
                        list.remove(list.size() - 1);
                        newest_adapter.notifyItemRemoved(list.size());
                        tblPostModelToTblDownload((List<tbl_PostModel>) items);
//                        list.addAll( tblPostModelToTblDownload((List<tbl_PostModel>) items));
                        //list.addAll((List<tbl_Download>) items);
                        ((List<tbl_Download>) items).clear();
                        newest_adapter.notifyDataSetChanged();
                        newest_adapter.setLoaded();
                        Application.getInstance().newest_feed_list.clear();
                    }
                }


                if(Application.getInstance().videoType.equals("پیشنهاد-ویژه")){
                    if(vipInitPage==1){
                        feed.addAll((List<tbl_PostModel>) items);
                        ((List<tbl_PostModel>) items).clear();
                        Application.getInstance().vip_feed_list.addAll(feed);
                        rv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                vipInitPage++;
                                if(vipInitPage <= Application.getInstance().getVipFinalPage()){
                                    feed.add(null);
                                    adapter.notifyItemInserted(feed.size() - 1);
                                    // get data
                                    if (NetworkUtils.getConnectivity(getActivity())) {
                                        getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity(), Application.getInstance().vip_feed_list, m, Application.getInstance().videoType,vipInitPage);
                                        a.getListPosts();
                                    } else HSH.showtoast(getActivity(), getString(R.string.error_internet));
                                }
                                else {}
                            }
                        });

                    }
                    else if(vipInitPage>1){
                        feed.remove(feed.size() - 1);
                        adapter.notifyItemRemoved(feed.size());
                        feed.addAll( (List<tbl_PostModel>) items);
                        ((List<tbl_PostModel>) items).clear();
                        adapter.notifyDataSetChanged();
                        adapter.setLoaded();
                        Application.getInstance().vip_feed_list.clear();
                    }
                }

                if(Application.getInstance().videoType.equals("جذابترین-ها")){
                    if(attractiveInitPage==1){
                        feed.addAll((List<tbl_PostModel>) items);
                        ((List<tbl_PostModel>) items).clear();
                        Application.getInstance().attractive_feed_list.addAll(feed);
                        rv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                attractiveInitPage++;
                                if(attractiveInitPage <= Application.getInstance().getAttractiveFinalPage()){
                                    feed.add(null);
                                    adapter.notifyItemInserted(feed.size() - 1);
                                    // get data
                                    if (NetworkUtils.getConnectivity(getActivity())) {
                                        getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity(), Application.getInstance().attractive_feed, m, Application.getInstance().videoType,attractiveInitPage);
                                        a.getListPosts();
                                    } else HSH.showtoast(getActivity(), getString(R.string.error_internet));
                                }
                                else {}
                            }
                        });

                    }
                    else if(attractiveInitPage>1){
                        feed.remove(feed.size() - 1);
                        adapter.notifyItemRemoved(feed.size());
                        feed.addAll((List<tbl_PostModel>) items);
                        ((List<tbl_PostModel>) items).clear();
                        adapter.notifyDataSetChanged();
                        adapter.setLoaded();
                        Application.getInstance().attractive_feed_list.clear();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void getError(String ErrorCodeTitle) throws Exception {
            }
        };

        setAdapter();

        if ( Application.getInstance().vip_feed_list.size() > 1 && Application.getInstance().videoType.equals("پیشنهاد-ویژه")) {
            feed.clear();
            feed.addAll(Application.getInstance().vip_feed_list);
            adapter.notifyDataSetChanged();
            pb.setVisibility(View.GONE);
        } else if (Application.getInstance().videoType.equals("پیشنهاد-ویژه")) {
            if (NetworkUtils.getConnectivity(getActivity())) {
                getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity(), Application.getInstance().vip_feed_list, m, Application.getInstance().videoType,vipInitPage);
                a.getListPosts();
            } else HSH.showtoast(getActivity(), getString(R.string.error_internet));
        }
        /////////////////////////////////////////////////////////////////////////////////////////////
        if (Application.getInstance().attractive_feed_list.size() > 1 && Application.getInstance().videoType.equals("جذابترین-ها")) {
            feed.clear();
            feed.addAll(Application.getInstance().attractive_feed_list);
            adapter.notifyDataSetChanged();
            pb.setVisibility(View.GONE);
        } else if (Application.getInstance().videoType.equals("جذابترین-ها")) {
            if (NetworkUtils.getConnectivity(getActivity())) {
                getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity(), Application.getInstance().attractive_feed_list, m, Application.getInstance().videoType,attractiveInitPage);
                a.getListPosts();
            } else HSH.showtoast(getActivity(), getString(R.string.error_internet));
        }
        /////////////////////////////////////////////////////////////////////////////////////////////
        if (Application.getInstance().newest_feed_list.size() > 1 && Application.getInstance().videoType.equals("جدیدترین-ها")) {
            feed.clear();
            feed.addAll(Application.getInstance().newest_feed_list);
            list = tblPostModelToTblDownload(feed);
            //newest_adapter.notifyDataSetChanged();
            rv.setAdapter(newest_adapter);
            newest_adapter.notifyDataSetChanged();
            pb.setVisibility(View.GONE);
        } else if (Application.getInstance().videoType.equals("جدیدترین-ها")) {
            if (NetworkUtils.getConnectivity(getActivity())) {
                getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity(), Application.getInstance().newest_feed_list, m, Application.getInstance().videoType,newestInitPage);
                a.getListPosts();
            } else HSH.showtoast(getActivity(), getString(R.string.error_internet));
        }
        return rootView;
    }

    private void setAdapter() {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        adapter = new ListAdapter(getActivity(), feed, pb,rv);
        rv.setAdapter(adapter);
        newest_adapter = new NewestAdapter(getActivity(), list, "Newest",rv);
    }

    private List<tbl_Download> tblPostModelToTblDownload(List<tbl_PostModel> feed) {
        for (tbl_PostModel item : feed){
            tbl_Download tbl = new tbl_Download();

            tbl.postid = item.getPostid();
            tbl.title = item.getTitle();
            tbl.content = item.getContent();
            tbl.date = item.getDate();
            tbl.categorytitle = item.getCategorytitle();
            tbl.videourl = item.getVideourl();
            tbl.imageurl = item.getImageurl();
            tbl.tagslug = item.getTagslug();
            tbl.posturl= item.getPosturl();
            list.add(tbl);
        }
        return list;
    }
}
