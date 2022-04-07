package com.gori.acmeexplorer.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Trip {
    private String startCity;
    private String endCity;
    private BigDecimal price;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isSelected;

    public Trip(String startCity, String endCity, BigDecimal price, LocalDate startDate, LocalDate endDate, Boolean isSelected) {
        this.startCity = startCity;
        this.endCity = endCity;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isSelected = isSelected;
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

    @Override
    public String toString() {
        return "Trip{" +
                "startCity='" + startCity + '\'' +
                ", endCity='" + endCity + '\'' +
                ", price='" + price + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public static ArrayList<Trip> createTripsList() {
        ArrayList<Trip> trips = new ArrayList<>();

        trips.add(new Trip("Miami", "Orlando", new BigDecimal(200), LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 14), false));
        trips.add(new Trip("Seville", "Madrid", new BigDecimal(400), LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1), false));
        trips.add(new Trip("Barquisimeto", "Caracas", new BigDecimal(100), LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 3), false));
        trips.add(new Trip("Madrid", "Barcelona", new BigDecimal(50), LocalDate.of(2023, 2, 3), LocalDate.of(2023, 2, 4), false));
        trips.add(new Trip("Valencia", "Madrid", new BigDecimal(56), LocalDate.of(2023, 4, 10), LocalDate.of(2023, 4, 13), false));
        trips.add(new Trip("Quito", "Amsterdam", new BigDecimal(500), LocalDate.of(2023, 7, 1), LocalDate.of(2023, 7, 18), false));

        return trips;
    }
}
