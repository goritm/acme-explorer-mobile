package com.gori.acmeexplorer.models;

import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.SelectedTripListActivity;
import com.gori.acmeexplorer.TripListActivity;

import java.util.ArrayList;

public class MenuItem {
    private int imageId;
    private String text;
    private Class activityClass;

    public MenuItem(int imageId, String text, Class activityClass) {
        this.imageId = imageId;
        this.text = text;
        this.activityClass = activityClass;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Class getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class activityClass) {
        this.activityClass = activityClass;
    }

    public static ArrayList<MenuItem> createTripsList() {
        ArrayList<MenuItem> menuItems = new ArrayList<>();

        menuItems.add(new MenuItem(R.drawable.viajes_disponibles, "Viajes disponibles", TripListActivity.class));
        menuItems.add(new MenuItem(R.drawable.viajes_seleccionados, "Viajes seleccionados", SelectedTripListActivity.class));

        return menuItems;
    }
}
