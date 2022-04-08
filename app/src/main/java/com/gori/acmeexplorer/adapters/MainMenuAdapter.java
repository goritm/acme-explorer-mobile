package com.gori.acmeexplorer.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.SelectedTripListActivity;
import com.gori.acmeexplorer.TripListActivity;
import com.gori.acmeexplorer.models.MenuItem;

import java.util.ArrayList;

public class MainMenuAdapter extends BaseAdapter {
    private ArrayList<MenuItem> localDataSet;
    Context context;

    public MainMenuAdapter(ArrayList<MenuItem> localDataSet, Context context) {
        this.localDataSet = localDataSet;
        this.context = context;
    }

    @Override
    public int getCount() {
        return localDataSet.size();
    }

    @Override
    public Object getItem(int i) {
        return localDataSet.get(i);
    }

    @Override
    public long getItemId(int i) {
        return localDataSet.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final MenuItem menuItem = localDataSet.get(i);

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.menu_item, viewGroup, false);
        }

        CardView cardView = view.findViewById(R.id.cvMenuItem);
        ImageView imageView = view.findViewById(R.id.trip_image);
        TextView textView = view.findViewById(R.id.trip_cities);

        imageView.setImageResource(menuItem.getImageId());
        textView.setText(menuItem.getText());
        cardView.setOnClickListener(view2 -> {
            Intent intent = new Intent(context, menuItem.getActivityClass());
            context.startActivity(intent);
        });

        return view;
    }
}
