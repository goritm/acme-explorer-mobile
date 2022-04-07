package com.gori.acmeexplorer.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Trip implements Serializable {
    private String startCity;
    private String endCity;
    private BigDecimal price;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isSelected;
    private String imageUrl;

    public Trip(String startCity, String endCity, BigDecimal price, LocalDate startDate, LocalDate endDate, Boolean isSelected, String imageUrl) {
        this.startCity = startCity;
        this.endCity = endCity;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isSelected = isSelected;
        this.imageUrl = imageUrl;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static ArrayList<Trip> createTripsList() {
        ArrayList<Trip> trips = new ArrayList<>();

        trips.add(new Trip("Miami", "Orlando", new BigDecimal(200), LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 14), false, "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/04/09/e0/eb/universal-s-islands-of.jpg?w=600&h=400&s=1"));
        trips.add(new Trip("Seville", "Madrid", new BigDecimal(400), LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1), false, "https://www.easyjet.com/ejcms/cache/medialibrary/Images/JSS/Destinations/Hero/Spain_Madrid_3840x2160.jpg?mw=1920&hash=E8335D1B8641F2150C395A3EC48BA45CC0B5BA6D"));
        trips.add(new Trip("Barquisimeto", "Caracas", new BigDecimal(100), LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 3), false, "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/33/fe/d4/caracas.jpg?w=700&h=500&s=1"));
        trips.add(new Trip("Madrid", "Barcelona", new BigDecimal(50), LocalDate.of(2023, 2, 3), LocalDate.of(2023, 2, 4), false, "https://media.cntraveler.com/photos/5a985924d41cc84048ce6db0/master/w_4348,h_3261,c_limit/Catedral-de-Barcelona-GettyImages-511874340.jpg"));
        trips.add(new Trip("Valencia", "Sevilla", new BigDecimal(56), LocalDate.of(2023, 4, 10), LocalDate.of(2023, 4, 13), false, "https://www.lavanguardia.com/files/content_image_mobile_filter/files/fp/uploads/2019/09/30/5fa52ff369941.r_d.328-221.jpeg"));
        trips.add(new Trip("Quito", "Amsterdam", new BigDecimal(500), LocalDate.of(2023, 7, 1), LocalDate.of(2023, 7, 18), false, "https://www.guruwalk.com/blog/wp-content/uploads/2019/10/what-to-do-see-amsterdam.jpg"));

        return trips;
    }
}
