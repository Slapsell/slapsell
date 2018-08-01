package com.example.user.slapsell.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.user.slapsell.R;
import com.example.user.slapsell.posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static android.app.Activity.RESULT_OK;
import static android.media.MediaRecorder.VideoSource.CAMERA;

public class upload extends Fragment {

    Context context;
    ImageView imageView;
    EditText Tname,Tdescription,Tprice;
    String name,description,price,type,city,url,uid,p_id;
    Button post;
    int per;
    Bitmap bitmap;
    private SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    public upload() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        imageView = view.findViewById(R.id.upload_image);
        sharedPreferences = getActivity().getPreferences(0);
        city = sharedPreferences.getString("city", "Location");
        final Spinner spinner =  view.findViewById(R.id.upload_spinner);
        selectOption();
        progressBar =  view.findViewById(R.id.upload_progress);
        FloatingActionButton floatingActionButton =  view.findViewById(R.id.upload_float);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption();
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
        post = view.findViewById(R.id.poost);
        Tname =  view.findViewById(R.id.upload_name);
        Tdescription = view.findViewById(R.id.upload_description);
        Tprice =  view.findViewById(R.id.upload_price);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = Tname.getText().toString();
                if(name.length()<1)
                {
                    makeToast("Enter Name of Product");
                    return;
                }
                description = Tdescription.getText().toString();
                if(description.length()<20)
                {
                    makeToast("Add sufficient description");
                    return;
                }
                price = Tprice.getText().toString();
                if(price.length()==0)
                    price="0";
                type = spinner.getSelectedItem().toString();
                if(type.equals("----Type----"))
                {
                    makeToast("Add product Type");
                    return;
                }
                if(bitmap==null)
                {
                    makeToast("Insert Image");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                uid = FirebaseAuth.getInstance().getUid();
                post.setClickable(false);
                uploads();
                Upload_image(bitmap,per,"main");
                Upload_image(bitmap,(int)(per*.1),"thumbnail");

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
        if(checkCameraPermission())
        {Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);}

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            return;
        }
        if (requestCode == 12) {
            if (data != null) {
                Uri file = data.getData();
                Cursor returnCursor =
                        context.getContentResolver().query(file, null, null, null, null);
                int si=returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                si=(int)(returnCursor.getLong(si)/1024);
                if(si>3000)per=20;
                else if(si>1000) per=40;
                else if(si>500) per=60;
                else per=70;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), file);
                    imageView.setImageBitmap(bitmap);
                    Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CAMERA && resultCode== RESULT_OK) {
            if (data != null) {
                try {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    per=1000;
                    Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show();
                    }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Log.d("prashu", "Uri null ");
            }
        }

    }
    public void Upload_image(Bitmap bitmap,int quality,String state) {
        byte[] bytes;
        ByteArrayOutputStream byte_im=new ByteArrayOutputStream();
        if(bitmap==null)
            Log.d("prashu", "Upload_image: ");
        if(quality>100)
            quality=100;
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,byte_im);
        bytes=byte_im.toByteArray();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference Ref = storageRef.child("image/"+p_id+"/"+state+".jpg");
        Ref.putBytes(bytes).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
        }
    public boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Permission")
                        .setMessage("Permission is required for Camera")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.CAMERA},CAMERA
                                        );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA);
            }
            return false;
        } else {
            return true;
        }
    }
    public void selectOption()
    {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
        pictureDialog.setTitle("Select Action");
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
    }
public  void uploads(){
    Date currentTime = Calendar.getInstance().getTime();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    p_id=UUID.randomUUID().toString();
    Map<String,Object> map=new HashMap<>();
    map.put("name",name);
    map.put("description",description);
    map.put("price",Integer.valueOf(price));
    map.put("owner",uid);
    map.put("date",currentTime.toString());
    map.put("type",type);
    map.put("p_id",p_id);
    map.put("Location",city);
    final String location="Products/"+p_id;
    HashMap<String,Object> map1=new HashMap<>();
        db.document("Cities/"+city).set(map1);//empty document
    db.document(location).set(map).
            addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(),"Posted Successfully",Toast.LENGTH_SHORT).show();
                    Map<String,Object> map1=new HashMap<>();
                    map1.put("refers",location);
                    db.document("users/"+uid+"/uploads/"+p_id).set(map1);
                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(getActivity(),"Upload Failed",Toast.LENGTH_SHORT).show();
        }
    }).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            startActivity(new Intent(context, posts.class));
            post.setClickable(true);
            Tname.setText("");
            Tdescription.setText("");
            Tprice.setText("");
            imageView.setImageURI(null);
                    }
    });
}

public void makeToast(String message){
    (Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT)).show();
}
}
