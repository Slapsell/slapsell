package com.example.user.slapsell;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.user.slapsell.Adapter.home_adapter;
import com.example.user.slapsell.pojo_model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class posts extends AppCompatActivity{

    home_adapter mAdapter;
    Products products;
    ArrayList<Products> list;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts);
        Toolbar toolbar=findViewById(R.id.posts_toolbar);
        toolbar.setTitle("Your Posts");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        products = new Products();
        final FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        String id= FirebaseAuth.getInstance().getUid();
        list=new ArrayList<>();
        mAdapter = new home_adapter(list,this);
        firebaseFirestore.collection("users/"+id+"/uploads").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("prashu", "onComplete: ");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String location = document.get("refers").toString();
                        firebaseFirestore.document(location).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException e) {
                                Log.d("prashu", "onEvent: ");
                                if (e != null) {
                                    Log.w("prashu", "Listen failed.", e);
                                    return;
                                }
                                if (snapshot != null && snapshot.exists()) {
                                    Products products = snapshot.toObject(Products.class);
                                    Log.d("prashu", products.getName());
                                    list.add(products);
                                }
                                recyclerView.getRecycledViewPool().clear();
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
                recyclerView.getRecycledViewPool().clear();
                mAdapter.notifyDataSetChanged();
            }
        });
        recyclerView =findViewById(R.id.posts_recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        }
}

