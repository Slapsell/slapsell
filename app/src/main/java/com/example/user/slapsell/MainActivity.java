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
import android.widget.TextView;
import com.example.user.slapsell.Fragment.Home;
import com.example.user.slapsell.Fragment.Profile;
import com.example.user.slapsell.Fragment.upload;
import com.example.user.slapsell.pojo_model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Fragment fragment,currentFragment;
    FirebaseAuth auth;
    FirebaseUser user;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(currentFragment instanceof Home)
                        return true;
                    fragment = new Home();
                    loadFragment(fragment);
                    return true;
                case R.id.Camera:
                    if(currentFragment instanceof upload)
                        return true;
                    fragment = new upload();
                    execute(fragment);
                    return true;
                case R.id.Profile:
                    if(currentFragment instanceof Profile)
                        return true;
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
        setTheme(R.style.splashScreenTheme);
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
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Users user=snapshot.toObject(Users.class);
                    editor.putString("name",user.getName());
                    editor.putString("mob",user.getMob());
                    editor.putString("city",user.getAddress().getCity());
                    editor.putString("pincode",user.getAddress().getPincode());
                    editor.putString("street",user.getAddress().getStreet());
                    editor.putString("state",user.getAddress().getState());
                    editor.putString("email",user.getEmail());
                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            String id=task.getResult().getToken();
                            editor.putString("regis_id",id);
                            Log.d("prashu", id);
                        }
                    });
                    editor.commit();
                } else {
                    Log.d("prashu", "Current data: null");
                }
            }
        });
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        fragment = new Home();
        loadFragment(new Home());
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter,R.anim.fragment_exit,R.anim.fragmentpop_enter,R.anim.fragmentpop_exit);
        transaction.replace(R.id.frame_container, fragment);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }
    @Override
    public void onBackPressed()
    {
            new AlertDialog.Builder(this).setTitle("Exit Alert").setMessage("Are you sure you want to exit?").setNegativeButton("No",null)
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();}

}

