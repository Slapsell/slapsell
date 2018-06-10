package com.example.user.slapsell.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.slapsell.R;

import java.util.ArrayList;

public class home_adapter extends RecyclerView.Adapter<home_adapter.viewHolder> {
    ArrayList arrayList;
    Context c;
    Intent intent;

    @NonNull
    @Override
    public home_adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView itemView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull home_adapter.viewHolder holder, int position) {
        CardView cardView=holder.cardView;
        ImageView imageView=(ImageView)cardView.findViewById(R.id.card_image);
        TextView price=(TextView)cardView.findViewById(R.id.card_price);
        TextView name=(TextView)cardView.findViewById(R.id.card_name);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public static class viewHolder extends RecyclerView.ViewHolder {
        Button edit, remove;
        Context context;
        private CardView cardView;

        public viewHolder(CardView Views) {
            super(Views);
            cardView = Views;
            this.context = Views.getContext();
        }
    }
}
