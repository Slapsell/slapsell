package com.example.user.slapsell;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;


import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.slapsell.pojo_model.Users;
import com.google.android.gms.common.util.CrashUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity{

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    String pw, memail;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mAuthListener;
    Button sign_up,back;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialiseAll();
        verify();
        signUp_all();
  }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }
    void verify()
    {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d("prashu", "create ");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("prashu", "onAuthStateChanged: ");
                    sendVerificationEmail();
                } else {
                    Log.d("prashu", "onAuthStateChanged: ");
                }
                // ...
            }
        };
    }
    void initialiseAll()
    {
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.set_password);
        sign_up=(Button)findViewById(R.id.email_sign_up_button);
        auth=FirebaseAuth.getInstance();
        back=(Button)findViewById(R.id.Backtologin);
    }
    void signUp_all()
    {
        sign_up.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            memail = mEmailView.getText().toString();
            pw = mPasswordView.getText().toString();
            Log.d("prashu", pw);
            boolean cancel = false;
            View focusView = null;
            if (!TextUtils.isEmpty(pw) && pw.length()<6) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }
            if (TextUtils.isEmpty(memail)) {
                mEmailView.setError(getString(R.string.error_field_required));
                focusView = mEmailView;
                cancel = true;
            } else if (!isEmailValid(memail)) {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
            } else {
                Log.d("prashu", "else ");
                auth.createUserWithEmailAndPassword(memail, pw).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Verify Email Address",
                                    Toast.LENGTH_SHORT).show();
                            Users use=new Users();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(FirebaseAuth.getInstance().getUid()).set(use).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("prashu", "DocumentSnapshot successfully written!");
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("prashu", "Error writing document", e);
                                        }
                                    });
                            startActivity(new Intent(SignUp.this,LoginActivity.class));
                        finish();
                        }
                        else{
                            Toast.makeText(SignUp.this, "Something went wrong",
                                    Toast.LENGTH_SHORT).show();
                        mPasswordView.setText("");}
                    }
                });
            }
        }
    });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this,LoginActivity.class));
            }
        });
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }
    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(SignUp.this, LoginActivity.class));
                            finish();
                        }
                        else
                        {   overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });

    }
}

