package com.example.user.slapsell;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpw extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pw);
        final EditText editText = findViewById(R.id.email_reset);
        final ProgressBar progressBar=findViewById(R.id.progress_reset);
        progressBar.setVisibility(View.GONE);
        findViewById(R.id.reset_pw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().sendPasswordResetEmail(editText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    (Toast.makeText(forgotpw.this, "Reset password by email", Toast.LENGTH_SHORT)).show();
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                        (Toast.makeText(forgotpw.this, "Failed to sent check email address", Toast.LENGTH_SHORT)).show();
                    }
                });
            }
        });
    }
}
