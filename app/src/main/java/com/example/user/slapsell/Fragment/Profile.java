package com.example.user.slapsell.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.slapsell.LoginActivity;
import android.Manifest;
import com.example.user.slapsell.R;
import com.example.user.slapsell.pojo_model.Address;
import com.example.user.slapsell.pojo_model.Users;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

public class Profile extends Fragment implements LocationListener {
    FirebaseAuth auth;
    FirebaseUser user;
    TextView address,name,phone,email;
    Address add;
    EditText estreet,ecity,estate,epincode;
    String id,tname,street,city,state,pincode;
    String tphone,posts;
    private Location mLocation;
    private LocationManager mLocationManager;
    Dialog dialog1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        id=FirebaseAuth.getInstance().getUid();
        email =(TextView)view.findViewById(R.id.profile_email);
        name =(TextView)view.findViewById(R.id.profile_name);
        phone =(TextView)view.findViewById(R.id.profile_phone);
        address =(TextView)view.findViewById(R.id.Profile_address);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference=db.document("users/"+id);
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
                                          tname=user.getName().toString();
                                          tphone=user.getMob();
                                          add=user.getAddress();
                                          if(tname!=null||tname!="")name.setText(tname);else name.setText("Set Name");
                                          if(tphone!=null)phone.setText(tphone.toString()); else phone.setText("Add Phone");
                                          if(user.getAddress()!=null)address.setText(add.getStreet()+","+add.getCity());
                                          } else {
                                                Log.d("prashu", "Current data: null");
                                                }
                                          }
                                          });
                                           email.setText(user.getEmail());

        name.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        if (event.getAction() == MotionEvent.ACTION_UP) {
                                            if (event.getRawX() >= (name.getRight() - name.getCompoundDrawables()[2].getBounds().width())) {
                                                final Dialog dialog = new Dialog(getActivity());
                                                dialog.setContentView(R.layout.custom_one_dialog);
                                                final EditText name = (EditText) dialog.findViewById(R.id.one_custom);
                                                name.setText(tname);
                                                 Button btn=dialog.findViewById(R.id.custom_name);
                                                 btn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        db.collection("users").document(id).update("name",returnText(name)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("prashu", e.getMessage());
                                                            }
                                                        });
                                                        dialog.dismiss();
                                                    }
                                                });
                                                dialog.show();
                                                return true;
                                            }
                                        }

                                      return false;
                                    }

                                });
        phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (name.getRight() - name.getCompoundDrawables()[2].getBounds().width())) {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.custom_one_dialog);
                        final EditText name = (EditText) dialog.findViewById(R.id.one_custom);
                        name.setHint("Mobile");
                        TextView t=(TextView)dialog.findViewById(R.id.title);
                        t.setText("Set Mobile");
                        name.setText(tphone.toString());
                        Button btn=dialog.findViewById(R.id.custom_name);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                db.collection("users").document(id).update("mobile",returnText(name)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("prashu", e.getMessage());
                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        return true;
                    }
                }

                return false;
            }

        });


        address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (address.getRight() - address.getCompoundDrawables()[2].getBounds().width())) {
                        dialog1=new Dialog(getActivity());
                       dialog1.setContentView(R.layout.custom_dialog);
                        fill(dialog1,add);
                        Button OK = (Button) dialog1.findViewById(R.id.custom_address);
                        OK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                add=new Address(returnText(estreet),returnText(ecity),returnText(estate),returnText(epincode));
                                Map<String,Object> map=new HashMap<>();
                                map.put("address.pincode",add.getPincode());
                                map.put("address.city",add.getCity());
                                map.put("address.state",add.getState());
                                map.put("address.street",add.getStreet());
                                map.put("email",user.getEmail());
                                db.collection("users").document(id).update(map).
                                        addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                           }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("prashu", e.getMessage());
                                    }
                                });
                                dialog1.dismiss();
                            }
                        });
                        Button pick=(Button)dialog1.findViewById(R.id.pick_location);
                        pick.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("prashu", "onClick: ");
                                if(checkLocationPermission())
                                {Log.d("prashu", "per ");
                                    mLocation=getLastKnownLocation();
                                    if(mLocation!=null)
                                    onLocationChanged(mLocation);
                                }
                            }
                        });
                        dialog1.show();
                        return true;
                    }
                }
                return false;
            }
        });
        TextView textView=(TextView)view.findViewById(R.id.logout);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
            }
        });

        return  view;
    }
    String returnText(TextView textView)
    {
        return textView.getText().toString();
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Permission")
                        .setMessage("Permission is required for picking location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<android.location.Address> address = geoCoder.getFromLocation(lat, lng, 1);
                try {
                    Address add=new Address();
                    add.setStreet(address.get(0).getAddressLine(0));
                    add.setCity(address.get(0).getLocality());
                    add.setPincode(address.get(0).getPostalCode());
                    add.setState(address.get(0).getAdminArea());
                    fill(dialog1,add);
                    } catch (Exception ignored) {}


        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
          if(checkLocationPermission()) {
              for (String provider : providers) {
                  Location l = mLocationManager.getLastKnownLocation(provider);
                  if (l == null) {
                      continue;
                  }
                  if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                      // Found best last known location: %s", l);
                      bestLocation = l;
                  }
              }
          }
        return bestLocation;
    }
    public void fill(Dialog dialog,Address address)
    {
        epincode = (EditText) dialog.findViewById(R.id.custom_pin);
        estreet = (EditText) dialog.findViewById(R.id.custom_street);
        ecity = (EditText) dialog.findViewById(R.id.custom_City);
        estate = (EditText) dialog.findViewById(R.id.custom_state);
        street=address.getStreet();
        state=address.getState();
        city=address.getCity();
        pincode=address.getPincode();
        epincode.setText(pincode);
        ecity.setText(city);
        estate.setText(state);
        estreet.setText(street);

    }
}
