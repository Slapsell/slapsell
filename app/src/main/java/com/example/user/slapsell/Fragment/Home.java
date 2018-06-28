package com.example.user.slapsell.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.slapsell.R;


public class Home extends Fragment {

    public Home() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar=(Toolbar)view.findViewById(R.id.main_toolbar);
        toolbar.setTitle("Slapsell");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        Fragment fragment=new recycler_items();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.frame_recyler, fragment)
                .commit();
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.seachable_menus, menu);
        super.onCreateOptionsMenu(menu,menuInflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_m:
                //start search dialog
                super.getActivity().onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
