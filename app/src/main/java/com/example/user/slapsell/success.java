package com.example.user.slapsell;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class success extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sucess_delete);
        View view = findViewById(R.id.successimage);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.enlarging);
        view.startAnimation(anim);
    }

}

