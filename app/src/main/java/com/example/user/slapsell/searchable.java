package com.example.user.slapsell;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.slapsell.Adapter.searchAdapter;


public class searchable extends AppCompatActivity {
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searched);
        Intent intent=getIntent();
        Log.d("prashu", intent.getStringExtra(SearchManager.QUERY));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }
}
