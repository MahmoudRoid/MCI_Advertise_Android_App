package ir.mahmoud.app.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.mahmoud.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewIdeasFragment extends Fragment {


    public NewIdeasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_ideas, container, false);
    }

}
