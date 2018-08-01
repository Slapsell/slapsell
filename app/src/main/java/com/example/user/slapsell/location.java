package com.example.user.slapsell;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;

import com.example.user.slapsell.Adapter.Location_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class location extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Declare Variables
    ListView list;
    Location_adapter adapter;
    SearchView editsearch;
    ArrayList<String> arraylist = new ArrayList<>();
    List<String> lista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        list = (ListView) findViewById(R.id.location_list);
        adapter = new Location_adapter(this, arraylist);
        list.setAdapter(adapter);
        lista=new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Cities").
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                        arraylist.add(documentSnapshot.getId());

                    lista.addAll(arraylist);
                    }
                    adapter.notifyDataSetChanged();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent();
                intent.putExtra("city",arraylist.get(position));
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
        editsearch = (SearchView) findViewById(R.id.location_search);
        editsearch.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        filter(text);
        return false;
    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arraylist.clear();
        if (charText.length() == 0) {
            arraylist.addAll(lista);
        } else {
            for (String wp : lista) {
                if (wp.toLowerCase(Locale.getDefault()).contains(charText)) {
                    arraylist.add(wp);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}