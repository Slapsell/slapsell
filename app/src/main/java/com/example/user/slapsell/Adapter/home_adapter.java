package com.example.user.slapsell.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.slapsell.R;
import com.example.user.slapsell.pojo_model.Products;
import com.example.user.slapsell.product_desc;
import com.example.user.slapsell.success;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.List;
//this class is used to create the adapter for filling the database entries to the recycler view
// used by home.java and posts.java
public class home_adapter extends RecyclerView.Adapter<home_adapter.viewHolder> {
    Context context;   //context of the calling class
    private List<Products> list;

    //used to create the view
    @NonNull
    @Override
    public home_adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView itemView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new viewHolder(itemView);
    }
    public home_adapter(List<Products> list,Context context)
    {
        this.context=context;
        this.list=list;
    }
//user to bind the values with holder according to its position
    @Override
    public void onBindViewHolder(@NonNull home_adapter.viewHolder holder, final int position) {
        CardView cardView=holder.cardView;
        final Products recycleHome=list.get(position);
        holder.name.setText(recycleHome.getName());
        holder.price.setText("₹"+String.valueOf(recycleHome.getPrice()));
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("image/"+recycleHome.getP_id()+"/thumbnail.jpg");
        //insert the image
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference).centerCrop().placeholder(R.drawable.loading)
                .into(holder.imageView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,product_desc.class);
                intent.putExtra("product",recycleHome);
                context.startActivity(intent);
            }
        });
        // for deleting the products by long press
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(recycleHome.getOwner().equals(FirebaseAuth.getInstance().getUid()) && context.getClass().getSimpleName().equals("posts"))
                {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete")
                            .setMessage("Do you really want to Delete?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    notifyItemRemoved(position);
                                    FirebaseStorage.getInstance().getReference().child("image/" + recycleHome.getP_id() + "/main.jpg").delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    });
                                    FirebaseStorage.getInstance().getReference().child("image/" + recycleHome.getP_id() + "/thumbnail.jpg").delete();
                                    final DocumentReference documentReference = FirebaseFirestore.getInstance().document("users/" + recycleHome.getOwner() + "/uploads/" + recycleHome.getP_id());
                                    documentReference.get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    try {
                                                        String path = task.getResult().get("refers").toString();
                                                        documentReference.delete();
                                                        FirebaseFirestore.getInstance().document(path).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                (Toast.makeText(context, "Delete Succussfully", Toast.LENGTH_SHORT)).show();
                                                                 context.startActivity(new Intent(context,success.class));
                                                                ((Activity)context).finish();
                                                            }
                                                        })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {

                                                                    }
                                                                });
                                                    } catch (NullPointerException e) {
                                                        (Toast.makeText(context, "This mightn't be your, contact to the operators", Toast.LENGTH_SHORT)).show();
                                                    } catch (RuntimeExecutionException r) {
                                                        (Toast.makeText(context, "check internet connection", Toast.LENGTH_SHORT)).show();
                                                    }
                                                }
                                            });
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder {
        Context context;
        private CardView cardView;
        ImageView imageView;
        TextView price,name;
        private viewHolder(CardView Views) {
            super(Views);
            Log.d("prashu", "holder");
            cardView = Views;
            imageView=cardView.findViewById(R.id.card_image);
            price=cardView.findViewById(R.id.card_price);
            name=cardView.findViewById(R.id.card_name);
            this.context = Views.getContext();
        }
    }
}
