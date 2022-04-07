package com.gori.acmeexplorer.models;

import com.gori.acmeexplorer.R;

import java.util.ArrayList;

public class MenuItem {
    private int imageId;
    private String text;

    public MenuItem(int imageUrl, String text) {
        this.imageId = imageUrl;
        this.text = text;
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

    public static ArrayList<MenuItem> createTripsList() {
        ArrayList<MenuItem> menuItems = new ArrayList<>();

        menuItems.add(new MenuItem(R.drawable.viajes_disponibles, "Viajes disponibles"));
        menuItems.add(new MenuItem(R.drawable.viajes_seleccionados, "Viajes seleccionados"));

        return menuItems;
    }
}
