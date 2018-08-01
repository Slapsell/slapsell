package com.example.user.slapsell;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.user.slapsell.pojo_model.Address;
import com.example.user.slapsell.pojo_model.Products;
import com.example.user.slapsell.pojo_model.Users;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class product_desc extends AppCompatActivity {

    Users user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_desc);
        Intent intent=getIntent();
        Products products=intent.getParcelableExtra("product");
        ImageView imageView=(ImageView)findViewById(R.id.desc_image);
        TextView name=(TextView)findViewById(R.id.desc_name);
        TextView price=(TextView)findViewById(R.id.desc_price);
        TextView description=(TextView)findViewById(R.id.desc_desc);
        final TextView owner=(TextView)findViewById(R.id.desc_ownername);
        TextView date=(TextView)findViewById(R.id.desc_time);
        date.setText("Posted on "+products.getDate().substring(0,11));
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        final DocumentReference documentReference=db.document("users/"+products.getOwner());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("prashu", "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                   user=snapshot.toObject(Users.class);
                    owner.setText(user.getName());
                    bottomload();
                } else {
                    Log.d("prashu", "Current data: null");

                }
            }
        });
        name.setText(products.getName());
        price.setText("₹"+products.getPrice());
        description.setText(products.getDescription());
        Log.d("prashu", products.getName()+" "+products.getDescription()+" "+products.getPrice());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("image/"+products.getP_id()+"/main.jpg");
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference).placeholder(R.drawable.loading)
                .into(imageView);

    }
    public void bottomload()
    {
        Button viewprofile=(Button)findViewById(R.id.desc_profile);
        View cardView=findViewById(R.id.view);
        TextView n=(TextView)cardView.findViewById(R.id.view_name);
        TextView m=(TextView)cardView.findViewById(R.id.view_mobile);
        TextView a=(TextView)cardView.findViewById(R.id.view_Add);
        TextView e=(TextView)cardView.findViewById(R.id.view_email);
        n.setText(user.getName());
        m.setText("Mobile:-"+user.getMob());
        e.setText(user.getEmail());
        Address address=user.getAddress();
        a.setText(address.getStreet()+","+address.getCity()+","+address.getState()+"("+address.getPincode()+")");
        final BottomSheetBehavior sheetBehavior=BottomSheetBehavior.from(cardView);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        viewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBottomSheet(sheetBehavior);
            }
        });
        sheetBehavior.setPeekHeight(0);
    }
    public void toggleBottomSheet(BottomSheetBehavior sheetBehavior)  {
        if (sheetBehavior.getState()!=BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}
