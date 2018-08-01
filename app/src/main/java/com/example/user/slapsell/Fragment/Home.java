package com.example.user.slapsell.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.user.slapsell.Adapter.home_adapter;
import com.example.user.slapsell.LoginActivity;
import com.example.user.slapsell.R;
import com.example.user.slapsell.location;
import com.example.user.slapsell.pojo_model.Products;
import com.example.user.slapsell.posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
//Home class is of fragment type which contains the main funtionality of the app
//Recycler View which shows all tha products according to the city and filters
public class Home extends Fragment {

    home_adapter mAdapter;           //adapter
    RecyclerView recyclerView;
    List<Products> LProducts;
    Button location,filter;
    FirebaseFirestore firebaseFirestore;
    static View view;
    int mi,ma,t;
    ProgressBar progressBar;
    Dialog dialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public Home() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);  //inflate the view with layout
        progressBar=view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        filter=view.findViewById(R.id.filter);
        LProducts = new ArrayList<>();
        setFilterbg();
        firebaseFirestore=FirebaseFirestore.getInstance();
        mAdapter = new home_adapter(LProducts,getActivity());
        fillrecycler(view);
        view.findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                String email;
                if(user!=null)
                    email=user.getEmail();
                else
                    email="null";
                if (email.equals("null")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity()
                    );
                    alertDialog.setMessage("Login first").setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    }).setNegativeButton("Cancel", null);
                    alertDialog.show();
                } else
                startActivity(new Intent(getActivity(),posts.class));
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("prashu", "onClick: ");
                dialog= new Dialog(getActivity());
                dialog.setContentView(R.layout.filter);
                final CrystalRangeSeekbar seekbar=dialog.findViewById(R.id.rangeSeekbar);
                final Spinner spinner=dialog.findViewById(R.id.filter_spinner);
                spinner.setSelection(Integer.valueOf(t));
                final EditText mini=dialog.findViewById(R.id.filter_min);
                mini.setText(String.valueOf(mi));
                final EditText maxi=dialog.findViewById(R.id.filter_max);
                maxi.setText(String.valueOf(ma));
                final Number[] maxv = new Number[1];
                final Number[] minv = new Number[1];
                seekbar.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        maxv[0] =seekbar.getSelectedMaxValue();
                        minv[0] =seekbar.getSelectedMinValue();
                        mini.setText(String.valueOf(minv[0]));
                        maxi.setText(String.valueOf(maxv[0]));
                        return false;
                    }
                });
                dialog.findViewById(R.id.filter_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor=sharedPreferences.edit();
                        editor.putInt("filter.type",spinner.getSelectedItemPosition());
                        editor.putInt("filter.min",Integer.valueOf(mini.getText().toString()));
                        editor.putInt("filter.max",Integer.valueOf(maxi.getText().toString()));
                        editor.commit();
                        setFilterbg();
                        fillrecycler(Home.view);
                        dialog.dismiss();
                    }

                });
                dialog.findViewById(R.id.filter_clear).
                        setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   mini.setText("0");
                                                   maxi.setText("10000");
                                                   spinner.setSelection(0);
                                                   editor=sharedPreferences.edit();
                                                   editor.putInt("filter.type", 0);
                                                   editor.putInt("filter.min", 0);
                                                   editor.putInt("filter.max", 10000);
                                                   editor.commit();
                                                   setFilterbg();
                                               }
                                           });
                dialog.show();

            }
        });
        location=view.findViewById(R.id.home_location);
        location.setText(getActivity().getPreferences(0).getString("filter.city","Location"));
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(),location.class),1);
            }
        });
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
                super.getActivity().onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result;
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                result=data.getStringExtra("city");
            }
            else {
                result="Location";
            }
            sharedPreferences=getActivity().getPreferences(0);
            editor=sharedPreferences.edit();
            editor.putString("filter.city",result);
            editor.commit();
            location.setText(result);
            fillrecycler(view);

        }
    }
    void fillrecycler(View view) {
        LProducts.clear();
        final String location=sharedPreferences.getString("filter.city","Location");
        final int min=sharedPreferences.getInt("filter.min",0);
        final int max=sharedPreferences.getInt("filter.max",10000);
        int t = sharedPreferences.getInt("filter.type", 0);
        String type = getActivity().getResources().getStringArray(R.array.type)[t];
        Query reference;
        if (location.equals("Location")) {
            if (type.equals("----Type----")) {
                reference = firebaseFirestore.collection("Products").whereGreaterThan("price", min).whereLessThan("price", max);
            } else {
                reference = firebaseFirestore.collection("Products")
                        .whereEqualTo("type", type).whereGreaterThan("price", min).whereLessThan("price", max);
            }
        }
        else {
            if (type.equals("----Type----")) {
                reference = firebaseFirestore.collection("Products").whereGreaterThan("price", min).whereLessThan("price", max).whereEqualTo("Location",location);
            } else {
                reference = firebaseFirestore.collection("Products")
                        .whereEqualTo("type", type).whereGreaterThan("price", min).whereLessThan("price", max).whereEqualTo("Location",location);
            }
        }
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    LProducts.clear();
                    Log.d("prashu","inner"+String.valueOf(LProducts.size()));
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Products products = document.toObject(Products.class);
                        Log.d("prashu",products.getName());
                        LProducts.add(products);
                    }
                    Log.d("prashu","inner2"+String.valueOf(LProducts.size()));
                }
                progressBar.setVisibility(View.GONE);
                recyclerView.getRecycledViewPool().clear();
                mAdapter.notifyDataSetChanged();
            }
        });
            recyclerView =view.findViewById(R.id.home_recle);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
    }
    void setFilterbg()
    {
        sharedPreferences=getActivity().getPreferences(0);
        mi=sharedPreferences.getInt("filter.min",0);
        ma=sharedPreferences.getInt("filter.max",10000);
        t=sharedPreferences.getInt("filter.type",0);
        if(!(mi==0&&ma==10000&&t==0))
            filter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.boundaries));
        else
            filter.setBackgroundResource(0);

    }

}
