package com.example.user.slapsell.Adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.slapsell.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
//this class is used to create the adapter for filling the database entries to the recycler view
// used by location.java
public class Location_adapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    public ArrayList<String> resulted;
    private List<String> locations;

    public Location_adapter(Context context, List<String> locations) {
        mContext = context;
        this.locations=locations;
        inflater = LayoutInflater.from(mContext);
        this.resulted=new ArrayList<String>();
        this.resulted.addAll(locations);

    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public String getItem(int position) {
        return locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.single_location, null);
            holder.name = view.findViewById(R.id.single_city);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(locations.get(position));
        return view;
    }
}
