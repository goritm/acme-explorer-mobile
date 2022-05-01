package com.gori.acmeexplorer.models;

import static com.gori.acmeexplorer.utils.Utils.parseDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Trip implements Serializable {
    private String id;
    private String startCity;
    private String endCity;
    private double price;
    private Date startDate;
    private Date endDate;
    private Boolean isSelected;
    private String imageUrl;

    public Trip() {
    }

    public Trip(String id, String startCity, String endCity, double price, Date startDate, Date endDate, Boolean isSelected, String imageUrl) {
        this.id = id;
        this.startCity = startCity;
        this.endCity = endCity;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isSelected = isSelected;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                ", startCity='" + startCity + '\'' +
                ", endCity='" + endCity + '\'' +
                ", price=" + price +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isSelected=" + isSelected +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(startCity, trip.startCity) && Objects.equals(endCity, trip.endCity) && Objects.equals(price, trip.price) && Objects.equals(startDate, trip.startDate) && Objects.equals(endDate, trip.endDate) && Objects.equals(imageUrl, trip.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startCity, endCity, price, startDate, endDate, isSelected, imageUrl);
    }

    public static ArrayList<Trip> createTripsList() {
        ArrayList<Trip> trips = new ArrayList<>();

//        trips.add(new Trip("Miami", "Orlando", 200, parseDate("2022-05-10"), parseDate("2022-10-14"), false, "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/04/09/e0/eb/universal-s-islands-of.jpg?w=600&h=400&s=1"));
//        trips.add(new Trip("Seville", "Madrid", 400, parseDate("2023-01-01"), parseDate("2023-01-03"), false, "https://www.easyjet.com/ejcms/cache/medialibrary/Images/JSS/Destinations/Hero/Spain_Madrid_3840x2160.jpg?mw=1920&hash=E8335D1B8641F2150C395A3EC48BA45CC0B5BA6D"));
//        trips.add(new Trip("Barquisimeto", "Caracas", 100, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 3), false, "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/33/fe/d4/caracas.jpg?w=700&h=500&s=1"));
//        trips.add(new Trip("Madrid", "Barcelona", 50, LocalDate.of(2023, 2, 3), LocalDate.of(2023, 2, 4), false, "https://media.cntraveler.com/photos/5a985924d41cc84048ce6db0/master/w_4348,h_3261,c_limit/Catedral-de-Barcelona-GettyImages-511874340.jpg"));
//        trips.add(new Trip("Valencia", "Sevilla", 56, LocalDate.of(2023, 4, 10), LocalDate.of(2023, 4, 13), false, "https://www.lavanguardia.com/files/content_image_mobile_filter/files/fp/uploads/2019/09/30/5fa52ff369941.r_d.328-221.jpeg"));
//        trips.add(new Trip("Quito", "Amsterdam", 500, LocalDate.of(2022, 7, 1), LocalDate.of(2022, 7, 18), false, "https://www.guruwalk.com/blog/wp-content/uploads/2019/10/what-to-do-see-amsterdam.jpg"));

        return trips;
    }
}
