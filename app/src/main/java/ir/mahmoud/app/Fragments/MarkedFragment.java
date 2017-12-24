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
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.R;

public class MarkedFragment extends Fragment {

    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.rv)
    RecyclerView rv;
    ListAdapter adapter;
    private List<PostModel> feed = new ArrayList<>();
    View rootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_marked, container, false);
        ButterKnife.bind(getActivity());
        rv = rootView.findViewById(R.id.rv);
        pb = rootView.findViewById(R.id.pb);
        setAdapter();

        return rootView;
    }

    private void setAdapter() {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        adapter = new ListAdapter(getActivity(), feed, pb);
        rv.setAdapter(adapter);
    }
}
