package com.example.user.slapsell.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.slapsell.R;

public class recycler_items extends Fragment {
    public recycler_items() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.searched, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.frame_recyler);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        return view;
    }
}
