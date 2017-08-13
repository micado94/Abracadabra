package com.apero_area.aperoarea.models;


import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import java.util.List;



/**
 * Created by micka on 10-Aug-17.
 */

public class Product {

    private int id;
    private String name;
    private String description;
    private String price;
    private String average_rating;
    private String rating_count;
    private List<Images> images;
    private String short_description;


    //getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return String.valueOf(stripHtml(description));
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public String getRating_count() {
        return rating_count;
    }

    public void setRating_count(String rating_count) {
        this.rating_count = rating_count;
    }

    public String getShort_description() {
        return String.valueOf(stripHtml(short_description));
    }

    public void setShort_description(String short_description) { this.short_description = short_description;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }




    public Spanned stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
}

