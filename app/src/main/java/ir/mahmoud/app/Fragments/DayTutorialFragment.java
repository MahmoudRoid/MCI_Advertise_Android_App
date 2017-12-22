package ir.mahmoud.app.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.mahmoud.app.Adapters.ListAdapter;
import ir.mahmoud.app.Adapters.SameVideoAdapter;
import ir.mahmoud.app.Asynktask.getListPostsAsynkTask;
import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.ExpandableTextView;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Interfaces.ApiClient;
import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Interfaces.IWerbService;
import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DayTutorialFragment extends Fragment {

    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.rv)
    RecyclerView rv;
    ListAdapter adapter;
    private List<PostModel> feed = new ArrayList<>();
    IWebService2 m;
    View rootView = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_day_tutorial, container, false);
        ButterKnife.bind(getActivity());
        rv = rootView.findViewById(R.id.rv);
        pb = rootView.findViewById(R.id.pb);
        m = new IWebService2() {
            @Override
            public void getResult(Object items)throws Exception {
               feed = (List<PostModel>) items;
               adapter.notifyDataSetChanged();
               pb.setVisibility(View.GONE);
            }
            @Override
            public void getError(String ErrorCodeTitle) throws Exception {
            }
        };

        setAdapter();
        getListPostsAsynkTask a = new getListPostsAsynkTask(getActivity(), feed ,m , Application.getInstance().videoType);
        a.getListPosts();
        return rootView;
    }

    private void setAdapter() {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        adapter = new ListAdapter(getActivity(),feed, pb);
        rv.setAdapter(adapter);
    }
}
