package com.example.user.slapsell.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.slapsell.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class upload extends Fragment {

    Context context;
    ImageView imageView;
    EditText Tname,Tdescription,Tprice;
    String name,description,price,type,city;
    Button post;
    Uri file;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public upload() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        imageView = (ImageView)view.findViewById(R.id.upload_image);
        sharedPreferences=getActivity().getPreferences(0);
        city=sharedPreferences.getString("city","Unknown");
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
        pictureDialog.setTitle("Select Action");
        final Spinner spinner=(Spinner)view.findViewById(R.id.upload_spinner);
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
        post=(Button)view.findViewById(R.id.poost);
        Tname=(EditText)view.findViewById(R.id.upload_name);
        Tdescription=(EditText)view.findViewById(R.id.upload_description);
        Tprice=(EditText)view.findViewById(R.id.upload_price);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=Tname.getText().toString();
                description=Tdescription.getText().toString();
                price=Tprice.getText().toString();
                type=spinner.getSelectedItem().toString();
                String uid=FirebaseAuth.getInstance().getUid();
                Date currentTime = Calendar.getInstance().getTime();
                Map<String,Object> map=new HashMap<>();
                map.put("name",name);
                map.put("description",description);
                map.put("price",price);
                map.put("owner",uid);
                map.put("date",currentTime.toString());
                db.document("Location/"+city+"/"+type+"/"+uid+"_"+name).set(map).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getActivity(),"Upload Failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
             startActivityForResult(galleryIntent, 12);
    }

    public void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            return;
        }
        if (requestCode == 12) {
            if (data != null) {
                file = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), file);
                    Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show();
            imageView.setImageBitmap(thumbnail);

        }

    }
}
