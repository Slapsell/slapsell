package com.example.user.slapsell;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.user.slapsell.Fragment.Home;
import com.example.user.slapsell.Fragment.Profile;
import com.example.user.slapsell.Fragment.upload;
import com.example.user.slapsell.pojo_model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Fragment fragment;
    FirebaseAuth auth;
    FirebaseUser user;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new Home();
                    loadFragment(fragment);
                    return true;
                case R.id.Camera:
                    fragment = new upload();
                    execute(fragment);
                    return true;
                case R.id.Profile:
                    fragment = new Profile();
                    execute(fragment);
                    return true;
            }
            return false;
        }
    };

    void execute(Fragment fragment) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this
            );
            alertDialog.setMessage("Login first").setPositiveButton("Login", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }).setNegativeButton("Cancel", null);
            alertDialog.show();
        } else
            loadFragment(fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getPreferences(0);
        editor=sharedPreferences.edit();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference=db.document("users/"+FirebaseAuth.getInstance().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("prashu", "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Users user=snapshot.toObject(Users.class);
                    editor.putString("name",user.getName());
                    editor.putString("mob",user.getMob());
                    editor.putInt("uploads",user.getUploads());
                    editor.putString("city",user.getAddress().getCity());
                    editor.putString("pincode",user.getAddress().getPincode());
                    editor.putString("street",user.getAddress().getStreet());
                    editor.putString("state",user.getAddress().getState());
                    editor.putString("email",user.getEmail());
                    editor.commit();
                } else {
                    Log.d("prashu", "Current data: null");
                }
            }
        });
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        fragment = new Home();
        loadFragment(new Home());
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }
}

